/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tui.iec60870.master104.layers;

import org.tui.iec60870.common104.Layers.Ethernet;
import org.tui.iec60870.common.LayerThread;

//==============================================================================
// lib imports
//==============================================================================

import lib.xml.kXMLElement;
import lib.io.StreamConnection;
import lib.io.Connector;

//==============================================================================
// java imports
//==============================================================================

//==============================================================================
// logging
//==============================================================================

import org.tui.iec60870.IEC60870App;

/**
 *
 * @author mikra
 */
public class MEthernet extends Ethernet {

    private StreamConnection conn;

    public MEthernet(LayerThread parent, kXMLElement node) {
        super("MEthernet");
        upper = parent;
        uri = "socket://" + node.getProperty("ip", "127.0.0.1");
        port = node.getProperty("port", 2404);
        retry = node.getProperty("retry", 4);
        this.name = node.getProperty("name", "unbekannt");
    }

    //==========================================================================
    // Methods
    //==========================================================================
    @Override
    public void init() {
            IEC60870App.log("Auf Verbindung zu Station auf Port " + this.port + " wird gewartet.", name);
            this.connected.set(false);
            
            int count =0;

            while ((!this.connected.get()) && (running) && upper != null)
            {
                try {
                    conn = (StreamConnection) Connector.open(this.uri + ":" + port);
                    inp = conn.openInputStream();
                    out = conn.openOutputStream();
                    this.connected.set(true);
                    break;
                }
                catch (Exception e) {
                    count++;
                }
                if (count > 20 || upper == null)
                {
                    return;
                }
            }
            IEC60870App.log("Verbindung zu Station auf Port " + this.port + " hergestellt.", name);
    }

    private void closeClient() {

        
            if (this.connected.get() == false) {
                IEC60870App.log("Verbindung zu nicht aktiver Station soll geschlossen werden.", name);
            } else {
                IEC60870App.log("Verbindung zu aktiver Station soll geschlossen werden.", name);
            }
            this.connected.set(false);
            IEC60870App.log("Verbindung zu Station geschlossen.", name);

            try {

            if (conn != null) {

                conn.close();
                conn = null;
            }

            if (inp != null) {
                inp.close();
                inp = null;
            }

            if (out != null) {
                out.close();
                out = null;
            }
        } catch (Exception e) {
        }


    }

    @Override
    public void run() {
        init();

        if (this.upper != null)
        {
            ((LayerThread) this.upper).comm_error(!this.connected.get());
        }
        else
        {
            this.running = false;
            this.closeClient();
        }

        while (running) {
            try {
                if (this.upper == null)
                {
                    this.running = false;
                    this.closeClient();
                    break;
                }

                if (this.connected.get()) {
                    handle_events();
                    handle_read();
                }
                Thread.sleep(20);
            } catch (InterruptedException e) {
                // thats normal
            } catch (Exception e) {
                IEC60870App.err("Station meldet Error in Layer: " + this.instance + e, name, 3);
            }
        }
    }

    @Override
    public void kill() {
        closeClient();
        super.kill();   
    }
}