/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package lib.util;

import java.io.PrintStream;

/**
 * Utility methods for arrays manipulation
 *
 * @author <a href="mailto:alexis.bietti@sysaware.com"> Alexis BIETTI </a>
 * @version 1.0 - 22 janv. 03
 */
public class ArrayUtils {
  //---------------------------------------------------------------------------
  // Constants
  //---------------------------------------------------------------------------

  /**
   * This byte array has a length of 0, so it can be shared among all users.
   * It avoids instanciation process.
   *
   * <p>
   * You can replace:
   * </p>
   *
   * <p>
   * <blockquote>
   * <code>byte[] b = new byte[0];</code>
   * </blockquote>
   * </p>
   *
   * <p>
   * With:
   * </p>
   *
   * <p>
   * <blockquote>
   * <code>byte[] b = ArrayUtils.BYTE_ARRAY_ZERO_LENGTH; </code>
   * </blockquote>
   * </p>
   *
   * <p>
   * Note: it is unsafe to use this array to synchronize blocks.
   * </p>
   */
  public static final byte[] BYTE_ARRAY_ZERO_LENGTH = new byte[0];

  /**
   * This array of {@link java.lang.Object}s has a length of 0, so it can be
   * shared among all users. It avoids instanciation process.
   *
   * <p>
   * You can replace:
   * </p>
   *
   * <p>
   * <blockquote>
   * <code>Object[] o = new Object[0];</code>
   * </blockquote>
   * </p>
   *
   * <p>
   * With:
   * </p>
   *
   * <p>
   * <blockquote>
   * <code>Object[] b = ArrayUtils.OBJECT_ARRAY_ZERO_LENGTH; </code>
   * </blockquote>
   * </p>
   *
   * <p>
   * Note: it is unsafe to use this array to synchronize blocks.
   * </p>
   */
  public static final Object[] OBJECT_ARRAY_ZERO_LENGTH = new Object[0];

  /**
   * This array of booleans has a length of 0, so it can be shared among all
   * users. It avoids instanciation process.
   *
   * <p>
   * You can replace:
   * </p>
   *
   * <p>
   * <blockquote>
   * <code>boolean[] b = new boolean[0];</code>
   * </blockquote>
   * </p>
   *
   * <p>
   * With:
   * </p>
   *
   * <p>
   * <blockquote>
   * <code>boolean[] b = ArrayUtils.BOOLEAN_ARRAY_ZERO_LENGTH; </code>
   * </blockquote>
   * </p>
   *
   * <p>
   * Note: it is unsafe to use this array to synchronize blocks.
   * </p>
   */
  public static final boolean[] BOOLEAN_ARRAY_ZERO_LENGTH = new boolean[0];

  /**
   * This array of integers has a length of 0, so it can be shared among all
   * users. It avoids instanciation process.
   *
   * <p>
   * You can replace:
   * </p>
   *
   * <p>
   * <blockquote>
   * <code>boolean[] b = new int[0];</code>
   * </blockquote>
   * </p>
   *
   * <p>
   * With:
   * </p>
   *
   * <p>
   * <blockquote>
   * <code>boolean[] b = ArrayUtils.INT_ARRAY_ZERO_LENGTH; </code>
   * </blockquote>
   * </p>
   *
   * <p>
   * Note: it is unsafe to use this array to synchronize blocks.
   * </p>
   */
  public static final int[] INT_ARRAY_ZERO_LENGTH = new int[0];

  /**
   * There is no use to instanciate this class.
   *
   * @see java.lang.Object#Object()
   */
  ArrayUtils() {
    super();
  }

  //---------------------------------------------------------------------------
  // Methods to read data from a byte array
  //---------------------------------------------------------------------------

  /**
   * Read a 64 bit long value from the given array at the specified index
   *
   * @param array
   * @param index
   *
   * @return long
   */
  public static long readLong(byte[] array, int index) {
    long result = 0;

    result = (array[index] << 56) & 0xff00000000000000L;
    result |= ((array[++index] << 48) & 0xff000000000000L);
    result |= ((array[++index] << 40) & 0xff0000000000L);
    result |= ((array[++index] << 32) & 0xff00000000L);
    result |= ((array[++index] << 24) & 0xff000000L);
    result |= ((array[++index] << 16) & 0xff0000L);
    result |= ((array[++index] << 8) & 0xff00L);
    result |= (array[++index] & 0xffL);

    return result;
  }

