/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rt_database;

/**
 * A DatabaseException is thrown when a database operation can not be
 * performed.
 *
 * @author <a href="mailto:alexis.bietti@sysaware.com">Alexis BIETTI</a>
 */
public class DatabaseException extends RuntimeException {
  /**
   * Create a new DatabaseException with no message.
   */
  public DatabaseException() {
  }

  /**
   * Create a new DatabaseException the the given message.
   *
   * @param message a String that explains the reasons of this exception.
   */
  public DatabaseException(String message) {
    super(message);
  }
}
