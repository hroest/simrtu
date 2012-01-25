/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package lib.util;


/**
 * <p>
 * This class defines a priority queue for java.lang.Object where priority has
 * a dual purpose. Indeed, priority ensures a unicity of related object
 * piling up. So, such an object is useful when user have got to define
 * priority according to an object nature.
 * </p>
 *
 * @author lionnel cauvy
 * @version 1.0
 *
 * @date 06.I.03
 */
public class UnicPrioQueue extends PrioQueue {
  private int[] key;

  /**
   * Allocates a new priority queue where priority is also perceived as an
   * object enqueued nature.
   *
   * @param dim required dimension of the queue.
   */
  public UnicPrioQueue(int dim) {
    super(dim);
    key = new int[dim];
  }

  /**
   * Internal method to register the priority. This ensures that an user can't
   * add element with an already registered priority.
   *
   * @param newkey the priority to register
   *
   * @return true if no registration has been made yet on this key, and false
   *         in other case.
   */
  private boolean keep(int newkey) {
    if (isFull()) {
      return false;
    }

    int i;

    for (i = 0; i < getElementCount(); i++) {
      if (newkey == key[i]) {
        return false;
      }
    }

    key[i] = newkey;

    return true;
  }

  /**
   * Adds an object to the priority queue if priority key is not already
   * repertoried.
   *
   * @param key object priority & identifiant.
   *
   * @return number of free areas or -1 if not added. Notice -1 doesn't
   *         necessary mean that queue is full.
   */
  public int enqueue(Object object, int key) {
    if (keep(key)) {
      return super.enqueue(object, key);
    }

    return -1;
  }
}
