/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lib.io.socketserver;

/**
 * http://www.docjar.org/download.jsp?q=javax.microedition.io.Connection
 * @author mikra
 */
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.ServerSocket;

import lib.io.StreamConnection;
import lib.io.ClosedConnection;
import lib.io.StreamConnectionNotifier;

public class Connection implements StreamConnection, ClosedConnection, StreamConnectionNotifier {

    private Socket socket;
    private ServerSocket serversocket;

    @Override
    public Connection open(String name)
            throws IOException {
        int portSepIndex = name.lastIndexOf(':');
        int port = Integer.parseInt(name.substring(portSepIndex + 1));
        serversocket = new ServerSocket(port);
        return this;
    }

    @Override
    public StreamConnection acceptAndOpen() throws IOException {
        this.socket = serversocket.accept();
        return (StreamConnection) this;
    }

    @Override
    public boolean isOpen() {

        byte[] tmp = null;
        int result=0;
        try
        {
        result = this.socket.getInputStream().read(tmp,0, 0);
        }
        catch(Exception e)
        {
            
        }
        return true;
    }

    @Override
    public void close()
            throws IOException {
        if (socket == null) {
            return;
        }

        socket.close();
    }

    @Override
    public InputStream openInputStream()
            throws IOException {
        if (socket == null) {
            throw new IOException();
        }

        return socket.getInputStream();
    }

    @Override
    public DataInputStream openDataInputStream()
            throws IOException {
        return new DataInputStream(openInputStream());
    }

    @Override
    public OutputStream openOutputStream()
            throws IOException {
        if (socket == null) {
            throw new IOException();
        }

        return socket.getOutputStream();
    }

    @Override
    public DataOutputStream openDataOutputStream()
            throws IOException {
        return new DataOutputStream(openOutputStream());
    }

    @Override
    public String get_foreign_uri() {

        if (this.socket != null) {
            
            String ip = this.socket.getInetAddress().toString();
            return "socketserver:/"+ip;

        } else {
            return "not connected";
        }
    }

    @Override
    public String get_own_uri() {
        if (this.socket != null) {
            
            String ip = this.socket.getLocalAddress().toString();
            return "socketserver://"+ip;
        } else {
            return "not connected";
        }
    }
}