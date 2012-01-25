/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tui.iec60870.slave104.layers;

import org.tui.iec60870.common104.TimerSvc;
import org.tui.iec60870.common.LayerThread;
import lib.xml.kXMLElement;
import org.tui.iec60870.common104.Layers.Transport;
import org.tui.iec60870.common.IEC608705Exception;
import org.tui.iec60870.IEC60870App;

/**
 *
 * @author Micha
 */
public class STransport extends Transport {

    public STransport(LayerThread parent, kXMLElement anode) {
        super("STransport");
        upper = parent;
        lower = new SLink(this, anode);
        node = anode;
        //timeoutsvc = new TimeoutSvc(this,(long) anode.getProperty("104TTimeout", 5) * 1000);
        timersvc = new TimerSvc(this, (long) anode.getProperty("104TInterval", 5) * 1000, 1);
        MaxError = (int) anode.getProperty("104MaxError", 3);
        name = anode.getProperty("name", "unbekannt");
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

            ns = itu_x25counting(ns);
            this.nack++;
        }
    }

    @Override
    public void handle_errors() {

        if (error >= MaxError) {
            upper.comm_error(true);

            this.fromUpper.emptyQueue();
            this.fromLower.emptyQueue();

            IEC60870App.msg("Station schließt Verbindung.", name, 1);
            lower.kill();
            lower = new SLink(this, node);
            ((SLink) lower).launch();
            error = ns = nr = nack = 0;
            this.timersvc.pause();
            this.timersvc = new TimerSvc(this, (long) node.getProperty("104TInterval", 5) * 1000, 1);

            this.timeout.set(false);


            this.condt = false;
            this.connected.set(false);

            IEC60870App.msg("Station wartet auf Verbindung.", name, 1);
            this.wait_for_connect();
            IEC60870App.msg("Station verbunden.", name, 1);

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
    }

    @Override
    public void comm_test()
    {
        
    }

    @Override
    public void wait_for_connect()
    {
        while (connected.get() == false) {
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                }
            }
    }

}