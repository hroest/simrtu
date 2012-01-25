/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tui.iec60870.common104.Layers;

import org.tui.iec60870.common.LayerThread;
import org.tui.iec60870.IEC60870App;

import java.io.OutputStream;
import java.io.InputStream;

import org.tui.iec60870.common.IEC608705Exception;

/**
 *
 * @author mikra
 */
public abstract class Ethernet extends LayerThread {

    //==========================================================================
    // Stream Attributes
    //==========================================================================
    protected int port = 2404;
    protected String uri = "socket://127.0.0.1";
    protected int retry = 4;
 
    //==========================================================================
    // private Attributes
    //==========================================================================
    protected OutputStream out;
    protected InputStream inp;

    public Ethernet(String name) {
        super("Ethernet");
        this.instance = "EthernetLayer";
        this.lower = null;
    }

    @Override
    public void comm_error(boolean val) {
        this.connected.set(false);
        upper.comm_error(val);
    }

    protected void handle_read() {
        //if some data exists this method reads them
        int remaing_retry = this.retry;
        while (remaing_retry >= 0) {
            try {


                int leng = inp.available();
                if ((leng > 0)) {
                    byte[] b = new byte[leng];
                    int length = inp.read(b);
                    if (length == 0) {
                        return;
                    }
                    send(b);
                }
                return;
            } catch (Exception e) {
                if (remaing_retry > 0) {
                    remaing_retry--;
                } else {
                    IEC60870App.log("Verbindung zu Station + " + this.name + " geschlossen.", "Ethernet");
                    this.comm_error(true);
                    return; //maximum retries occured -> break up and handle error...
                }
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                //thats normal
            }
        }
    }

    @Override
    protected void upperEvent(Object object) throws IEC608705Exception {

        int remaing_retry = this.retry;
        byte[] data = (byte[]) object;

        while (remaing_retry >= 0) {
            try {
                if (data[0] == 104) {
                    out.write(data);
                }
                return; //if no error occured return and break up
            } catch (Exception e) {
                if (remaing_retry > 0) {
                    remaing_retry--;
                } else {
                    IEC60870App.log("Verbindung zu Station + " + this.name + " geschlossen.", "Ethernet");
                    this.comm_error(true);
                    return; //maximum retries occured -> break up and handle error...
                }
            }

            //sleep if error occured
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                //thats normal
            }
        }
    }

    @Override
    public void kill() {
        super.kill();
    }

    /***************************************************************************
     *
     * Abstract methods...
     *
     ***************************************************************************
     */
    public abstract void init();

    /***************************************************************************
     *
     * Never running methods...
     * 
     ***************************************************************************
     */
    @Override
    protected void lowerEvent(Object object) throws IEC608705Exception {
        //never runs
    }

    @Override
    public void init_from_lower() {
        //never runs
    }

    @Override
    public void init_from_upper() {
        //never runs
    }
}
