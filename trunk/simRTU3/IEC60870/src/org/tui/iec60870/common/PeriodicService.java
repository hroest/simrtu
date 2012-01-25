/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tui.iec60870.common;

import java.util.Timer;
import java.util.TimerTask;

/**
 * <p>
 * A Periodic Service use the aJile CallbackTimer avoiding to use the
 * aJile Periodic Timer that creates a supplementary Thread.<br>
 * At each event triggered, callback is relaunched. Notice that time
 * passed as argument is RELATICE and not absolute as in CallbackTimer class.
 *
 * @author lionnel cauvy
 * @version 1.0
 * @date 29.XII.02
 */
public class PeriodicService extends TimerTask
{
	
        Timer timer=new Timer();

        /**
	 * Restart flag. Avoids incontrolled restart
	 * when user pause a periodic task inside
	 * trigger method body.
	 */
	private boolean restart = true;
  
  /**
   * Service.
   */
  private AppService svc;

  /**
   * Absolute time.
   */
  private int time;
  /**
   * The object to periodically trigger.
   */
  private PeriodicEventListener handler;

  /**
   * This call disables the periodic trigger.
   */
  public void pause()
  {
    timer.cancel();
    timer.purge();
    restart = false;
  }

  /**
   * Enables the periodic trigger.
   */
  public void resume()
  {
      timer=new Timer();
      timer.schedule(this, 1000, time);
  }

  /**
   * INTERNAL method bound to CallbackTimer.
   */

  @Override
    public void run() {

        if (handler !=null)
        {
            handler.triggerEvent(svc);
            if(!restart)
            timer.cancel();
        }
    }

  /**
   * Instanciates a new periodic task.
   *
   * @param time	RELATIVE time in ms.
   * @param id		task identifiant.
   * @param handler	the object to trigger.
   */
  public PeriodicService(int time, AppService svc, PeriodicEventListener handler)
  {
    this.svc = svc;
    this.handler = handler;
    this.time = time;
    timer = new Timer();
  }
}
