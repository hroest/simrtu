/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tui.iec60870.common104.Layers;

import org.tui.iec60870.IEC60870App;
import org.tui.iec60870.common.LayerThread;
import org.tui.iec60870.common.IEC608705Exception;

/**
 * @author Michael Kratz, July 2009
 * @version 1.0
 */
public class Link extends LayerThread {

    //constructor
    public Link(String name) {
        super(name);
        this.instance = "LinkLayer";
    }

    //lower event procedure
    @Override
    protected synchronized void lowerEvent(Object object) throws IEC608705Exception {

        byte[] data = (byte[]) object;
        int length = data.length;
        String msg = "Data length: " + Integer.toString(length) + " Package: ";

        try {


            //for debugging purposes write all package date to file
            for (int ii = 0; ii < length; ii++) {
                msg = msg + "[" + Integer.toString(data[ii] & 0xFF) + "] ";
            }

            msg = msg + "!";

            IEC60870App.log("Empfangenes 104er Package: " + msg, name);

        } //divide packages into atomar ones
        catch (Exception e) {
            IEC60870App.err("Fehler im Linklayer (Datenpaket): " + msg + " " + e, name, 3);
        }


        
        for (int ii = 0; ii < length; ii++) {
            msg="";
            try {

                //starting point equals 104
                if (ii >= length) {
                    return;
                }

                if ((data[ii] & 0xFF) == 104) {
                    //length of one package is coded

                    byte[] rxd = new byte[(data[ii + 1] & 0xFF) + 2];

                    try {
                        for (int j = 0; j < rxd.length; j++) {
                            if (ii >= length) {
                                IEC60870App.log("Fehlerhafte Paketlänge, verwerfe Paket der Länge :" + j + ":" + msg, name);
                                return;
                            }
                            rxd[j] = data[ii];
                            msg = msg + "[" + Integer.toString(data[ii] & 0xFF) + "] ";
                            if (j < rxd.length) {
                                ii++;
                            }
                        }
                    } catch (Exception e) {
                        IEC60870App.err("Fehler im Linklayer (Einzelpaket decodieren): " + e, name, 3);

                    }
                    try {
                        send(rxd);
                        ii--;
                        IEC60870App.log("Empfangenes 104er Einzelpacket: " + msg, name);
                    } catch (Exception e) {
                        IEC60870App.err("Fehler im Linklayer (Paket an TransportLayer senden): " + e, name, 3);
                    }
                }

            } catch (Exception e) {
                //if an error occurs
                IEC60870App.err("Fehler im Linklayer (Datenpaket decodieren): " + e + " Index Paket: " + Integer.toString(ii) + " " + msg, name, 3);
            }
        }
    }

    @Override
    protected synchronized void upperEvent(Object object) throws IEC608705Exception {

        //typecast from object to byte[]
        byte[] data = (byte[]) object;

        //for debbuging purposes write to file
        String msg = "";
        try {
            for (int ii = 0; ii < data.length; ii++) {
                msg = msg + "[" + Integer.toString(data[ii] & 0xFF) + "] ";
            }
        } catch (Exception e) {
            IEC60870App.err("Fehler im Linklayer (Paket codieren): " + e + " " + msg, name, 3);
        }


        //to lower layer

        IEC60870App.log("Sende 104er Paket: " + msg, name);

        try {
            write(object);
        } catch (Exception e) {
            IEC60870App.err("Fehler im Linklayer (Paket senden): " + e + " " + msg, name, 3);
            this.comm_error(true);
        }
    }

    @Override
    public void init_from_lower() {
        //not used
    }

    @Override
    public void init_from_upper() {
        //not used
    }

    @Override
    public void comm_error(boolean val) {
        //used if an error has been detected
        if (val)
        {
            this.empty_Queues();
        }
        this.connected.set(!val);
        upper.comm_error(val);
    }
}
