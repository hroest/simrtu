/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package lib.util;

import java.util.Enumeration;

/**
 * <p>
 * <tt> Log </tt> class allow Objects to be buffered momentarily, without
 * exceeding a predefined size.
 * <p>
 * If a buffer is full and an Object is to be stored into, then the oldest
 * element is lost, and replaced by the newest element.
 * <p>
 * This class supports the storing of <tt> null </tt> references.
 *
 * @author <a href="mailto:alexis.bietti@sysaware.com">Alexis BIETTI
 *         &lt;alexis.bietti@sysaware.com&gt;</a>
 */
public class CircularBuffer {
  //---------------------------------------------------------------------------
  // Constants
  //---------------------------------------------------------------------------

  /** Default buffer size. */
  public static final int DEFAULT_SIZE = 10;

  /** Maximum buffer size. */
  public static final int MAX_SIZE = 256;

  /** Minimum buffer size. */
  public static final int MIN_SIZE = 1;

  /** This object represents the <tt> null </tt> value. */
  private static final Object NULL = new Object();

  //---------------------------------------------------------------------------
  // Attributes
  //---------------------------------------------------------------------------

  /** Array of elements. */
  private Object[] elements = null;

  /** Index of the most recent element. -1 means that the buffer is empty. */
  private int index = -1;

  //---------------------------------------------------------------------------
  // Constructors
  //---------------------------------------------------------------------------

  /**
   * Construct a new <tt>CircularBuffer</tt> with a default size and no class
   * constraint.
   */
  public CircularBuffer() {
    this(DEFAULT_SIZE);
  }

  /**
   * <p>
   * Construct a new <tt> CircularBuffer </tt> with the specified size.
   * <p>
   * <b> Precondition </b> : <tt> MIN_SIZE <= size <= MAX_SIZE </tt>
   *
   * @param size   a size for this <tt> CircularBuffer </tt>.
   *
   * @see          #MIN_SIZE
   * @see          #MAX_SIZE
   *
   * @throws       IllegalArgumentException if the size is illegal.
   */
  public CircularBuffer(int size) throws IllegalArgumentException {
    if ((size < MIN_SIZE) || (size > MAX_SIZE)) {
      throw new IllegalArgumentException();
    }

    elements = new Object[size];
  }

  //---------------------------------------------------------------------------
  // Methods
  //---------------------------------------------------------------------------

  /**
   * Add an element in this buffer. If the buffer is full, the oldest element is
   * discarded.
   *
   * @param o  an object to store in this buffer.
   */
  public synchronized void addElement(Object o) {
    if (o == null) {
      o = NULL;
    }

    ++index;
    index %= elements.length;
    elements[index] = o;
  }

  /**
   * Clear this buffer so that it contains zero element.
   */
  public synchronized void clear() {
    ArrayUtils.erase(elements);
    index = -1;
  }

  /**
   * Get the most recent element in this buffer, if any.
   *
   * @return  the most recent element, or <tt>null</tt> if this buffer is empty.
   */
  public synchronized Object lastElement() {
    // Are we empty ?
    if (index == -1) {
      return null;
    }

    // Special support for 'null' entries that are NOT empty entries !
    if (elements[index] == NULL) {
      return null;
    }

    return elements[index];
  }

  /**
   * Return the number of elements this buffer can contain.
   *
   * @return the capacity of this buffer.
   */
  public int capacity() {
    return elements.length;
  }

  /**
   * Return the number of elements this buffer contains.
   *
   * @return the number of elements this buffer contains.
   */
  public synchronized int countElements() {
    int count = 0; // counter

    for (int i = elements.length - 1; i >= 0; --i) {
      // NULL references != null references !
      if (elements[i] != null) {
        ++count;
      }
    }

    return count;
  }

  /**
   * Return an array containing all elements in this buffer.
   * <p>
   * It is safe to modify the returned array.
   * <p>
   * If the buffer contains zero element, this method return an
   * array whose size is zero.
   *
   * @return a newly allocated array containing all elements in this buffer.
   */
  private synchronized Object[] getAllElementsAsArray() {
    int      count    = countElements(); // number of elements
    int      srcIndex = index; // current source index
    Object[] ans      = new Object[count];

    for (int i = count - 1; i >= 0; --i) {
      ans[i] = elements[srcIndex--];

      if (srcIndex < 0) {
        srcIndex = elements.length - 1;
      }

      if (ans[i] == NULL) {
        ans[i] = null;
      }

      if (srcIndex < 0) {
        srcIndex = elements.length;
      }
    }

    return ans;
  }

  /**
   * Return an enumeration of all objects stored in this log.
   */
  public Enumeration elements() {
    return new ArrayEnumerator(getAllElementsAsArray());
  }

  /**
   * Return an enumeration of all objects stored in this log.
   *
   * @param reverse whether we enumerate backwards or upwards (true is
   *        backwards)
   */
  public Enumeration elements(boolean reverse) {
    return new ArrayEnumerator(getAllElementsAsArray(), reverse);
  }

  /**
   * Return a <tt>String</tt> representation of this buffer.
   *
   * @return a <tt>String</tt> representation of this buffer.
   */
  public synchronized String toString() {
    StringBuffer buf = new StringBuffer("{ ");

    for (Enumeration e = elements(); e.hasMoreElements();) {
      buf.append(e.nextElement());
      buf.append(' ');
    }

    buf.append('}');

    return buf.toString();
  }

  //---------------------------------------------------------------------------
  // Testing
  //---------------------------------------------------------------------------

  /**
   * TEST
   */
  public static void main(String[] args) {
    CircularBuffer buf = new CircularBuffer(10);

    for (int i = 0; i < 50; ++i) {
      buf.addElement(new Integer(i));
      System.out.println(buf);
    }
  }
}