  /**
   * Read a 32 bit int value from the given array at the specified index.
   *
   * @param array an array of bytes
   * @param index index of the first byte to read
   *
   * @return int    a 32-bit int value
   */
  public static int readInt(byte[] array, int index) {
    int result = 0;

    result = (array[index] << 24) & 0xff000000;
    result |= ((array[++index] << 16) & 0xff0000);
    result |= ((array[++index] << 8) & 0xff00);
    result |= (array[++index] & 0xff);

    return result;
  }

  /**
   * Read a 32 bit int value from the given array at the specified index, in
   * the reverse order.
   *
   * @param array
   * @param off
   *
   * @return int
   */
  public static int readIntInverted(byte[] array, int off) {
    return (array[off] & 0xff) | ((array[++off] << 8) & 0x0000ff00)
    | ((array[++off] << 16) & 0x00ff0000)
    | ((array[++off] << 24) & 0xff000000);
  }

  /**
   * Read a 24 bit int value from the given array at the specified index
   *
   * @param array
   * @param index
   *
   * @return a 24 bit value
   */
  public static int read24bits(byte[] array, int index) {
    int result = 0;

    result = (array[index] << 16) & 0xff0000;
    result |= ((array[++index] << 8) & 0xff00);
    result |= (array[++index] & 0xff);

    return result;
  }

  /**
   * Read two bytes of data and return them as a short value.
   *
   * @param data an array of bytes
   * @param off index of the first byte to read
   */
  public static short readShort(byte[] array, int off) {
    return (short) (((array[off] << 8) & 0xff00) | (array[++off] & 0xff));
  }

  //---------------------------------------------------------------------------
  // Methods to write data to a byte array
  //---------------------------------------------------------------------------

  /**
   * Copy the least significant num bytes of val into byte array b, starting
   * at offset.
   *
   * @param val the value to write
   * @param b an array of bytes
   * @param offset the first index where to write
   * @param num the number of bytes to write
   */
  public static void intToBytes(long val, byte[] b, int offset, int num) {
    int i3 = 0; // running value of (i << 3)

    for (int i = 0; i < num; i++) {
      i3                          = i << 3;
      b[(num + offset) - i - 1]   = (byte) ((val & (0xffL << (i3))) >> (i3));
    }
  }

  /**
   * Write a 32 bit long value in the given array.
   *
   * @param array
   * @param index
   * @param value
   */
  public static void writeLong(byte[] array, int index, long value) {
    array[index]     = (byte) ((value << 56) & 0xff);
    array[++index]   = (byte) ((value << 48) & 0xff);
    array[++index]   = (byte) ((value << 40) & 0xff);
    array[++index]   = (byte) ((value << 32) & 0xff);
    array[++index]   = (byte) ((value << 24) & 0xff);
    array[++index]   = (byte) ((value << 16) & 0xff);
    array[++index]   = (byte) ((value << 8) & 0xff);
    array[++index]   = (byte) (value & 0xff);
  }

  /**
   * Write a 32 bit int value in the given array.
   *
   * @param array
   * @param index
   * @param value
   */
  public static void writeInt(byte[] array, int index, int value) {
    array[index]     = (byte) ((value << 24) & 0xff);
    array[++index]   = (byte) ((value << 16) & 0xff);
    array[++index]   = (byte) ((value << 8) & 0xff);
    array[++index]   = (byte) (value & 0xff);
  }

  /**
   * Write an int in an array of bytes, as four bytes, in the reverse order.
   *
   * @param value an int value
   * @param data an arry of bytes
   * @param off index of the first byte to write
   */
  public static void writeIntInverted(byte[] data, int off, int value) {
    data[off]     = (byte) (value & 0xff);
    data[++off]   = (byte) ((value >> 8) & 0xff);
    data[++off]   = (byte) ((value >> 16) & 0xff);
    data[++off]   = (byte) ((value >> 24) & 0xff);
  }

  /**
   * Write a 24 bit int value in the given array.
   *
   * @param array
   * @param index
   * @param value a 24 bit value
   */
  public static void write24bits(byte[] array, int index, int value) {
    array[index]     = (byte) ((value << 16) & 0xff);
    array[++index]   = (byte) ((value << 8) & 0xff);
    array[++index]   = (byte) (value & 0xff);
  }

  /**
   * Write a short in an array of bytes, as two bytes.
   *
   * @param value a short value
   * @param data an array of bytes
   * @param off index of the first byte to write
   */
  public static void writeShort(byte[] data, int off, short value) {
    data[off]     = (byte) ((value >> 8) & 0xff);
    data[++off]   = (byte) (value & 0xff);
  }

  //----------------------------------------------------------------------------
  // Comparison methods
  //----------------------------------------------------------------------------

