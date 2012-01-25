/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package lib.math;

/**
 * <p>
 * Frozen version of the MathFP class. Date: 17-sep-2002
 *
 * <p>
 * This class implements 32 bit fixed-point integer math functions.
 * The intend of this class is to provide float like calculations for the
 * KVM CLDC configuration. However this class can be used where ever the long
 * data type is supported.<br>
 * Fixed point integers are integer numbers with an imaginary fixed decimal
 * point. The number consist of a decimal part and a fractional part. The size
 * of the fractional part expressed in the number of bits determines the
 * precision. MathFP Version II uses the long data type to implement
 * fixed-point integers. The main difference and possible advantage between
 * floats and fixed-point integers is that the fixed-point integer domain is
 * equaly spaced whereas a float increases the space between numbers when the
 * exponent raises.
 * <p>
 * This implementation includes variable precision and allows the developer to
 * change the precision during run-time anywhere between 24 and 3 bits for the
 * fraction. Transparent scaling is added where possible to increase the range
 * of the fixed-point integers. However caution should always be taken for
 * numbers close to MAX_VALUE.
 * <br><br>
 *
 * Example:<br><br>
 * <code>
 * int n = MathFP.toFP(12);<br>
 * int m = MathFP.toFP("14.5");<br>
 * int a = n + m;<br>
 *     a = a - MathFP.toFP("1.5");<br>
 *
 *     a = MathFP.div(a,n);<br>
 *     a = MathFP.sqrt(a);<br>
 * System.out.println(MathFP.toString(a));<br>
 * </code>
 * <br>
 * @author Onno Hommes
 * @version 2.0.5
 */
public class Math32FP {
  //=============================================================================
  // Utility constants
  //=============================================================================
  private static final int   _fbits  = 12;
  private static final int   _digits = 4;
  private static final int   _fmask  = 4095;
  private static final int   _dmul   = 10000;
  private static final int   _flt    = 0;
  private static final int   _pi;
  private static final int[] e;

  /**
   * The fixed point value of the natural unity "1".
   */
  public static final int ONE;

  //=============================================================================
  // Numerical constants
  //=============================================================================

  /**
   * The fixed-point representation of pi (3.14159265)
   */
  public static final int PI;

  /**
   * The fixed-point representation of the natural number (2.71828183).
   */
  public static final int E;

  /**
   * The maximum fixed-point representation for the current precision.
   */
  public static final int MAX_VALUE = 0x7fffffff;

  /**
   * The minimum fixed-point representation for the current precision.
   */
  public static final int MIN_VALUE = 0x80000001;

  /**
   * Min value for casting to/from float.
   */
  public static final float FLOAT_MAX = ((float) MAX_VALUE) * _fmask;

  /**
   * Max value for casting to/from float.
   */
  public static final float FLOAT_MIN = ((float) MIN_VALUE) * _fmask;

  //=============================================================================
  // Static Initializer
  //=============================================================================
  static {
    ONE   = 4096;
    _pi   = 12868;
    e     = (new int[] { ONE, 11134, 30266, 0x1415e, 0x36994 });
    PI   = _pi;
    E    = e[1];
  }

  //=============================================================================
  // Constructor
  //=============================================================================

  /**
   *
   */
  final int value;

  /**
   * <p>
   * Construct a new <tt> Math32FP </tt> object with the given fixed point
   * value.
   * <p>
   * <tt> Math32FP </tt> objects are immutable.
   *
   * @param fpValue   a fixed point value.
   */
  public Math32FP(int fpValue) {
    value = fpValue;
  }

  public Math32FP(float fpValue)
  {
    value = toFP(fpValue);
  }

  //=============================================================================
  // Instance methods
  //=============================================================================

  /**
   * This method returns the fixed point value of this Math32FP object.
   *
   * @return   a fixed point value.
   */
  public int fpValue() {
    return value;
  }

  /**
   * DOCUMENT ME!
   *
   * @return DOCUMENT ME!
   */
  public int intValue() {
    return toInt(value);
  }

  /**
   * DOCUMENT ME!
   *
   * @return DOCUMENT ME!
   */
  public long longValue() {
    return (long) toInt(value);
  }

  /**
   * DOCUMENT ME!
   *
   * @return DOCUMENT ME!
   */
  public float floatValue() {
    return toFloat(value);
  }

