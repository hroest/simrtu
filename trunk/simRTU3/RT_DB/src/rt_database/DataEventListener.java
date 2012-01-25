/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rt_database;

/**
 * Applications that want to be notified of data events must implement this
 * interface.
 *
 * @author <a href="mailto:alexis.bietti@sysaware.com">Alexis BIETTI</a>
 */
public interface DataEventListener {
  //---------------------------------------------------------------------------
  // Constants
  //---------------------------------------------------------------------------

  /*
 * These constants describe what kind of event raised this event.
 */
  public static final int VALUE_CHANGED           = 0x1;
  public static final int VALUE_MIN_CROSSED       = 0x2;
  public static final int VALUE_MAX_CROSSED       = 0x4;
  public static final int VALUE_DEAD_BAND_CROSSED = 0x8;

  //---------------------------------------------------------------------------
  // Methods
  //---------------------------------------------------------------------------

  /**
   * This method is called when a data event is raised by the database
   * manager.
   *
   * @param source the dataElement that raised the event.
   */
  public void dataEventPerformed(DataElement source);
}
