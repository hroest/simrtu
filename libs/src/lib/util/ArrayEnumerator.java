/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package lib.util;

import java.util.Enumeration;

//-----------------------------------------------------------------------------
// Class
//-----------------------------------------------------------------------------

/**
 * <p>
 * This class has the ability to enumerate arrays in both orders. See the
 * constructors for details.
 * </p>
 *
 * @author <a href="mailto:alexis.bietti@sysaware.com"> Alexis BIETTI </a>
 * @version 1.0
 */
public class ArrayEnumerator implements Enumeration {
  //---------------------------------------------------------------------------
  // Attributes
  //---------------------------------------------------------------------------

  /** The array we enumerate. */
  private Object[] theArray;

  /** Current index. */
  private int theIndex = 0;
  int         off     = -1;
  int         len     = -1;
  boolean     reverse = false;

  //---------------------------------------------------------------------------
  // Constructor
  //---------------------------------------------------------------------------

  /**
   * Standard constructor.
   *
   * @param anArray the array we want to enumerate.
   */
  public ArrayEnumerator(Object[] anArray) {
    this(anArray, 0, anArray.length, false);
  }

  /**
   * Standard constructor.
   *
   * @param anArray the array we want to enumerate.
   * @param reverse whether the enumeration goes upwards or backwards (true if
   *        for backwards)
   */
  public ArrayEnumerator(Object[] anArray, boolean reverse) {
    this(anArray, 0, anArray.length, reverse);
  }

  /**
   * Standard constructor.
   *
   * @param anArray the array we want to enumerate.
   * @param off index of the first element to enumerate
   * @param len number of elements to enumerate
   */
  public ArrayEnumerator(Object[] anArray, int off, int len) {
    this(anArray, off, len, false);
  }

  /**
   * Standard constructor.
   *
   * @param anArray the array we want to enumerate.
   * @param off index of the first element to enumerate
   * @param len number of elements to enumerate
   * @param reverse whether the enumeration goes upwards or backwards (true if
   *        for backwards)
   */
  public ArrayEnumerator(Object[] anArray, int off, int len, boolean reverse) {
    this.theArray   = anArray;
    this.off        = off;
    this.len        = len;
    this.reverse    = reverse;
    reset();
  }

  //---------------------------------------------------------------------------
  // Methods
  //---------------------------------------------------------------------------

  /**
   * See the general contract of this method in the interface <tt>
   * java.util.Enumeration </tt>
   */
  public boolean hasMoreElements() {
    return this.reverse ? (this.theIndex >= 0) : (this.theIndex < this.len);
  }

  /**
   * See the general contract of this method in the interface <tt>
   * java.util.Enumeration </tt>
   */
  public Object nextElement() {
    if (hasMoreElements()) {
      return theArray[this.reverse ? this.theIndex-- : this.theIndex++];
    } else {
      throw new java.util.NoSuchElementException();
    }
  }

  /**
   * Restart this enumerator at the beginning of the array.
   */
  public void reset() {
    this.theIndex = this.reverse ? ((this.off + this.len) - 1) : 0;
  }

  //---------------------------------------------------------------------------
  // Testing
  //---------------------------------------------------------------------------

  /**
   * Test
   */
  public static void main(String[] args) {
    int       len   = 10;
    Integer[] array = new Integer[len];

    for (int i = 0; i < len; ++i) {
      array[i] = new Integer(i);
    }

    // for (Enumeration e = new ArrayEnumerator(array) ; e.hasMoreElements() ;)
    //   System.out.println(e.nextElement());
    for (Enumeration e = new ArrayEnumerator(array, true);
          e.hasMoreElements();) {
      System.out.println(e.nextElement());
    }
  }
}