  /**
   * DOCUMENT ME!
   *
   * @return DOCUMENT ME!
   */
  public double doubleValue() {
    return (double) toFloat(value);
  }

  /**
   * DOCUMENT ME!
   *
   * @return DOCUMENT ME!
   */
  public short shortValue() {
    return (short) toInt(value);
  }

  /**
   * DOCUMENT ME!
   *
   * @return DOCUMENT ME!
   */
  public byte byteValue() {
    return (byte) toInt(value);
  }

  /**
   * DOCUMENT ME!
   *
   * @return DOCUMENT ME!
   */
  public boolean booleanValue() {
    return (value != 0);
  }

  /**
   * Return a String representation of this Math32FP object.
   */
  public String toString() {
    return toString(value);
  }

  /**
   * Return a hash code for this object.
   */
  public int hashCode() {
    return value;
  }

  /**
   * Return true if o is a Math32FP object with the same value.
   */
  public boolean equals(Object o) {
    if (o instanceof Math32FP) {
      return this.value == ((Math32FP) o).value;
    }

    return false;
  }

  //=============================================================================
  // Utility methods
  //=============================================================================

  /**
   * Get the current runtime precision for MathFP.
   * <p>
   * The current precision is expressed in  the number of bits used for the
   * fractional part.
   *
   * @return the size of the fraction expressed in the number of bits.
   * @since KVM CLDC 1.0
   */
  public static int getPrecision() {
    return _fbits;
  }

  //=============================================================================
  // Conversion methods
  //=============================================================================

  /**
   * Converts a fixed-point integer to an int.
   * <p>
   * This method will round the fixed point integer before converting it to a
   * normal int. To avoid rounding you can shift the value precision bits to
   * the right.
   *
   * @param i   the fixed-point integer to be converted.
   * @return    an int in a normal integer representation.
   * @since     KVM CLDC 1.0
   */
  public static int toInt(int i) {
    i = round(i, 0);

    return i >> _fbits;
  }

  /**
   *
   */
  public static float toFloat(int fpValue) {
    return ((float) fpValue) / _fmask;
  }

  /**
   * rounds a fixed-point integer.
   * <p>
   * This method will round the fixed point integer x to a precision of d
   * digits. The maximum number of digits to round to is 8 but can be lower
   * depending on the current runtime precision. The minimum is 0. Any other
   * number will use the default precision digits which depends on the runtime
   * precision.
   *
   * @param l the fixed-point integer to be rounded
   * @param i the number of precision digits to round to.
   * @return a  rounded fixed-point integer.
   * @since KVM CLDC 1.0
   */
  public static int round(int l, int i) {
    int l1 = 10;

    for (int j = 0; j < i; j++) {
      l1 *= 10;
    }

    l1 = div(toFP(5), toFP(l1));

    if (l < 0) {
      l1 = -l1;
    }

    return l + l1;
  }

  /**
   * Converts a normal int to a fixed-point integer.
   * <p>
   * The input long will be shifted the number of fraction bits to
   * the left.
   *
   * @param i    the long to be converted to a fixed-point integer
   * @return     a fixed-point integer with runtime precision bits fraction.
   * @since      KVM CLDC 1.0
   */
  public static int toFP(int i) {
    return i << _fbits;
  }

  /**
   * Convert an FP value into a float.
   *
   * @see #FLOAT_MIN
   * @see #FLOAT_MAX
   */
  public static int toFP(float f) {
    f *= 4095;

    return (int) f;
  }

  /**
   * Converts an IEEE float to a fixed-point integer.
   *
   * @param floatBits  an IEEE 32 bit float value
   * @return           a fixed-point integer.
   * @since            KVM CLDC 1.0
   */
  public static int fromFloatBitsToFP(int floatBits) {
    // Is the value negative ?
    boolean negative = (floatBits & 0x80000000) != 0;

    // Unshifted exponent
    int exponent = ((floatBits & 0x7f800000) >> 23) - 127;

    // Mantissa, used for the final result
    int mantissa = (floatBits & 0x007fffff) | 0x00800000;

    /*
    System.out.println("neg: " + negative);
    System.out.println("exp: " + exponent);
    System.out.println("mantissa: 0x" + Integer.toHexString(mantissa));
    */
    mantissa >>= (23 - _fbits - exponent);

    // Invert sign if applicable.
    if (negative) {
      return -mantissa;
    } else {
      return mantissa;
    }
  }

