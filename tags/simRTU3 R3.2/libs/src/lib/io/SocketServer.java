/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lib.io;

import lib.util.Hashtable;
import java.io.IOException;
import java.util.Enumeration;

/**
 * General purpose socket server.
 * <p>
 * Modifications:
 *
 * <dl>
 * <dt>8.IV.2003
 * <dd>The servers are now pooled and reused to offer the best performances.
 * <dt>15.IV.2003
 * <dd>Added support for PPP servers on the same port than Ethernet.
 * </dl>
 *
 * @author <a href="mailto:alexis.bietti@sysaware.com">Alexis BIETTI</a>
 * @version 1.1 - 8.IV.2003
 */
public class SocketServer extends Thread {
    //---------------------------------------------------------------------------
    // Static fields
    //---------------------------------------------------------------------------

    /** If true, we print debug info on System.out */
    static final boolean DEBUG = true;
    /** Max number of concurent server sockets. */
    static int MAX_SERVER_SOCKETS = 5;
    /** A pool server sockets already in use. Negative values are used for PPP */
    static Hashtable POOL = new Hashtable();
    /** Number of retries to open the server socket */
    static final int RETRIES = 3;
    //---------------------------------------------------------------------------
    // Attributes
    //---------------------------------------------------------------------------
    /** Whether we accept time outs. */
    boolean timeout = false;
    /** Set to true when the thread starts */
    boolean running = false;
    /** The server we open */
    StreamConnectionNotifier server = null;
    /**Handler**/
    private Hashtable Sockets = new Hashtable();
    String server_uri = "";

    //---------------------------------------------------------------------------
    // Constructor
    //---------------------------------------------------------------------------
    SocketServer() {
        super();
    }

    //---------------------------------------------------------------------------
    // Static methods
    //---------------------------------------------------------------------------
    /**
     * Open and accept connection on the specified port.
     *
     * @param port an IP port
     * @param handler an handler for the incoming connections.
     *
     * @throws IOException if something bad happens.
     */
    /**
     * Open and accept connection on the specified port.
     *
     * @param port an IP port
     * @param handler an handler for the incoming connections.
     * @param ppp whether we open the port on ethernet/ppp
     *
     * @throws IOException if something bad happens.
     */
    public static void open(String uri, AbstractSocketHandler handler) throws IOException {
        // Check if this port is already open
        SocketServer s = (SocketServer) POOL.get(uri);

        if (s == null) {
            s = new SocketServer();
            s.server_uri = uri;
            POOL.put(uri, s);
        }

        s.Sockets.put(handler.hashCode(), handler);

        if (!s.running) {
            s.start();
        }
    }

    /**
     * If the specified port was previously open, this method will close it and
     * the previously specified handler will no more be notified.
     *
     * @param port an IP port.
     *
     * @throws IOException if something wick happens.
     */
    public static void close(String uri, AbstractSocketHandler handler) throws IOException {
        // Is this port already open ?
        SocketServer s = (SocketServer) POOL.get(uri);

        if (s == null) { // No, we have nothing to do
            return;
        }

        AbstractSocketHandler h = (AbstractSocketHandler) s.Sockets.get(handler.hashCode());

        if (h == null) { // No, we have nothing to do
            return;
        }

        s.Sockets.remove(handler.hashCode());

    }

    /**
     * Close the server
     */
    void closeServer() {
        if (server != null) {
            try {
                server.close();
            } catch (Exception e) {
            }
        }
        server = null;
    }

    /**
     * Starts this server.
     */
    @Override
    public void run() {
        running = true;
        StreamConnection socket = null;
        
        while (running) {
            try {

                if (server == null) {
                    server = (StreamConnectionNotifier) Connector.open(server_uri, Connector.READ_WRITE, timeout);
                }
                //open port

                if (server != null) {
                    socket = server.acceptAndOpen();
                    String uri = socket.get_foreign_uri();
                    System.out.println("Socketserver - eingehende Verbindung von " + uri + ".");
                }

                Enumeration e = Sockets.elements();

                while (e.hasMoreElements()) {
                    AbstractSocketHandler handler = (AbstractSocketHandler) e.nextElement();
                    String furi1=handler.get_foreign_uri();
                    String furi2=socket.get_foreign_uri();
                    
                    if (furi1.equals(furi2)) {
                        handler.handle(socket);
                    }
                }

            } catch (Exception e) {
            }
        }
    }
}
