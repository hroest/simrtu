/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tui.iec60870.common104.Layers;

import org.tui.iec60870.common104.Features104;
import org.tui.iec60870.common104.TimerSvc;
import org.tui.iec60870.common.LayerThread;
import org.tui.iec60870.common.IEC608705Exception;
import org.tui.iec60870.common.Lock;

import lib.xml.kXMLElement;
import lib.interfaces.queue.QueueException;

import java.util.concurrent.atomic.AtomicBoolean;

//==============================================================================
// logging
//==============================================================================

import org.tui.iec60870.IEC60870App;

/**
 *
 * @author mikra
 */
public abstract class Transport extends LayerThread implements Features104 {

    //this layer should analyze which packages are sent and proove which one should be repeated
    //TimeoutSvc timeoutsvc;
    public TimerSvc timersvc;
    public AtomicBoolean timeout = new AtomicBoolean();
    /** running layer **/
    public boolean STOP;
    public boolean condt = false;
    /** control counters 104 layer **/
    public int nack = 0;
    public int nr = 0;
    public int ns = 0;
    public int count_ack = 0;
    private int itu_x25 = 32767;
    /** Error counters and timeouts **/
    public volatile int error = 0;
    public int MaxError;
    /**configuration**/
    public kXMLElement node;
    /**timeouts**/
    protected Lock TEST_FR_LOCK = new Lock();
    protected long to_TEST_FR = 2000;

    public Transport(String name) {
        super(name);
        this.instance = "TransportLayer";
        this.timeout.set(false);
    }

    public abstract void handle_errors();

    public abstract void proc_init();

    public abstract void comm_test();

    public abstract void wait_for_connect();

    @Override
    public abstract void upperEvent(Object object) throws IEC608705Exception;

    @Override
    public void handle_events() throws IEC608705Exception {

        int count = 0;
        Object obj;
        String msg = null;

        //to avoid buffer overrun
        while (!fromLower.isEmpty() && count < 8) {
            try {
                obj = fromLower.dequeue();
                lowerEvent(obj);
                count++;
            } catch (QueueException e) {
                msg = "Lower: " + e;
            }

        }

        //keine Freigabe zum senden
        if (!condt) {
            return;
        }

        //to avoid buffer overrun
        while (!fromUpper.isEmpty() && this.nack < 8) {
            try {
                obj = fromUpper.dequeue();
                upperEvent(obj);
                count++;
            } catch (QueueException e) {
                if (msg == null) {
                    msg = "Upper: " + e;
                } else {
                    msg = msg + " Upper: " + e;
                }
            }
        }

        if (msg != null) {
            throw new IEC608705Exception(msg);
        }


    }

    @Override
    public void lowerEvent(Object object) throws IEC608705Exception {

        if (timeout.get()) {
            this.error = this.MaxError;
            return;
        }

        byte[] data = (byte[]) object;
        byte[] apdu = null;

        //not a correct apdu
        if ((data.length < 6) || data == null) {
            return;
        }

        try {
            apdu = getAPDU(data);
        } catch (Exception e) {
            IEC60870App.err("APDU nicht analysierbar..." + e, name, 1);
            return;
        }
        if (isSFormat(apdu)) {
            try {
                handleSFormat(apdu);
            } catch (Exception e) {
                IEC60870App.err("Prozedur SFormat nicht durchführbar..." + e, name, 1);
            }
            return;
        }

        if (isUFormat(apdu)) {
            try {
                handleUFormat(apdu);
            } catch (Exception e) {
                IEC60870App.err("Prozedur UFormat nicht durchführbar..." + e, name, 1);
            }
            return;
        }

        if (isIFormat(apdu)) {

            //keine Freigabe für IFormat
            if (!condt) {
                return;
            }

            try {
                handleIFormat(apdu);
            } catch (Exception e) {
                IEC60870App.err("Prozedur IFormat nicht durchführbar..." + e, name, 1);
            }
            return;
        }
    }