  /**
   * Converts a fixed-point integer to an IEEE float.
   *
   * @param fpValue    a fixed-point integer.
   * @return           an IEEE 32 bit float value
   * @since            KVM CLDC 1.0
   */
  public static int toFloatBits(int fpValue) {
    throw new RuntimeException("Not implemented");
  }

  /**
   * Converts a string input to a fixed-point integer.
   * <p>
   * This function will take the string and convert it to a fixed-point integer.
   * If you specify more than the number of decimal digits as
   * returned from setPrecision(...) then the additional decimal digits will be
   * ignored. Invalid strings will result in a NumberFormatException
   * (e.g. 1.ac23); Valid numbers are .5, 0.2, 20.1234, etc...<br>
   * Numbers close to a natural number will sometimes be rounded to the natural
   * number if that natural number is the closest representation in a
   * fixed-point integer format For example 0.99999999 will become 1.
   *
   * @param s   the String to be converted to a fixed-point integer.
   * @return    a fixed-point integer with precision bits fraction.
   * @throws    NumberFormatException if the input is invalid.
   * @since     KVM CLDC 1.0
   */
  public static int toFP(String s) {
    int i = 0;

    if (s.charAt(0) == '-') {
      i = 1;
    }

    String s1 = "-1";
    int    j = s.indexOf('.');

    if (j >= 0) {
      for (
        s1 = s.substring(j + 1, s.length()); s1.length() < _digits;
            s1 = s1 + "0") {
        ;
      }

      if (s1.length() > _digits) {
        s1 = s1.substring(0, _digits);
      }
    }
    else {
      j = s.length();
    }

    int k = 0;

    if (i != j) {
      k = Integer.parseInt(s.substring(i, j));
    }

    int l  = Integer.parseInt(s1) + 1;
    int i1 = (k << _fbits) + ((l << _fbits) / _dmul);

    if (i == 1) {
      i1 = -i1;
    }

    return i1;
  }

  /**
   * Converts a fixed-point integer to a string.
   * <p>
   * This method allows you to display a fixed-point integer in a more
   * user friendly way. The number of digits behind the decimal point will be
   * the number returned by setPrecision(...).
   *
   * @param i    the fixed-point integer to be converted.
   * @return     a string representing the fixed-point integer value.
   * @since      KVM CLDC 1.0
   */
  public static String toString(int i) {
    boolean flag = false;

    if (i < 0) {
      flag   = true;
      i      = -i;
    }

    int    j = i >> _fbits;
    int    k = (_dmul * (i & _fmask)) >> _fbits;
    String s;

    for (s = Integer.toString(k); s.length() < _digits; s = "0" + s) {
      ;
    }

    return (flag ? "-" : "") + Integer.toString(j) + "." + s;
  }

  /**
   * Converts a fixed-point integer to a string with rounding.
   * <p>
   * This method allows you to display a fixed-point integer in a more
   * user friendly. In addition one can specify the number of digits after
   * the decimal point. Possible values are 0 through 8 depending on the
   * current runtime precision. Any other number will will have the default
   * number of digits based on the runtime precision. This method uses the
   * normal round. E.g. 1.05 displayed with 1 digit of the fraction becomes 1.1
   *
   * @param i the fixed-point integer to be converted.
   * @param j the number of digits to show of the fraction.
   * @return a string representing the rounded fixed-point integer.
   * @since KVM CLDC 1.0
   */
  public static String toString(int i, int j) {
    if (j > _digits) {
      j = _digits;
    }

    String s = toString(round(i, j));

    return s.substring(0, (s.length() - _digits) + j);
  }

  //=============================================================================
  // Arithmetic methods
  //=============================================================================

  /**
   * Returns the biggest number of the two fixed-point integers.
   * <p>
   * This method returns the biggest of the two numbers.
   *
   * @param i    a fixed-point integer.
   * @param j    a fixed-point integer.
   * @return     the maximum of the two fixed-point integers.
   * @since      KVM CLDC 1.0
   */
  public static int max(int i, int j) {
    return (i >= j) ? i : j;
  }

