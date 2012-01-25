/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rt_database;

/**
 * All commands must implement this.
 *
 * @author Alexis BIETTI
 * @version 1.0
 */
public interface Command {
  public static final String XML_SBOID = "sbo";

  /**
   * If this command is monitored by a {@link
   * com.itlity.luciol.database.SelectBeforeOperate}, then this method must
   * return the database ID of this {@link
   * com.itlity.luciol.database.SelectBeforeOperate}. If not, it must
   * return <tt> -1 </tt>.
   *
   * @return an integer
   */
  public int getSelectBeforeOperateID();
}
