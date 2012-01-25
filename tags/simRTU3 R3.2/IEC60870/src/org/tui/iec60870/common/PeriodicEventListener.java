/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tui.iec60870.common;

/**
 * <p>
 * This interface enables objects to use a periodic task.
 *
 * @author lionnel cauvy
 * @version 1.0
 * @date 29.XII.02
 */
public interface PeriodicEventListener
{
  /**
   * This event is raised by a periodic task.
   *
   * @param svc		The application service recorded as periodic procedure.
   */
  public abstract void triggerEvent(AppService svc);
}