  /**
   * Returns the smallest number of the two fixed-point integers.
   * <p>
   * This method returns the smallest of the two numbers.
   *
   * @param i   a fixed-point integer.
   * @param j   a fixed-point integer.
   * @return    the minimum of the two fixed-point integers.
   * @since     KVM CLDC 1.0
   */
  public static int min(int i, int j) {
    return (j >= i) ? i : j;
  }

  /**
   * Multiplies two fixed-point integers.
   * <p>
   * The method will multiply the fixed-point i with the fixed-point j.
   * Overflows and underflows are not always flaged so be carefull when
   * operating near boudry areas. Remember these numbers are fixed points so
   * they are internally bigger then you might perceive.
   *
   * @param i    the fixed-point integer to be multiplied.
   * @param j    the fixed-point integer multiplier
   * @return     a new fixed point integer representing i multiplied by j.
   * @throws     ArithmeticException if the function overflows.
   * @since      KVM CLDC 1.0
   */
  public static int mul(int i, int j) {
    boolean flag = false;
    int     k = _fbits;
    int     l = _fmask;

    if ((i & l) == 0) {
      return (i >> k) * j;
    }

    if ((j & l) == 0) {
      return i * (j >> k);
    }

    if (((i < 0) && (j > 0)) || ((i > 0) && (j < 0))) {
      flag = true;
    }

    if (i < 0) {
      i = -i;
    }

    if (j < 0) {
      j = -j;
    }

    for (; max(i, j) >= (1 << (31 - k)); k--) {
      i >>= 1;
      j >>= 1;
      l >>= 1;
    }

    int i1 = ((i >> k) * (j >> k)) << k;
    int j1 = ((i & l) * (j & l)) >> k;
    j1 += (((i & ~l) * (j & l)) >> k);
    i1 = (i1 + j1 + (((i & l) * (j & ~l)) >> k)) << (_fbits - k);

    if (i1 < 0) {
      throw new ArithmeticException("Overflow");
    }
    else {
      return flag ? (-i1) : i1;
    }
  }

  /**
   * Divides two fixed-point integers.
   * <p>
   * This method will divide the fixed point i by the fixed point j.
   * Overflows and underflows are not flaged so be carefull when operating near
   * boundry areas.
   * This method includes floating point behavior also referred to as scaling.
   * This scaling is transparent to the developer and increases the range of
   * the division method at the cost off precision.
   *
   * @param i the dividend fixed-point integer.
   * @param j the divider fixed-point integer.
   * @return a new fixed point integer representing i divided by j.
   * @since KVM CLDC 1.0
   */
  public static int div(int i, int j) {
    boolean flag = false;
    int     k = _fbits;

    if (j == ONE) {
      return i;
    }

    if ((j & _fmask) == 0) {
      return i / (j >> k);
    }

    if (((i < 0) && (j > 0)) || ((i > 0) && (j < 0))) {
      flag = true;
    }

    if (i < 0) {
      i = -i;
    }

    if (j < 0) {
      j = -j;
    }

    for (; max(i, j) >= (1 << (31 - k)); k--) {
      i >>= 1;
      j >>= 1;
    }

    int l = ((i << k) / j) << (_fbits - k);

    return flag ? (-l) : l;
  }

  /**
   * Adds two fixed-point integers.
   * <p>
   * This method is provided for consistency only. Since the inputs are
   * fixed-point integers you might as well use the + operator.<br>
   * MathFP.add(toFP(1),toFP(2)) equals toFP(1) + toFP(2).
   *
   * @param i   the fixed-point integer to add to.
   * @param j   the fixed-point integer to be added.
   * @return    a new fixed point integer representing the addition of i and j.
   * @since     KVM CLDC 1.0
   */
  public static int add(int i, int j) {
    return i + j;
  }

  /**
   * Subtracts two fixed-point integers.
   * <p>
   * This method is provided for consistency only. Since the inputs are
   * fixed-point integers you might as well use the - operator.<br>
   * MathFP.sub(toFP(2),toFP("1.5")) equals toFP(1) - toFP("1.5").
   *
   * @param i    the fixed-point integer to substract to.
   * @param j    the fixed-point integer to be substracted.
   * @return     a new fixed point integer representing the substraction of i
   *             and j.
   * @since      KVM CLDC 1.0
   */
  public static int sub(int i, int j) {
    return i - j;
  }

