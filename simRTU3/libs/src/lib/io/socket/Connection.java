/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lib.io.socket;

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

import lib.io.StreamConnection;
import lib.io.ClosedConnection;

public class Connection implements StreamConnection, ClosedConnection {

    private Socket socket;

    @Override
    public Connection open(String name)
            throws IOException {
        int portSepIndex = name.lastIndexOf(':');
        int port = Integer.parseInt(name.substring(portSepIndex + 1));
        String host = name.substring("socket://".length(), portSepIndex);
        socket = new Socket(host, port);
        
        return this;
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
    public boolean isOpen() {

        return !socket.isClosed();
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
            String port = Integer.toString(this.socket.getPort());
            String ip = this.socket.getInetAddress().toString();
            return "socket://"+ip+":"+port;
        } else {
            return "not connected";
        }
    }

    @Override
    public String get_own_uri() {
        if (this.socket != null) {
             String port = Integer.toString(this.socket.getLocalPort());
            String ip = this.socket.getLocalAddress().toString();
            return "socket://"+ip+":"+port;
        } else {
            return "not connected";
        }
    }
}