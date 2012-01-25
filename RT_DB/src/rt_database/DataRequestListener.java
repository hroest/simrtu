/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rt_database;

/**
 * <p>
 * Applications that have the ability to refresh a <tt>DataElement</tt> must
 * implement this interface.
 * </p>
 *
 * <p>
 * When an other application requests for a refreshed data, the
 * <tt>DataRequestListener</tt> is called.
 * </p>
 *
 * <p>
 * It is NOT REQUIRED that the <tt>DataRequestListener</tt> actually refresh
 * the <tt>DataElement</tt>.
 * </p>
 *
 * @author <a href="mailto:alexis.bietti@sysaware.com">Alexis BIETTI</a>
 */
public interface DataRequestListener {
  /**
   * <p>
   * This method is called whenever the method <tt> requestRefreshedRecord()
   * </tt> is called for a <tt> DataElement </tt> that has a <tt>
   * DataRequestListener </tt>
   * </p>
   *
   * <p>
   * The <tt> DataRequestListener </tt> should read the actual value of the
   * <tt> DataElement </tt> and write a new <tt> Record </tt> in its history
   * if necessary, if at least one of the following conditions occurs :
   *
   * <ul>
   * <li>
   * The value has changed
   * </li>
   * <li>
   * The quality has changed (the data became valid, or became invalid)
   * </li>
   * <li>
   * The last <tt>Record</tt> is too old
   * </li>
   * </ul>
   * </p>
   *
   * @param request the <tt> DataElement </tt> that is requested to refresh.
   */
  public void dataRequestPerformed(DataElement request);
}