  /**
   * Returns the absolute value of the fixed-point integer.
   * <p>
   * If the fixed-point integer is smaller than 0 then it returns -i
   * else it will return i.
   *
   * @param i   the fixed-point integer to get the absolute value of.
   * @return    a new fixed point integer representing the absolute value of n.
   * @since     KVM CLDC 1.0
   */
  public static int abs(int i) {
    if (i < 0) {
      return -i;
    }
    else {
      return i;
    }
  }

  /**
   * Returns the square root of the the fixed-point integer.
   * <p>
   * This function calculates the square root of a fixed-point integer.
   * This method is extreemly fast and is the shortest sqrt function available.
   * This method allows the developer to specify the number of iterations (j)
   * to use to extract the root from n to allow performance control. This is
   * usefull if many sqrt calls have to be made. The higher the number the more
   * precise the answer. Typical values for r range from 6 to 24 depending on
   * the runtime precision. Higher numbers will not give you any better
   * results.
   * <p>
   * This method uses the Newton method to extract the root.
   *
   * @param i    the fixed-point integer to extract the root from.
   * @param j    an integer that specifies the number of iterations.
   * @return     the fixed-point integer sqrt of i.
   * @throws     ArithmeticException for negative inputs and overflows.
   * @since      KVM CLDC 1.0
   */
  public static int sqrt(int i, int j) {
    if (i < 0) {
      throw new ArithmeticException("Bad Input");
    }

    if (i == 0) {
      return 0;
    }

    int k = (i + ONE) >> 1;

    for (int l = 0; l < j; l++) {
      k = (k + div(i, k)) >> 1;
    }

    if (k < 0) {
      throw new ArithmeticException("Overflow");
    }
    else {
      return k;
    }
  }

  /**
   * Returns the square root of the the fixed-point integer.
   * <p>
   * Internally this method uses the sqrt(i,j) with 24 iterations.
   * Auto adjustment is not provided yet. All limitations of sqrt(i,j) apply.
   *
   * @param i    the fixed-point integer to extract the root from.
   * @return     the fixed-point integer sqrt of i.
   * @throws     ArithmeticException for negative inputs and overflows.
   * @since      KVM CLDC 1.0
   */
  public static int sqrt(int i) {
    return sqrt(i, 16);
  }

  //=============================================================================
  // Trigonometric methods
  //=============================================================================

  /**
   * Returns the sine of the radian fixed-point integer.
   * <p>
   * Returns sine for a given radian. The input must be a fixed-point integer
   * radian. The return value is ofcourse a fixed-point integer again.
   * This sine function calculates the sine using a polynomial function.
   *
   * @param i    the radian fixed-point integer to get the sine value of.
   * @return     the sine for the radian n as a fixed-point integer.
   * @since      KVM CLDC 1.0
   */
  public static int sin(int i) {
    int j = mul(i, div(toFP(180), PI));
    j %= toFP(360);

    if (j < 0) {
      j = toFP(360) + j;
    }

    int k = j;

    if ((j >= toFP(90)) && (j < toFP(270))) {
      k = toFP(180) - j;
    }
    else if ((j >= toFP(270)) && (j < toFP(360))) {
      k = -(toFP(360) - j);
    }

    int l  = k / 90;
    int i1 = mul(l, l);
    int j1 =
      mul(
        mul(
          mul(mul(-18 >> _flt, i1) + (326 >> _flt), i1) - (2646 >> _flt), i1)
        + (6434 >> _flt), l);

    return j1;
  }

  /**
   * Returns the arc sine of the fixed-point integer.
   * <p>
   * This method is the reverse of the sin function and uses a polynomial
   * function to calculate the result.
   * The function will only accept values between and equal to -1 and 1.
   *
   * @param i a fixed-point integer to get the arc sine of.
   * @return the arc sine of the fixed-point integer s.
   * @throws ArithmeticException if the input is < -1 or > 1.
   * @since KVM CLDC 1.0
   */
  public static int asin(int i) {
    if (abs(i) > ONE) {
      throw new ArithmeticException("Bad Input");
    }

    boolean flag = i < 0;

    if (i < 0) {
      i = -i;
    }

    int j =
      mul(
        mul(mul(mul(35 >> _flt, i) - (146 >> _flt), i) + (347 >> _flt), i)
        - (877 >> _flt), i) + (6434 >> _flt);
    int k = (PI / 2) - mul(sqrt(ONE - i), j);

    return flag ? (-k) : k;
  }