  /**
   * <p>
   * Erase the given array. All elements of this array are set to <code>null
   * </code>
   * </p>
   *
   * @param array an array of {@link java.lang.Object}
   */
  public static void erase(Object[] array) {
    for (int i = array.length; --i >= 0;) {
      array[i] = null;
    }
  }

  /**
   * Compare two byte arrays.
   *
   * @param array1 a byte array
   * @param array2 a byte array
   *
   * @return <tt>true</tt> if they're equal, <tt>false</tt> if they're
   *         different or if one of them at least equals <tt>null</tt>
   */
  public static boolean areEqual(byte[] array1, byte[] array2) {
    if ((array1 == null) | (array2 == null)) {
      return false;
    }

    if (array1.length != array2.length) {
      return false;
    }

    for (int i = array1.length - 1; i > -1; i--) {
      if (array1[i] != array2[i]) {
        return false;
      }
    }

    return true;
  }

  //---------------------------------------------------------------------------
  // Output methods
  //---------------------------------------------------------------------------

  /**
   * Print an array of bytes in a user friendly format.
   *
   * @param data an array of bytes.
   * @param off index of the first byte to print.
   * @param len number of bytes to print.
   */
  public static void output(byte[] data, int off, int len, PrintStream out) {
    out.print(" + ");

    for (int i = 0; i < len; i++) {
      if (((i % 16) == 0) && (i != 0)) {
        out.println();
        out.print(" + ");
      }

      String octet = Integer.toString(data[i + off] & 0xff, 16);

      if (octet.length() == 1) {
        out.print('0');
      }

      out.print(octet + ' ');
    }

    out.println();
  }

  /**
   * Print an array of bytes in a user friendly format.
   *
   * @param data an array of bytes.
   * @param off index of the first byte to print.
   * @param len number of bytes to print.
   */
  public static void output(byte[] data, int off, int len) {
    output(data, off, len, System.out);
  }

  /**
   * Print an array of bytes in a user friendly format.
   *
   * @param data an array of bytes.
   */
  public static void output(byte[] data) {
    output(data, 0, data.length);
  }

  //----------------------------------------------------------------------------
  // Copy methods
  //----------------------------------------------------------------------------

  /**
   * Fill the given array with a pattern.
   *
   * @param array a byte array
   * @param off index of the first byte to write
   * @param len number of bytes to write
   * @param pattern value to write in the array
   */
  public static void fillByteArray(
    byte[] array, int off, int len, byte pattern) {
    for (int i = off + len; --i >= off;) {
      array[i] = pattern;
    }
  }

  /**
   * Fill the given array with a pattern.
   *
   * @param array a byte array
   * @param pattern value to write in the array
   */
  public static void fillByteArray(byte[] array, byte pattern) {
    fillByteArray(array, 0, array.length, pattern);
  }

  /**
   * Allocate a new byte array and copy the source's countent into.
   *
   * @return a newly allocated copy of the array <tt> source </tt>.
   */
  public static byte[] copy(byte[] source) {
    if (source == null) {
      return null;
    }

    byte[] ret = new byte[source.length];
    System.arraycopy(source, 0, ret, 0, source.length);

    return ret;
  }

  /**
   * Allocate a new int array and copy the source's countent into.
   *
   * @return a newly allocated copy of the array <tt> source </tt>.
   */
  public static int[] copy(int[] source) {
    if (source == null) {
      return null;
    }

    int[] ret = new int[source.length];
    System.arraycopy(source, 0, ret, 0, source.length);

    return ret;
  }

  /**
   * Concatenate some array of bytes into one array of bytes.
   *
   * @param inputs an array of array of bytes
   *
   * @return the concatenation of all inputs elements
   */
  public static byte[] concat(byte[][] inputs) {
    int total = 0;

    for (int i = 0; i < inputs.length; i++) {
      total += inputs[i].length;
    }

    byte[] ret = new byte[total];
    int    pos = 0;

    for (int i = 0; i < inputs.length; i++) {
      System.arraycopy(inputs[i], 0, ret, pos, inputs[i].length);
      pos += inputs[i].length;
    }

    return ret;
  }

  //----------------------------------------------------------------------------
  // Search methods
  //----------------------------------------------------------------------------

  /**
   * Return the last index where an Object <i>equal to</i><tt>o</tt> appears
   * in <tt>array</tt>. Return -1 if <tt>o</tt> is not found or
   * <tt>null</tt>.
   *
   * @param array an array of Object
   * @param o an Object
   *
   * @return the index of the last occurence of <tt>o</tt> in <tt>array</tt>
   *
   * @see java.lang.Object.equals(Object)
   */
  public static int lastIndexOf(Object[] array, Object o) {
    if ((o == null) || (array == null)) {
      return -1;
    }

    for (int i = array.length; --i >= 0;) {
      if (o.equals(array[i])) {
        return i;
      }
    }

    return -1;
  }

