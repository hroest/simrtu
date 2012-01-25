/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tui.iec60870.master104.layers;

import org.tui.iec60870.IEC60870App;

import org.tui.iec60870.common.LayerThread;
import org.tui.iec60870.common.IEC608705Exception;

import org.tui.iec60870.master101.MApplication101;

import org.tui.iec60870.common104.TimerSvc;
import org.tui.iec60870.common104.Layers.Transport;

import lib.xml.kXMLElement;

/**
 *
 * @author Micha
 */
public class MTransport extends Transport {
        String ip ="";

    public MTransport(LayerThread parent, kXMLElement anode) {
        super(anode.getProperty("name", "unbekannt"));
        upper = parent;
        lower = new MLink(this, anode);
        node = anode;
        timersvc = new TimerSvc(this, (long) anode.getProperty("104TInterval", 5) * 1000, 1);
        MaxError = (int) anode.getProperty("104MaxError", 3);
        ip = node.getProperty("ip", "127.0.0.1");
        
        to_TEST_FR = (long) anode.getProperty("104TTimeout", 5) * 1000;
    }

    @Override
    public void upperEvent(Object data) throws IEC608705Exception {

        if (timeout.get()) {
            this.error = this.MaxError;
            return;
        }

        if (this.nack > 8) {
            this.error = this.MaxError;
        }

        if (this.error >= this.MaxError) {
            this.handle_errors();
        }

        short apdu[] = null;
        if (data != null) {
            int index = 0;
            short[] asdu = (short[]) data;
            apdu = new short[asdu.length + 6];
            apdu[index++] = 0x68;
            apdu[index++] = (short) (asdu.length + 4);
            apdu[index++] = (short) ((ns & 0x7F) << 1);
            apdu[index++] = (short) ((ns & 0x7F80) >> 7);
            apdu[index++] = (short) ((nr & 0x7F) << 1);
            apdu[index++] = (short) ((nr & 0x7F80) >> 7);
            for (int j = 0; j < asdu.length; j++) {
                apdu[index++] = asdu[j];
            }

            try {
                write(s2b(apdu));
            } catch (Exception e) {
                error++;
                return;
            }
            ns = this.itu_x25counting(ns);
            this.nack++;
        }
    }

    @Override
    public void handle_errors() {
        if (error >= MaxError) {

            // close lower Layers
            IEC60870App.msg("Zentrale schließt Verbindung.", name , 1);
            lower.kill();
            lower = new MLink(this, node);

            // reset variables
            this.timersvc.pause();
            this.timersvc = new TimerSvc(this, (long) node.getProperty("104TInterval", 5) * 1000, 1);
            this.timeout.set(false);
            this.condt = false;
            this.connected.set(false);
            error = ns = nr = nack = 0;

            // launch lower layer
            ((MLink) lower).launch();

            // go to wait state
            IEC60870App.msg("Zentrale wartet auf Verbindung.", name, 1);
            this.wait_for_connect();

            // analyze for connect
            if (this.error == MaxError)
            {
                return;
            }
            else
            {
                upper.comm_error(true);
            }

            IEC60870App.msg("Zentrale ist verbunden.", name, 1);
            
            //reinit system
            proc_init();
            this.timeout.set(false);
            try
            {
                this.timersvc.activate();
            }
            catch(Exception e)
            {
                IEC60870App.err("Kann Timerservice nicht fortsetzen " + e, name, 3);
            }
            //comm_error back
            upper.comm_error(false);
        }
    }

    @Override
    public void proc_init() {
        sendTESTFRAME_ACT();
        if (!this.condt) {
            sendSTARTDT_ACT();
        }
    }

    @Override
    public void comm_test()
    {
        try
        {
            ((MApplication101)this.upper).comm_test();
        }
        catch(Exception e)
        {
           
        }
    }

    @Override
    public void wait_for_connect() {
        
        try {
            ((MApplication101) this.upper).wait_for_connect();
            int count = 0; //watchdog
            while(!this.connected.get())
            {
                try
                {
                    count++;
                    Thread.sleep(1000);
                }
                catch(Exception e)
                {
                }
                if (count >= 2*60)
                {
                    IEC60870App.err("Station nicht erreichbar." ,name + " -> " + ip , 1);
                    this.error = this.MaxError;
                    return;
                }
            }
        } catch (Exception e)
        {
        
        }
    }
}