  /**
   * Returns the cosine of the radian fixed-point integer.
   * <p>
   * Returns cosine for a certain radian. The input must be a fixed-point
   * integer radian. This function is based on sin.
   *
   * @param i    the radian fixed-point integer to get the cosine value of.
   * @return     the cosine for the radian r as a fixed-point integer.
   * @since      KVM CLDC 1.0
   */
  public static int cos(int i) {
    return sin((PI / 2) - i);
  }

  /**
   * Returns the arc cosine of the fixed-point integer.
   * <p>
   * This method is the reverse of the cos function.
   * The function will only accept values between and equal to -1 and 1.
   *
   * @param i    a fixed-point integer to get the arc cosine of.
   * @return     the arc cosine of the fixed-point integer i.
   * @throws     ArithmeticException if the input is < -1 or > 1.
   * @since      KVM CLDC 1.0
   */
  public static int acos(int i) {
    return (PI / 2) - asin(i);
  }

  /**
   * Returns the tangent of the radian fixed-point integer.
   * <p>
   * The input must be a fixed-point integer radian.
   * Input values close to PI/2 or any multiplication of PI/2 will give
   * unpredicted or smaller values than expected. PI/2 will result in an
   * exception.
   *
   * @param i    the radian fixed-point integer to get the tangent value of.
   * @return     the tangent of the radian r as a fixed-point integer.
   * @throws     ArithmeticException if the input is PI/2 or any multplication of
   *             it.
   * @since      KVM CLDC 1.0
   */
  public static int tan(int i) {
    return div(sin(i), cos(i));
  }

  /**
   * Returns the cotangent of the radian fixed-point integer.
   * <p>
   * The input must be a fixed-point integer radian.
   * This method has the same problems as tan. Since cot = 1/tan r
   *
   * @param i    the radian fixed-point integer to get the cotangent value of.
   * @return     the cotangent of the radian r as a fixed-point integer.
   * @throws     ArithmeticException if the input is PI/2 or any multplication of
   *             it.
   * @since      KVM CLDC 1.0
   */
  public static int cot(int i) {
    return div(cos(i), sin(i));
  }

  /**
   * Returns the arc tangent of the radian fixed-point integer.
   * <p>
   * The input must be a fixed-point integer radian.
   *
   * @param i   the radian fixed-point integer to get the arc tangent value of.
   * @return    the arc tangent of the radian r as a fixed-point integer.
   * @throws    ArithmeticException
   * @since     KVM CLDC 1.0
   */
  public static int atan(int i) {
    return asin(div(i, sqrt(ONE + mul(i, i))));
  }

  //=============================================================================
  // Logarithmic / exponential methods
  //=============================================================================

  /**
   * The natural number raised to the power of a fixed-point integer.
   * <p>
   * Calculate the power of the natural number e raised to the value
   * of a fixed-point integer. This function uses a fixed multiplication map
   * for higher acuracy.
   *
   * @param i    the fixed-point integer exponent.
   * @return     the natural number e raised to the power of i.
   * @throws     ArithmeticException if the function overflows
   * @since      KVM CLDC 1.0
   */
  public static int exp(int i) {
    if (i == 0) {
      return ONE;
    }

    boolean flag = i < 0;
    i = abs(i);

    int j = i >> _fbits;
    int k = ONE;

    for (int l = 0; l < (j / 4); l++) {
      k = mul(k, e[4] >> _flt);
    }

    if ((j % 4) > 0) {
      k = mul(k, e[j % 4] >> _flt);
    }

    i &= _fmask;

    if (i > 0) {
      int i1 = ONE;
      int j1 = 0;
      int k1 = 1;

      for (int l1 = 0; l1 < 16; l1++) {
        j1 += (i1 / k1);
        i1 = mul(i1, i);
        k1 *= (l1 + 1);

        if ((k1 > i1) || (i1 <= 0) || (k1 <= 0)) {
          break;
        }
      }

      k = mul(k, j1);
    }

    if (flag) {
      k = div(ONE, k);
    }

    return k;
  }

