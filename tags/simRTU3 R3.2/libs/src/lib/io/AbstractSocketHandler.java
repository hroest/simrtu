/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package lib.io;

/**
 * Classes that implement this interface handle StreamConnection objects. It
 * is not required that they close these connections because it is done in
 * the SocketServer class.
 *
 * <p>
 * This interface also defines a way to inform implementers that a network
 * exception broke a connection. So input &amp; output buffers in use
 * according to a faulty socket must be discarded.
 * </p>
 *
 * @author Alexis BIETTI
 * @author lionnel cauvy
 */
public interface AbstractSocketHandler {
  /**
   * Handle a stream connection:
   *
   * <ul>
   * <li>
   * Read the request
   * </li>
   * <li>
   * Compute a send a response to the recipient(s)
   * </li>
   * </ul>
   *
   * <p>
   * The implementation may allow or not several sockets to be open at a same
   * time.
   *
   * @param socket a stream connection
   */
  public void handle(StreamConnection socket);

  public String get_own_uri();
  public String get_foreign_uri();
  public boolean valid_uri(String uri);

}