    private void handleUFormat(byte[] apdu) {
        //to avoid failure causes from signs add 0xFF
        //example 0x97 results -105, but the right value should be 105
        //105 in bits is also the first 8 bits of -105

        if (apdu.length != 6) {
            String msg = "";
            for (int ii = 0; ii < apdu.length; ii++) {
                msg = msg + "[" + Byte.toString(apdu[ii]) + "] ";
            }
            IEC60870App.err("APDUU falscher Länge empfangen: " + msg, name, 1);
            return;
        }

        switch (apdu[2]) {

            case TESTFR_ACT:
                IEC60870App.log("Empfange TESTFR_ACT. Sende TESTFR_CON.", name);
                sendTESTFRAME_CON();
                break;

            case TESTFR_CON:
                //handle timeouts
                IEC60870App.log("Empfange TESTFR_CON.", name);
                this.TEST_FR_LOCK.unlock();
                this.comm_test();
                break;

            case STARTDT_ACT:
                IEC60870App.log("Empfange STARTDT_ACT. Sende STARTDT_CON.", name);
                this.condt = true;
                sendSTARTDT_CON();
                break;

            case STOPDT_ACT:
                IEC60870App.log("Empfange STOPDT_ACT. Sende STOPDT_CON.", name);
                IEC60870App.err("Station beendet Datenübertragung.", instance, 3);
                this.condt = false;
                sendSTOPDT_CON();
                break;

            case STOPDT_CON:
                IEC60870App.log("Empfange STOPDT_CON.", name);
                this.condt = false;
                break;

            case STARTDT_CON:
                IEC60870App.log("Empfange STARTDT_CON.", name);
                this.condt = true;
                upper.init_from_lower();
                break;
            default:
                break;
        }
    }

    private void handleSFormat(byte[] apdu) {
        
        IEC60870App.log("SFormat empfangen.", name);

        if (apdu.length != 6) {
            String msg = "";
            for (int ii = 0; ii < apdu.length; ii++) {
                msg = msg + "[" + Byte.toString(apdu[ii]) + "] ";
            }
            IEC60870App.err("APDUS falscher Länge von Station empfangen:" + msg, name, 1);
            return;
        }

        int ak = (((apdu[4] & 0xFE) >> 1) + ((apdu[5] & 0xFF) << 7));
        acknowledge(ak);
        String msg = "Von Station " + name + " SAPDU empfangen, von Station bestätigt: " + ak;
        IEC60870App.log(msg, name);
    }

    private void handleIFormat(byte[] apdu) {

        IEC60870App.log("IFormat empfangen.", name);

        if (apdu.length == 6) {

            String msg = "";
            for (int ii = 0; ii < 6; ii++) {
                msg = msg + "[" + Byte.toString(apdu[ii]) + "] ";
            }

            IEC60870App.err("APDUI ohne ASDU Telegramm von Station empfangen: " + msg, name, 1);
            return;
        }

        if (apdu.length < 6) {
            String msg = "";
            for (int ii = 0; ii < apdu.length; ii++) {
                msg = msg + "[" + Byte.toString(apdu[ii]) + "] ";
            }
            IEC60870App.err("Fehlerhaftes Telegramm abgefangen. Minimale Größe unterschritten: " + msg, name, 1);
            return;
        }

        if (((apdu[1]) & 0xFF) + 2 != apdu.length) {
            String msg = "";
            for (int ii = 0; ii < apdu.length; ii++) {
                msg = msg + "[" + Byte.toString(apdu[ii]) + "] ";
            }
            IEC60870App.err("Fehlerhaftes Telegramm von Station abgefangen. Größe falsch: " + msg, name, 1);
            return;
        }

        if (this.count_ack < 8) {
            count_ack++;
        } else {
            this.count_ack = 0;
            this.sendSFORMAT();
        }

        nr = itu_x25counting(nr);

        int ak = (((apdu[4] & 0xFE) >> 1) + ((apdu[5] & 0xFF) << 7));

        String msg = "Von Station " + name + " IAPDU empfangen: " + nr + ", IAPDU gesendet: " + ns + ", von Station bestätigt: " + ak;

        IEC60870App.log(msg, name);
        acknowledge(ak);

        if (apdu.length > 6) {
            short[] asdu = get_asdu(apdu);
            send(asdu);
        }
    }