  /**
   * Returns the natural logarithm of a fixed-point integer.
   * <p>
   * Calculates the natural logarithm of the given fixed-point integer.
   * The fixed point integer must be greater than  0 otherwise an
   * arithmetic exception will be thrown.
   * This function uses a Taylor series and the famous ln(2) constant to
   * calculate the logarithm.
   *
   * @param i the fixed-point integer to get the logarithm of.
   * @return the logarithm of l.
   * @throws ArithmeticException if the input is <= 0.
   * @since KVM CLDC 1.0
   */
  public static int log(int i) {
    if (i <= 0) {
      throw new ArithmeticException("Bad Input");
    }

    int     j    = 0;
    boolean flag = false;
    int     l;

    for (l = 0; i >= (ONE << 1); l++) {
      i >>= 1;
    }

    int i1 = l * (2839 >> _flt);
    int j1 = 0;

    if (i < ONE) {
      return -log(div(ONE, i));
    }

    i -= ONE;

    for (int k1 = 1; k1 < 20; k1++) {
      int k;

      if (j == 0) {
        k = i;
      }
      else {
        k = mul(j, i);
      }

      if (k == 0) {
        break;
      }

      j1 += (((((k1 % 2) != 0) ? 1 : (-1)) * k) / k1);
      j = k;
    }

    return i1 + j1;
  }

  /**
   * Returns a fixed-point integer raised to a fixed-point integer
   * <p>
   * Calculate the power of a fixed-point integer raised to the power
   * of a fixed-point integer.
   * This function is based on the exp and log function. So any
   * precision issues that are embedded in those methods are going to
   * contribute to precision issues in this method.
   * If you are raising to the power of 0.5 then you should use sqrt for
   * higher accuracy.
   * So use this function for things like 3.2 to the power of -4, 2.0 to the
   * power of 0.3, etc...
   *
   * @param i    the fixed-point integer base.
   * @param j    the fixed-point integer exponent.
   * @return     the natural number s raised to the power of e.
   * @throws     ArithmeticException if the function overflows
   * @since      KVM CLDC 1.0
   */
  public static int pow(int i, int j) {
    boolean flag = j < 0;
    int     k = ONE;
    j = abs(j);

    for (int l = j >> _fbits; l-- > 0;) {
      k = mul(k, i);
    }

    if (k < 0) {
      throw new ArithmeticException("Overflow");
    }

    if (i != 0) {
      k = mul(k, exp(mul(log(i), j & _fmask)));
    }
    else {
      k = 0;
    }

    if (flag) {
      return div(ONE, k);
    }
    else {
      return k;
    }
  }

  //=============================================================================
  // Misc Methods
  //=============================================================================

  /**
   * Returns the pricipal value of the arc tangent of y/x
   * <p>
   * Compute the principal value of the arc tangent of y/x, using the signs
   * of both parameters to determine the quadrant of the return value
   * or inother words computes elementwise the angle in radian between the
   * positive part of the x-axis and the line with origin in (0,0) which
   * contains the point (x, y).
   *
   * @param l the fixed-point integer
   * @param l1 the fixed-point integer
   * @return the pricipal value of the arctangent l/l1
   * @since KVM CLDC 1.0
   */
  public static int atan2(int i, int j) {
    int k = 0;

    if (j > 0) {
      k = atan(div(i, j));
    }
    else if (j < 0) {
      k = ((j >= 0) ? PI : (-PI)) - atan(abs(div(i, j)));
    }
    else {
      if ((j == 0) && (i == 0)) {
        throw new ArithmeticException("Bad Input");
      }

      k = ((j >= 0) ? PI : (-PI)) / 2;
    }

    return k;
  }

  /**
   * DOCUMENT ME!
   *
   * @param a DOCUMENT ME!
   */
  public static void main(String[] a) {
    /*
        System.out.println(com.itlity.math.Float.toString(FLOAT_MAX));
        System.out.println(com.itlity.math.Float.toString(FLOAT_MIN));

        float[] vals = new float[]
        {
          0.0F, 1.0F, 56.1321F, 152e-1F, 10500000F
        };

        for (int i = 0 ; i < vals.length ; i++)
        {
          System.out.println(toString(toFP(vals[i])));
        }
    */
    System.out.println(Float.toString(toFloat(E)));
    System.out.println(Float.toString(toFloat(PI)));
    System.out.println(Float.toString(toFloat(ONE)));
  }
}