  /**
   * Like <tt>lastIndexOf(Object[] array, Object o)</tt> with <tt>char</tt>
   */
  public static int lastIndexOf(char[] array, char c) {
    if (array == null) {
      return -1;
    }

    for (int i = array.length; --i >= 0;) {
      if (array[i] == c) {
        return i;
      }
    }

    return -1;
  }

  /**
   * Like <tt>lastIndexOf(Object[] array, Object o)</tt> with <tt> int </tt>
   */
  public static int lastIndexOf(int[] array, int value) {
    if (array == null) {
      return -1;
    }

    for (int i = array.length; --i >= 0;) {
      if (array[i] == value) {
        return i;
      }
    }

    return -1;
  }

  /**
   * Test if a byte array contains another byte array.
   */
  public static boolean contains(
    final byte[] container, final byte[] contained) {
    return contains(container, container.length, contained);
  }

  /**
   * Test if a byte array contains another byte array.
   */
  public static boolean contains(
    final byte[] container, final int containerLen, final byte[] contained) {
    // Save often used values
    final int containedLen = contained.length;

    // Check that a parameter is valid
    if ((containerLen < 0) || (containerLen > container.length)) {
      throw new IllegalArgumentException();
    }

    // Check that length are compatible
    if (containerLen < containedLen) {
      return false;
    }

    // Number of matching bytes
    int match = containedLen;
    int index = containerLen;

    while (index > 0) {
      if (container[--index] == contained[--match]) {
        if (match == 0) {
          return true;
        }
      } else {
        match = containedLen;
      }
    }

    return false;
  }

  /**
   * <p>
   * Find with a binary search the index of the byte <code>b</code> in the
   * ordered array <code>array</code>.
   * </p>
   *
   * <p>
   * <code>array</code>  must be ordered in the ascendent order.
   * </p>
   *
   * @param array
   * @param b
   * @param returnAnyway if true, return the last search index even if the
   *        value is  not equal to the searched value.
   *
   * @return int
   */
  public static int binarySearch(byte[] array, byte b, boolean returnAnyway) {
    int first = 0; // First potential index
    int last = array.length; // Last portential index
    int med  = 0;

    do {
      med = (first + last) >> 1;

      if (array[med] == b) {
        return med;
      } else if (array[med] < b) {
        first = med;
      } else {
        last = med;
      }
    } while (first != last);

    // Not found
    return returnAnyway ? med : (-1);
  }

  /**
   * Compute the logical AND of all the boolean values contained in an array.
   *
   * <p>
   * If there is at least one <code>false</code> value in the array, then the
   * value <code>false</code> is returned. Otherwise the value
   * <code>true</code> is returned.
   * </p>
   *
   * @param b an array of boolean values
   *
   * @return the logical AND of all the boolean values contained in an array.
   */
  public static boolean and(boolean[] b) {
    for (int i = b.length; --i >= 0;) {
      if (!b[i]) {
        return false;
      }
    }

    return true;
  }

  /**
   * Compute the logical OR of all the boolean values contained in an array.
   *
   * <p>
   * If there is at least one <code>true</code> value in the array, then the
   * value <code>true</code> is returned. Otherwise the value
   * <code>false</code> is returned.
   * </p>
   *
   * @param b an array of boolean values
   *
   * @return the logical OR of all the boolean values contained in an array.
   */
  public static boolean or(boolean[] b) {
    for (int i = b.length; --i >= 0;) {
      if (b[i]) {
        return true;
      }
    }

    return false;
  }

  
  //copyByteArray
  /*public static void copyByteArray(byte abyte0[], int i, byte abyte1[], int j, int k)
    {
      //kopiert von abyte0 zu abyte1
      //i start byte0
      //j start byte1
      //k länge der zu kopierenden sequenz
      

      //int a=1;
      /*if(i + k > abyte0.length || j + k > abyte1.length || k < 0 || i < 0 || j < 0 || i + k < 0 || j + k < 0)
        {
            throw aiobException;
        } else
        {
            int l = rawJEM.toInt(abyte0);
            l += 12 + i;
            int i1 = rawJEM.toInt(abyte1);
            i1 += 12 + j;
            rawJEM.bblkcpy(l, i1, k);
            return;
        }*/
/*
        if(i + k > abyte0.length || j + k > abyte1.length || k < 0 || i < 0 || j < 0 || i + k < 0 || j + k < 0)
        {
            System.out.println("copyByteArray");

            //@todo aibExecption
            //throw aibException;
        }
        else
        {

        }
  }*/
}