    private boolean isSFormat(byte[] apdu) {
        if ((apdu[2] & 0x3) == 1) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isUFormat(byte[] apdu) {
        if ((apdu[2] & 0x3) == 3) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isIFormat(byte[] apdu) {
        if ((apdu[2] & 0x1) == 0) {
            return true;
        } else {
            return false;
        }
    }

    public void sendSTARTDT_ACT() {
        IEC60870App.log("Sende STARTDT_CON.", name);
        

        byte[] apdu = new byte[6];
        apdu[0] = 104;
        apdu[1] = 4;
        apdu[2] = STARTDT_ACT;
        apdu[3] = 0;
        apdu[4] = 0;
        apdu[5] = 0;
        try {
            write(apdu);
        } catch (Exception e) {
            IEC60870App.err("Kann Frame STARTDTACT nicht schreiben.", name, 1);
            error++;
            return;
        }
    }

    public void sendSTARTDT_CON() {


        byte[] apdu = new byte[6];
        apdu[0] = 104;
        apdu[1] = 4;
        apdu[2] = STARTDT_CON;
        apdu[3] = 0;
        apdu[4] = 0;
        apdu[5] = 0;
        try {
            write(apdu);
        } catch (Exception e) {
            IEC60870App.err("Kann Frame STARTDTCON nicht schreiben.", name, 1);
            error++;
            return;
        }
    }

    public void sendSTOPDT_ACT() {

       IEC60870App.log("Send STOPDT_ACT.", name);

        byte[] apdu = new byte[6];
        apdu[0] = 104;
        apdu[1] = 4;
        apdu[2] = STOPDT_ACT;
        apdu[3] = 0;
        apdu[4] = 0;
        apdu[5] = 0;
        try {
            write(apdu);
        } catch (Exception e) {
            IEC60870App.err("Kann Frame STOPACT nicht schreiben.", name, 1);
            error++;
            return;
        }

    }

    public void sendSTOPDT_CON() {
        
    

        byte[] apdu = new byte[6];
        apdu[0] = 104;
        apdu[1] = 4;
        apdu[2] = STARTDT_CON;
        apdu[3] = 0;
        apdu[4] = 0;
        apdu[5] = 0;
        try {
            write(apdu);
        } catch (Exception e) {
            IEC60870App.err("Kann Frame STOPDTCON nicht schreiben.", name, 1);
            error++;
            return;
        }
    }

    public void sendTESTFRAME_ACT() {
        IEC60870App.log("Sende TESTFRAME_ACT.", name);

        if (this.timeout.get() || TEST_FR_LOCK.isLocked()) {
            return;
        }

        byte[] TESTFRAME = new byte[6];
        TESTFRAME[0] = 0x68;
        TESTFRAME[1] = 4;
        TESTFRAME[2] = TESTFR_ACT;
        TESTFRAME[3] = 0;
        TESTFRAME[4] = 0;
        TESTFRAME[5] = 0;
        try {
            write(TESTFRAME);
        } catch (Exception e) {
            IEC60870App.err("Kann Frame TESTFRAMEACT nicht schreiben.", name, 1);
            error++;
            return;
        }

        //temporärer thread, der den lock verwaltet
        Thread t = new Thread() {

            @Override
            public void run() {
                TEST_FR_LOCK.lock();
                if (!TEST_FR_LOCK.waiting(to_TEST_FR)) {
                    IEC60870App.log("Station meldet Timeout.", name);
                    error = MaxError;
                    TEST_FR_LOCK.unlock();
                    timeout.set(true);
                }
            }
        };
        t.start();
    }

    public void sendTESTFRAME_CON() {

        

        byte[] TESTFRAME = new byte[6];

        TESTFRAME[0] = 0x68;
        TESTFRAME[1] = 4;
        TESTFRAME[2] = TESTFR_CON;
        TESTFRAME[3] = 0;
        TESTFRAME[4] = 0;
        TESTFRAME[5] = 0;

        try {
            write(TESTFRAME);
        } catch (Exception e) {
            IEC60870App.err("Station kann Frame TESTFRAMECON nicht schreiben.", name, 1);
            error++;
        }
    }

    public byte[] s2b(short[] data) {
        byte[] job = new byte[data.length];
        for (int ii = 0; ii < data.length; ii++) {
            job[ii] = (byte) (data[ii] & 0xFF);
        }
        return job;
    }

    public short[] b2s(byte[] data) {
        short[] job = new short[data.length];
        for (int ii = 0; ii < data.length; ii++) {
            job[ii] = (short) (data[ii] & 0xFF);
        }
        return job;
    }

    private byte[] getAPDU(byte[] inrx) {

        int length = (inrx[1] & 0xFF) + 2;

        byte[] job = new byte[length];

        if (inrx[0] == Start) {
            for (int ii = 0; ii < length; ii++) {
                job[ii] = (byte) (inrx[ii]);
            }
            return job;
        } else {
            return null;
        }
    }

    private short[] get_asdu(byte[] apdu) {
        int length = apdu.length;

        short[] asdu = new short[length - 6];
        for (int ii = 6; ii < length; ii++) {
            asdu[ii - 6] = (short) (apdu[ii] & 0xFF);
        }
        return asdu;
    }

    private void acknowledge(int ack) {

        // example itu_x25 counting
        //
        // ack = 32766 send 1 ns = 32767    -> ack = 32766       nack = 1
        // ack = 32767 send 1 ns = 1        -> ack = 0           nack = 1
        // ack = 1     send 0 ns = 1        -> ack = 1           nack = 0

        if ((ack > ns) && ((ack - itu_x25) < ns) && ((ack - itu_x25) >= 0)) {
            ack = ack - itu_x25;
        }

        IEC60870App.log("Bestätigte IAPDU's: " + ack, name);

        if (ack > ns) {
            IEC60870App.err("Mehr Pakete bestätigt als gesandt.", name, 1);

            this.error = MaxError;
            return;
        }

        if (ack < ns) {
            this.nack = ns - ack;
        } else if (ack == ns) {
            this.nack = 0;
        }

        if (this.nack > 8) {
            IEC60870App.err("Mehr als 12 Pakete unbestätigt", name, 1);
            this.error = MaxError;
            return;
        }

    }

    private void sendSFORMAT() {
        IEC60870App.log("Station " + name + " sendet SFORMAT. Bestätige empfangene IAPDU: " + nr, name);

        byte[] apdu = new byte[6];
        apdu[0] = 104;
        apdu[1] = 4;
        apdu[2] = 1;
        apdu[3] = 0;
        apdu[4] = (byte) ((nr & 0x7F) << 1);
        apdu[5] = (byte) ((nr & 0x7F80) >> 7);
        write(apdu);
    }

    @Override
    public void init_from_lower() {
    }

    @Override
    public void init_from_upper() {
    }

    @Override
    public void comm_error(boolean val) {
        try {
            this.upper.comm_error(val);
            if (val) {
                this.error = this.MaxError;
                this.empty_Queues();
                this.connected.set(false);
                this.TEST_FR_LOCK.unlock();
            } else {
                error = ns = nr = nack = 0;
                this.connected.set(true);
            }
        } catch (Exception e) {
            IEC60870App.err("Fehler im Transportlayer (Prozedur common104/Layers/Transport/comm_error): " + e, name, 1);
        }
    }

    @Override
    public void run() {

        IEC60870App.msg("Starte 104er Applikation", name, 1);
        
        this.wait_for_connect();

        IEC60870App.msg("Verbindung hergestellt.", name, 1);
        ns = nr = error = 0;

        proc_init();

        try
        {
            timersvc.activate();
        }
        catch(Exception e)
        {
            IEC60870App.msg("Kann Timerservice nicht starten " + e, name, 3);
        }

        while (running) {

            try {

                if (this.timeout.get()) {
                    this.error = this.MaxError;
                }
                handle_errors();
                handle_events();
                Thread.sleep(100);
            } catch (Exception e) {
                IEC60870App.err("Fehler im Transportlayer (Prozedur common104/Layers/Transport/run): " + e, name, 1);
            }

            try
            {
                
            }
            catch(Exception e)
            {

            }
        }
    }

    public void fire_timer(int index_timer) {
        switch (index_timer) {
            case 1:
                if (this.TEST_FR_LOCK.isLocked()) {
                    this.timeout.set(true);
                }
                if (!this.timeout.get()) {
                    this.proc_init();
                }
                break;
        }
    }

    protected int itu_x25counting(int counter) {
        if (counter < itu_x25) {
            counter++;
        } else {
            counter = 0;
        }

        return counter;
    }

    @Override
    public void kill() {
        running = false;
        this.timersvc.pause();
        if (lower != null) {
            lower.kill();
        }
        lower = null;
        upper = null;
        thread.interrupt();
        thread = null;
    }
    
}
