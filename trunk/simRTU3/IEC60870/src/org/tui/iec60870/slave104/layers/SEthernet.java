/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tui.iec60870.slave104.layers;

import org.tui.iec60870.common104.Layers.Ethernet;
import org.tui.iec60870.common.LayerThread;
import org.tui.iec60870.IEC60870App;

import lib.io.AbstractSocketHandler;
import lib.io.SocketServer;
import lib.io.StreamConnection;

import lib.xml.kXMLElement;

/**
 *
 * @author Administrator
 */
public class SEthernet extends Ethernet implements AbstractSocketHandler {

    private StreamConnection socket=null;

    private String central_uri = "";
    private String own_uri = "";

    public SEthernet(LayerThread parent, kXMLElement node) {
        super("SEthernet");
        this.upper = parent;
        central_uri = "socketserver://" + node.getProperty("masterip", "127.0.0.1");
        own_uri = "socketserver://" + node.getProperty("ip", "127.0.0.1");
        port = node.getProperty("port", 2404);
        retry = node.getProperty("retry", 4);
    }

    @Override
    public void init_from_upper() {
    }

    @Override
    public void kill() {
        closeServer();
        super.kill();
    }

    private void openServer() {
        try {
            SocketServer.open(own_uri + ":" + Integer.toString(port), this);
        } catch (Exception e) {
        }
    }

    private void closeServer() {
        try {
            SocketServer.close(own_uri, this);
        } catch (Exception e) {
        }
        this.connected.set(false);
    }

    @Override
    public void init() {
        this.connected.set(false);
        openServer();
    }

    @Override
    public void run() {
        init();
        while (running) {
            try {
                check_connection();
                if (this.connected.get()) {
                    handle_events();
                    handle_read();
                }
                Thread.sleep(20);
            } catch (Exception e) {
                IEC60870App.err("Station + " + this.uri + " meldet Error: " + e, name, 1);
            }
        }
    }

    private void check_connection()
    {
        if (this.connected.get()) {
            if (this.socket != null) {
                this.socket.isOpen();
            } else {
                this.connected.set(false);
                this.upper.comm_error(true);
            }
        }
    }

    @Override
    public void handle(StreamConnection socket) {
        try {
            this.socket = socket;
            this.inp = socket.openInputStream();
            this.out = socket.openOutputStream();
            this.connected.set(true);
            this.upper.comm_error(false);
        } catch (Exception e) {
            this.inp = null;
            this.out = null;
            return;
        }
    }

    @Override
    public boolean valid_uri(String uri) {
        if (uri.equals(this.central_uri)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String get_foreign_uri() {
        return central_uri;
    }

    @Override
    public String get_own_uri() {
        return own_uri;
    }
}
