/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tui.iec60870.common104;

import org.tui.iec60870.common104.Layers.Transport;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author Micha
 */
public class TimerSvc extends TimerTask {

    Timer timer = new Timer();
    Transport transport;
    long interval;
    int index_timer;

    public TimerSvc(Transport atransport, long ainterval, int aindex_timer)
    {
        transport=atransport;
        interval=ainterval;
        index_timer=aindex_timer;
    }

    public void activate() throws Exception {
        this.resume();
    }

    public synchronized void pause()
    {
        timer.cancel();
        timer.purge();
        timer = null;
        transport = null;
        notify();
    }

    public synchronized void resume() throws Exception
    {
        timer = new Timer();
        timer.schedule(this, interval, interval);
        notify();
    }


    @Override
    public void run()
    {
        if (transport==null)
        {
            timer.cancel();
            return;
        }
        transport.fire_timer(index_timer);
    }
}