/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package lib.math;

/**
 * <p>
 * Wrapper for floating point numbers. Instances of this class are immutable.
 * </p>
 *
 * @author Alexis BIETTI
 * @version 1.0
 *
 * @see com.ajile.lang.Float
 * @see com.itlity.math.Double
 * @see com.ajile.lang.Double
 */
public final class Float {
  //---------------------------------------------------------------------------
  // Constants
  //---------------------------------------------------------------------------
  public static final float POSITIVE_INFINITY = (1.0F / 0.0F);
  public static final float NEGATIVE_INFINITY = (-1.0F / 0.0F);
  public static final float NaN               = (0.0F / 0.0F);
  public static final float MAX_VALUE         = 3.402823E+038F;
  public static final float MIN_VALUE         = 1.401298E-045F;

  //---------------------------------------------------------------------------
  // Attributes
  //---------------------------------------------------------------------------

  /** Our float value. */
  private float val;

  //---------------------------------------------------------------------------
  // Constructors
  //---------------------------------------------------------------------------

  /**
   * Construct a new Float with the specified float value.
   *
   * @param f a <code>float</code> value
   */
  public Float(float f) {
    val = f;
  }

  /**
   * Construct a new Float with the specified double value.
   *
   * @param d a <code>double</code> value
   */
  public Float(double d) {
    val = (float) d;
  }

  /**
   * <p>
   * Construct a new Float object with the default value <code>0F</code>
   * </p>
   *
   * @see java.lang.Object#Object()
   */
  public Float() {
    this(0f);
  }

  /**
   * Construct a new Float with the specified value, represented in a string.
   *
   * @param s a string representing a float
   *
   * @throws NumberFormatException if s is not a well formed float litteral
   */
  public Float(String s) throws NumberFormatException {
    val = parseFloat(s);
  }

  //---------------------------------------------------------------------------
  // Methods with strings
  //---------------------------------------------------------------------------

  /**
   * <p>
   * Parse the String 's' and return the float value represented by this
   * String.
   * </p>
   *
   * @param s a {@link java.lang.String} representing a <code>float</code>.
   *
   * @return the <code>float</code> number represented in the parameter.
   *
   * @throws NumberFormatException if the {@link java.lang.String} 's' is not
   *         a well formed float litteral.
   */
  public static float parseFloat(String s) {
    return (float) Double.parseDouble(s);
  }

  /**
   * <p>
   * Return a String representation of the float 'f'.
   * </p>
   */
  public static String toString(float f) {
    return Double.toString(f);
  }

  /**
   * Return a String representation of the float value of this Float object.
   */
  public String toString() {
    return Double.toString(this.val);
  }

  //---------------------------------------------------------------------------
  // Methods for comparison
  //---------------------------------------------------------------------------

  /**
   * Return true if the float f is "not a number"
   */
  public static boolean isNaN(float f) {
    return f != f;
  }

  /**
   * Return true if the value of this Float is "not a number"
   */
  public boolean isNaN() {
    return val != val;
  }

  /**
   * Return true if the float f is a negative or positive infinity.
   */
  public static boolean isInfinite(float f) {
    return (f == POSITIVE_INFINITY) || (f == NEGATIVE_INFINITY);
  }

  /**
   * Return true if the the value of this Float is a negative or positive
   * infinity.
   */
  public boolean isInfinite() {
    return (val == POSITIVE_INFINITY) || (val == NEGATIVE_INFINITY);
  }

  //---------------------------------------------------------------------------
  // Methods for casting
  //---------------------------------------------------------------------------

  /**
   * Get the value of this Float cast as a byte.
   *
   * @return a byte
   */
  public byte byteValue() {
    return (byte) (int) val;
  }

  /**
   * Get the value of this Float cast as a short.
   *
   * @return a short
   */
  public short shortValue() {
    return (short) (int) val;
  }

  /**
   * Get the value of this Float cast as an int.
   *
   * @return an int
   */
  public int intValue() {
    return (int) val;
  }

  /**
   * Get the value of this Float cast as a long.
   *
   * @return a long
   */
  public long longValue() {
    return (long) val;
  }

  /**
   * Get the value of this Float cast as a float.
   *
   * @return a float
   */
  public float floatValue() {
    return val;
  }

  /**
   * Get the value of this Float cast as a double.
   *
   * @return a double
   */
  public double doubleValue() {
    return (double) val;
  }

  //---------------------------------------------------------------------------
  // Methods inherited of class Object
  //---------------------------------------------------------------------------

  /**
   * See the general contract of this method in the super class {@link
   * java.lang.Object}
   */
  public int hashCode() {
    return floatToIntBits(val);
  }

  /**
   * See the general contract of this method in the super class {@link
   * java.lang.Object}
   */
  public boolean equals(Object obj) {
    if ((obj != null) && (obj instanceof Float)) {
      return floatToIntBits(((Float) obj).val) == floatToIntBits(val);
    } else {
      return false;
    }
  }

  /**
   * Return the float f as bits in IEEE 754 format.
   */
  public static int floatToIntBits(float f) {
     //modified by Michael Kratz com.ajile.lang.Float.intBitsToFloat replaced by
      //java.lang.Float.intBitsToFloat
      return java.lang.Float.floatToIntBits(f);
  }

  /**
   * Return this float as bits in IEEE 754 format.
   */
  public static float intBitsToFloat(int i) {
      //modified by Michael Kratz com.ajile.lang.Float.intBitsToFloat replaced by
      //java.lang.Float.intBitsToFloat

      return java.lang.Float.intBitsToFloat(i);
  }
}
