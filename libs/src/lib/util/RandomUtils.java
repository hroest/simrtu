/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package lib.util;

import java.util.Random;

/**
 * The class contains static methods for generating random numbers, chars and
 * Strings, without the need for creating a {@link java.util.Random} object.
 *
 * @author <a href="mailto:alexis.bietti@sysaware.com">Alexis BIETTI</a>
 */
public class RandomUtils extends Random {
  static RandomUtils INSTANCE = new RandomUtils();

  /**
   * Private constructor.
   */
  private RandomUtils() {
    super();
  }

  //------------------------------------------------------------------------------
  // Public Methods
  //------------------------------------------------------------------------------

  public static Random random() {
    return INSTANCE;
  }

  /**
   * Generates uppercase characters in the range [A-Y].
   *
   * @return the next <code>char</code> in the pseudo-random suite.
   */
  public static char nextChar() {
    char c;

    do {
      c = (char) INSTANCE.nextInt();
    } while (
      ((c < 'a') || (c > 'z')) && ((c < 'A') || (c > 'Z'))
          && ((c < '0') || (c > '9')) && (c != '='));

    return c;
  }

  //------------------------------------------------------------------------------
  // * Static Methods
  //------------------------------------------------------------------------------

  /**
   * Generates a random String based on a new RandomUtils object.
   *
   * @param length length of the generated String
   *
   * @return a random String
   */
  public static String randomString(int length) {
    char[] buf = new char[length];

    for (int i = 0; i < length; i++) {
      buf[i] = nextChar();
    }

    return new String(buf);
  }

  /**
   * Generates a random byte array with the given length.
   *
   * @param length the length of the byte array
   *
   * @return a random byte array
   */
  public static byte[] randomByteArray(int length) {
    byte[] buf = new byte[length];

    for (int i = 0; i < length; i++) {
      buf[i] = (byte) INSTANCE.nextInt();
    }

    return buf;
  }

  /**
   * Generates an SSL random structure.
   *
   * <p>
   * <code>SSL_RANDOM := SEQUENCE { UNIX_DATE, RANDOM_BYTES[N] }<br>
   * UNIX_DATE := byte[4]<br>
   * RANDOM_BYTES := byte[N] </code>
   * </p>
   *
   * <p>
   * The total length of the SSL_RANDOM is <code>(N + 4)</code>
   * </p>
   *
   * @param bytes number of random bytes after the unix date
   *
   * @return an array of bytes containing an SSL_RANDOM
   */
  public static byte[] sslRandom(int bytes) {
    long   time = System.currentTimeMillis();
    byte[] buf = new byte[bytes + 4];

    ArrayUtils.writeInt(buf, 0, (int) time);

    for (int i = buf.length; --i >= 4;) {
      buf[i] = (byte) INSTANCE.nextInt();
    }

    return buf;
  }
}

