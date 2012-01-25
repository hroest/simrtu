/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tui.iec60870.common;

import lib.util.FifoQueue;
import lib.interfaces.queue.QueueException;
import org.tui.iec60870.IEC60870App;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.Hashtable;

/**
 * <p>
 * This class defines a layer with thread abilities. It describes
 * the basic mechanisms used by such layers to communicate.
 *
 * @author lionnel cauvy
 * @version 1.0
 * @date 01.II.03
 */
public abstract class LayerThread extends Layer implements Runnable {

    protected String name;
    protected String instance;
    public boolean running;
    protected Thread thread;
    protected FifoQueue fromUpper;
    protected FifoQueue fromLower;
    public AtomicBoolean connected = new AtomicBoolean();

    protected static long T_id = 0;

    @Override
    public String toString() {
        return name;
    }

    protected abstract void upperEvent(Object object) throws IEC608705Exception;

    protected abstract void lowerEvent(Object object) throws IEC608705Exception;


    @Override
    public void send(Object object) {
        if (connected.get()) {
            upper.read(object);
        }
    }

    @Override
    public void receive(Object object) {
        if (connected.get()) {
            fromUpper.enqueue(object);
        }
    }

    @Override
    public void write(Object object) {
        if (connected.get()) {
            lower.receive(object);
        }
    }

    @Override
    public void read(Object object) {
        if (connected.get()) {
            fromLower.enqueue(object);
        }
    }

    /**
     * Kill order received from upper. This layer MUST send down this
     * order before stopping itself.
     */

    public void empty_Queues()
    {
        // special handling, to empty queues...

        try
        {
            this.fromLower.emptyQueue();
        }
        catch(Exception e)
        {
        }

        try
        {
            this.fromLower.emptyQueue();
        }
        catch(Exception e)
        {
        }
    }

    @Override
    public void kill() {
        
        running = false;

        try
        {
            thread.interrupt();
            thread = null;
        }
        catch(Exception e)
        {
            IEC60870App.err("Fehler beim Unterbrechen Thread: " + instance + "." + e, name, 2);
        }

        try {
            if (this.lower != null) {
                this.lower.kill();
            }
        } catch (Exception e) {
            IEC60870App.err("Fehler beim Schließen: " + instance + "." + e, name, 2);
        }

        lower = null;
        upper = null;

        this.empty_Queues();
    }

    /**
     * Kill order received from lower layer. This layer MUST send up
     * this order after stopping itself.
     */
    @Override
    public void lowerKill() {
        if (upper != null) {
            upper.lowerKill();
        } else {
            kill();
        }
    }

    /**
     * Wait this thread to die...
     *
     * @throws Interrupted Exception
     */
    public void join() throws InterruptedException {
        this.thread.join();
    }

    /**
     *
     */
    public void handle_events() throws IEC608705Exception {

        Object obj;
        String msg = null;
        if (!fromLower.isEmpty()) {
            try {
                obj = fromLower.dequeue();
                lowerEvent(obj);
            } catch (QueueException e) {
                msg = "Q Lower: " + e;
            } catch (Exception e) {
                msg = "O Lower: " + e;
            }
        }

        if (!fromUpper.isEmpty()) {
            try {
                obj = fromUpper.dequeue();
                upperEvent(obj);
            } catch (QueueException e) {
                if (msg == null) {
                    msg = "Q Upper: " + e;
                } else {
                    msg = msg + " Q Upper: " + e;
                }
            } catch (Exception e) {
                if (msg == null) {
                    msg = " O Upper: " + e;
                } else {
                    msg = msg + " O Upper: " + e;
                }
            }
        }

        if (msg != null) {
            throw new IEC608705Exception(msg);
        }

    }

    @Override
    public void run() {
        while (running) {
            try {
                {
                    handle_events();
                    Thread.sleep(20);
                }
            } catch (InterruptedException e) {
                //thats normal
            } catch (Exception e) {
                IEC60870App.err("Allgemeiner Fehler während der Abarbeitung in LayerThread in Instanz: " + instance + ". Thread arbeitet weiter. " + e, name, 2);
            }
        }
    }

    @Override
    public void launch() {

        if (lower != null) {
            lower.launch();
        }
        (thread = new Thread(this)).start();
        thread.setPriority(5);
    }

    /**
     * Builds a new LayerThread object.
     *
     * @param name	thread name.
     */
    protected LayerThread(String name) {

       

        this.name = name + " T_id: " + Long.toString(T_id);
        this.running = true;
        this.connected.set(false);
        this.fromLower = new FifoQueue(3000);
        this.fromUpper = new FifoQueue(3000);

    }

    public void connected(boolean conn) {
        this.connected.set(conn);
        if (this.upper != null) {
            ((LayerThread) this.upper).connected(conn);
        }
    }
}
