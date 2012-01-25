/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package lib.math;

/**
 * Frozen version of the MathFP class. Date: 17-sep-2002
 *
 * <p>
 * This class implements 64 bit fixed-point integer math functions.
 * The intend of this class is to provide float like calculations for the
 * KVM CLDC configuration. However this class can be used where ever the long data
 * type is supported.<br>
 * Fixed point integers are integer numbers with an imaginary fixed decimal point.
 * The number consist of a decimal part and a fractional part. The size of the
 * fractional part expressed in the number of bits determines the precision.
 * MathFP Version II uses the long data type to implement fixed-point integers.
 * The main difference and possible advantage between floats and fixed-point integers
 * is that the fixed-point integer domain is equaly spaced whereas a float increases
 * the space between numbers when the exponent raises.
 * <p>
 * This implementation includes variable precision and allows the developer to change
 * the precision during run-time anywhere between 24 and 3 bits for the fraction.
 * Transparent scaling is added where possible to increase the range of the
 * fixed-point integers. However caution should always be taken for numbers
 * close to MAX_VALUE.
 * <br><br>
 *
 * Example:<br><br>
 * <code>
 * long n = MathFP.toFP(12);<br>
 * long m = MathFP.toFP("14.5");<br>
 * long a = n + m;<br>
 *      a = a - MathFP.toFP("1.5");<br>
 *
 *      a = MathFP.div(a,n);<br>
 *      a = MathFP.sqrt(a);<br>
 * System.out.println(MathFP.toString(a));<br>
 * </code>
 * <br>
 * @author Onno Hommes
 * @version 2.0.5
 */
public abstract class Math64FP {
  //=============================================================================
  // Utility constants
  //=============================================================================
  private static int    _fbits  = 24;
  private static int    _digits = 8;
  private static long   _one;
  private static long   _fmask  = 0xffffffL;
  private static long   _dmul   = 0x5f5e100L;
  private static long   _flt    = 0L;
  private static long   _pi;
  private static long[] e;

  //=============================================================================
  // Numerical constants
  //=============================================================================

  /**
   * The fixed-point representation of pi (3.14159265)
   */
  public static long PI;

  /**
   * The fixed-point representation of the natural number (2.71828183).
   */
  public static long E;

  /**
   * The maximum fixed-point representation for the current precision.
   */
  public static final long MAX_VALUE = 0x7fffffffffffffffL;

  /**
   * The minimum fixed-point representation for the current precision.
   */
  public static final long MIN_VALUE = 0x8000000000000001L;

  //=============================================================================
  // Static Initializer
  //=============================================================================
  static {
    _one   = 0x1000000L;
    _pi    = 0x3243f6aL;
    e      = (new long[] { _one, 0x2b7e151L, 0x763992eL, 0x1415e5bfL, 0x3699205cL });
    PI     = _pi;
    E      = e[1];
  }

  //=============================================================================
  // Constructor
  //=============================================================================

  /**
   * Can not be instanciated.
   */
  private Math64FP() {
  }

  //=============================================================================
  // Utility methods
  //=============================================================================

  /**
   * Set the runtime precision for MathFP.
   * <p>
   * The precision can be anywhere from 24 to 0 bits. However using 0 bits for the
   * precision makes no sense for this library. The lowest setting would usually
   * be 3. The default precision is 24 bits for the fraction.
   *
   * @param i the number of fractional bits.
   * @return the number of visual digits behind the decimal point.
   * @since KVM CLDC 1.0
   */
  public static int setPrecision(int i) {
    if ((i > 24) || (i < 0)) {
      return _digits;
    }

    _fbits    = i;
    _one      = 1L << i;
    _flt      = 24 - i;
    _digits   = 0;
    _dmul     = 1L;
    _fmask    = _one - 1L;
    PI        = _pi >> (int) _flt;
    E         = e[1] >> (int) _flt;

    for (long l = _one; l != 0L;) {
      l /= 10L;
      _digits++;
      _dmul *= 10L;
    }

    return _digits;
  }

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
   * Converts a fixed-point integer to a long.
   * <p>
   * This method will round the fixed point integer before converting it to a
   * normal long. To avoid rounding you can shift the value precision bits to
   * the right.
   *
   * @param l the fixed-point integer to be converted.
   * @return a long in a normal integer representation.
   * @since KVM CLDC 1.0
   */
  public static long toLong(long l) {
    l = round(l, 0);

    return l >> _fbits;
  }

  /**
   * Converts a normal long to a fixed-point integer.
   * <p>
   * The input long will be shifted the number of fraction bits to
   * the left.
   *
   * @param the long to be converted to a fixed-point integer
   * @return a fixed-point integer with runtime precision bits fraction.
   * @since KVM CLDC 1.0
   */
  public static long toFP(long l) {
    return l << _fbits;
  }

  /**
   * Changes precision of a fixed-point integer.
   * <p>
   * The input long will be adjusted from its current precision to the current
   * runtime precision. The function includes rounding before converting.
   * This function allows the developer to use different precisions during
   * runtime.
   *
   * @param l the long to change the precision of.
   * @param i the current precision of the fixed-point integer.
   * @return a fixed-point integer with the current runtime precision.
   * @since KVM CLDC 1.0
   */
  public static long convert(long l, int i) {
    long l1 = (l >= 0L) ? 1L : (-1L);

    if (abs(i) < 25L) {
      if (_fbits < i) {
        l = (l + (l1 * (1L << ((i - _fbits) >> 1)))) >> (i - _fbits);
      }
      else {
        l <<= (_fbits - i);
      }
    }

    return l;
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
   * @param s the String to be converted to a fixed-point integer.
   * @return a fixed-point integer with precision bits fraction.
   * @throws NumberFormatException if the input is invalid.
   * @since KVM CLDC 1.0
   */
  public static long toFP(String s) {
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

    long l = 0L;

    if (i != j) {
      l = Long.parseLong(s.substring(i, j));
    }

    long l1 = Long.parseLong(s1) + 1L;
    long l2 = (l << _fbits) + ((l1 << _fbits) / _dmul);

    if (i == 1) {
      l2 = -l2;
    }

    return l2;
  }

  /**
   * Converts a fixed-point integer to a string.
   * <p>
   * This method allows you to display a fixed-point integer in a more
   * user friendly way. The number of digits behind the decimal point will be
   * the number returned by setPrecision(...).
   *
   * @param l the fixed-point integer to be converted.
   * @return a string representing the fixed-point integer value.
   * @since KVM CLDC 1.0
   */
  public static String toString(long l) {
    boolean flag = false;

    if (l < 0L) {
      flag   = true;
      l      = -l;
    }

    long   l1 = l >> _fbits;
    long   l2 = (_dmul * (l & _fmask)) >> _fbits;
    String s;

    for (s = Long.toString(l2); s.length() < _digits; s = "0" + s) {
      ;
    }

    return (flag ? "-" : "") + Long.toString(l1) + "." + s;
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
   * @param l the fixed-point integer to be converted.
   * @param i the number of digits to show of the fraction.
   * @return a string representing the rounded fixed-point integer.
   * @since KVM CLDC 1.0
   */
  public static String toString(long l, int i) {
    if (i > _digits) {
      i = _digits;
    }

    String s = toString(round(l, i));

    return s.substring(0, (s.length() - _digits) + i);
  }

  //=============================================================================
  // Arithmetic methods
  //=============================================================================

  /**
   * Returns the biggest number of the two fixed-point integers.
   * <p>
   * This method returns the biggest of the two numbers.
   *
   * @param l a fixed-point integer.
   * @param l1 a fixed-point integer.
   * @return the maximum of the two fixed-point integers.
   * @since KVM CLDC 1.0
   */
  public static long max(long l, long l1) {
    return (l >= l1) ? l : l1;
  }

  /**
   * Returns the smallest number of the two fixed-point integers.
   * <p>
   * This method returns the smallest of the two numbers.
   *
   * @param l a fixed-point integer.
   * @param l1 a fixed-point integer.
   * @return the minimum of the two fixed-point integers.
   * @since KVM CLDC 1.0
   */
  public static long min(long l, long l1) {
    return (l1 >= l) ? l : l1;
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
  public static long round(long l, int i) {
    long l1 = 10L;

    for (int j = 0; j < i; j++) {
      l1 *= 10L;
    }

    l1 = div(toFP(5L), toFP(l1));

    if (l < 0L) {
      l1 = -l1;
    }

    return l + l1;
  }

  /**
   * Multiplies two fixed-point integers.
   * <p>
   * The method will multiply the fixed-point n with the fixed-point m.
   * Overflows and underflows are not always flaged so be carefull when
   * operating near boudry areas. Remember these numbers are fixed points so
   * they are internally bigger then you might perceive.
   *
   * @param l the fixed-point integer to be multiplied.
   * @param l1 the fixed-point integer multiplier
   * @return a new fixed point integer representing l multiplied by l1.
   * @throws ArithmeticException if the function overflows.
   * @since KVM CLDC 1.0
   */
  public static long mul(long l, long l1) {
    boolean flag = false;
    int     i  = _fbits;
    long    l2 = _fmask;

    if ((l & l2) == 0L) {
      return (l >> i) * l1;
    }

    if ((l1 & l2) == 0L) {
      return l * (l1 >> i);
    }

    if (((l < 0L) && (l1 > 0L)) || ((l > 0L) && (l1 < 0L))) {
      flag = true;
    }

    if (l < 0L) {
      l = -l;
    }

    if (l1 < 0L) {
      l1 = -l1;
    }

    for (; max(l, l1) >= (1L << (63 - i)); i--) {
      l >>= 1;
      l1 >>= 1;
      l2 >>= 1;
    }

    long l3 = ((l >> i) * (l1 >> i)) << i;
    long l4 = ((l & l2) * (l1 & l2)) >> i;
    l4 += (((l & ~l2) * (l1 & l2)) >> i);
    l3 = (l3 + l4 + (((l & l2) * (l1 & ~l2)) >> i)) << (_fbits - i);

    if (l3 < 0L) {
      throw new ArithmeticException("Overflow");
    }
    else {
      return flag ? (-l3) : l3;
    }
  }

  /**
   * Divides two fixed-point integers.
   * <p>
   * This method will divide the fixed point n by the fixed point m.
   * Overflows and underflows are not flaged so be carefull when operating near
   * boundry areas.
   * This method includes floating point behavior also referred to as scaling.
   * This scaling is transparent to the developer and increases the range of
   * the division method at the cost off precision.
   *
   * @param l the dividend fixed-point integer.
   * @param l1 the divider fixed-point integer.
   * @return a new fixed point integer representing n divided by m.
   * @since KVM CLDC 1.0
   */
  public static long div(long l, long l1) {
    boolean flag = false;
    int     i = _fbits;

    if (l1 == _one) {
      return l;
    }

    if ((l1 & _fmask) == 0L) {
      return l / (l1 >> i);
    }

    if (((l < 0L) && (l1 > 0L)) || ((l > 0L) && (l1 < 0L))) {
      flag = true;
    }

    if (l < 0L) {
      l = -l;
    }

    if (l1 < 0L) {
      l1 = -l1;
    }

    for (; max(l, l1) >= (1L << (63 - i)); i--) {
      l >>= 1;
      l1 >>= 1;
    }

    long l2 = ((l << i) / l1) << (_fbits - i);

    return flag ? (-l2) : l2;
  }

  /**
   * Adds two fixed-point integers.
   * <p>
   * This method is provided for consistency only. Since the inputs are
   * fixed-point integers you might as well use the + operator.<br>
   * MathFP.add(toFP(1),toFP(2)) equals toFP(1) + toFP(2).
   *
   * @param l the fixed-point integer to add to.
   * @param l1 the fixed-point integer to be added.
   * @return a new fixed point integer representing the addition of l and l1.
   * @since KVM CLDC 1.0
   */
  public static long add(long l, long l1) {
    return l + l1;
  }

  /**
   * Subtracts two fixed-point integers.
   * <p>
   * This method is provided for consistency only. Since the inputs are
   * fixed-point integers you might as well use the - operator.<br>
   * MathFP.sub(toFP(2),toFP("1.5")) equals toFP(1) - toFP("1.5").
   *
   * @param l the fixed-point integer to substract to.
   * @param l1 the fixed-point integer to be substracted.
   * @return a new fixed point integer representing the substraction of l
   *         and l1.
   * @since KVM CLDC 1.0
   */
  public static long sub(long l, long l1) {
    return l - l1;
  }

  /**
   * Returns the absolute value of the fixed-point integer.
   * <p>
   * If the fixed-point integer is smaller than 0 then it returns -n
   * else it will return n.
   *
   * @param l the fixed-point integer to get the absolute value of.
   * @return a new fixed point integer representing the absolute value of n.
   * @since KVM CLDC 1.0
   */
  public static long abs(long l) {
    if (l < 0L) {
      return -l;
    }
    else {
      return l;
    }
  }

  /**
   * Returns the square root of the the fixed-point integer.
   * <p>
   * This function calculates the square root of a fixed-point integer.
   * This method is extreemly fast and is the shortest sqrt function available.
   * This method allows the developer to specify the number of iterations (r)
   * to use to extract the root from n to allow performance control. This is
   * usefull if many sqrt calls have to be made. The higher the number the more
   * precise the answer. Typical values for r range from 6 to 24 depending on
   * the runtime precision. Higher numbers will not give you any better
   * results.
   * <p>
   * This method uses the Newton method to extract the root.
   *
   * @param l the fixed-point integer to extract the root from.
   * @param i an integer that specifies the number of iterations.
   * @return the fixed-point integer sqrt of n.
   * @throws ArithmeticException for negative inputs and overflows.
   * @since KVM CLDC 1.0
   */
  public static long sqrt(long l, int i) {
    if (l < 0L) {
      throw new ArithmeticException("Bad Input");
    }

    if (l == 0L) {
      return 0L;
    }

    long l1 = (l + _one) >> 1;

    for (int j = 0; j < i; j++) {
      l1 = (l1 + div(l, l1)) >> 1;
    }

    if (l1 < 0L) {
      throw new ArithmeticException("Overflow");
    }
    else {
      return l1;
    }
  }

  /**
   * Returns the square root of the the fixed-point integer.
   * <p>
   * Internally this method uses the sqrt(n,r) with 24 iterations.
   * Auto adjustment is not provided yet. All limitations of sqrt(n,r) apply.
   *
   * @param l the fixed-point integer to extract the root from.
   * @return the fixed-point integer sqrt of n.
   * @throws ArithmeticException for negative inputs and overflows.
   * @since KVM CLDC 1.0
   */
  public static long sqrt(long l) {
    return sqrt(l, 24);
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
   * @param l the radian fixed-point integer to get the sine value of.
   * @return the sine for the radian n as a fixed-point integer.
   * @since KVM CLDC 1.0
   */
  public static long sin(long l) {
    long l1 = mul(l, div(toFP(180L), PI));
    l1 %= toFP(360L);

    if (l1 < 0L) {
      l1 = toFP(360L) + l1;
    }

    long l2 = l1;

    if ((l1 >= toFP(90L)) && (l1 < toFP(270L))) {
      l2 = toFP(180L) - l1;
    }
    else if ((l1 >= toFP(270L)) && (l1 < toFP(360L))) {
      l2 = -(toFP(360L) - l1);
    }

    long l3 = l2 / 90L;
    long l4 = mul(l3, l3);
    long l5 =
      mul(
        mul(
          mul(
            mul(0xfffffffffffee21aL >> (int) _flt, l4)
            + (0x14594dL >> (int) _flt), l4) - (0xa55b13L >> (int) _flt), l4)
        + (long) (0x1921f9c >> (int) _flt), l3);

    return l5;
  }

  /**
   * Returns the arc sine of the fixed-point integer.
   * <p>
   * This method is the reverse of the sin function and uses a polynomial
   * function to calculate the result.
   * The function will only accept values between and equal to -1 and 1.
   *
   * @param l a fixed-point integer to get the arc sine of.
   * @return the arc sine of the fixed-point integer s.
   * @throws ArithmeticException if the input is < -1 or > 1.
   * @since KVM CLDC 1.0
   */
  public static long asin(long l) {
    if (abs(l) > _one) {
      throw new ArithmeticException("Bad Input");
    }
    else {
      boolean flag = l < 0L;
      l = abs(l);

      long l1 =
        mul(
          mul(
            mul(
              mul(0x236cf >> (int) _flt, l) - (long) (0x92748 >> (int) _flt), l)
            + (long) (0x15acb4 >> (int) _flt), l)
          - (long) (0x36d0dd >> (int) _flt), l)
        + (long) (0x1921f27 >> (int) _flt);
      long l2 = (PI / 2L) - mul(sqrt(_one - l), l1);

      return flag ? (-l2) : l2;
    }
  }

  /**
   * Returns the cosine of the radian fixed-point integer.
   * <p>
   * Returns cosine for a certain radian. The input must be a fixed-point
   * integer radian. This function is based on sin.
   *
   * @param l the radian fixed-point integer to get the cosine value of.
   * @return the cosine for the radian r as a fixed-point integer.
   * @since KVM CLDC 1.0
   */
  public static long cos(long l) {
    return sin((PI / 2L) - l);
  }

  /**
   * Returns the arc cosine of the fixed-point integer.
   * <p>
   * This method is the reverse of the cos function.
   * The function will only accept values between and equal to -1 and 1.
   *
   * @param l a fixed-point integer to get the arc cosine of.
   * @return the arc cosine of the fixed-point integer s.
   * @throws ArithmeticException if the input is < -1 or > 1.
   * @since KVM CLDC 1.0
   */
  public static long acos(long l) {
    return (PI / 2L) - asin(l);
  }

  /**
   * Returns the tangent of the radian fixed-point integer.
   * <p>
   * The input must be a fixed-point integer radian.
   * Input values close to PI/2 or any multiplication of PI/2 will give
   * unpredicted or smaller values than expected. PI/2 will result in an
   * exception.
   *
   * @param l the radian fixed-point integer to get the tangent value of.
   * @return the tangent of the radian r as a fixed-point integer.
   * @throws ArithmeticException if the input is PI/2 or any multplication of
   *                             it.
   * @since KVM CLDC 1.0
   */
  public static long tan(long l) {
    return div(sin(l), cos(l));
  }

  /**
   * Returns the cotangent of the radian fixed-point integer.
   * <p>
   * The input must be a fixed-point integer radian.
   * This method has the same problems as tan. Since cot = 1/tan r
   *
   * @param l the radian fixed-point integer to get the cotangent value of.
   * @return the cotangent of the radian r as a fixed-point integer.
   * @throws ArithmeticException if the input is PI/2 or any multplication of
   *                             it.
   * @since KVM CLDC 1.0
   */
  public static long cot(long l) {
    return div(cos(l), sin(l));
  }

  /**
   * Returns the arc tangent of the radian fixed-point integer.
   * <p>
   * The input must be a fixed-point integer radian.
   *
   * @param l the radian fixed-point integer to get the arc tangent value of.
   * @return the arc tangent of the radian r as a fixed-point integer.
   * @throws ArithmeticException
   * @since KVM CLDC 1.0
   */
  public static long atan(long l) {
    return asin(div(l, sqrt(_one + mul(l, l))));
  }

  /**
   * The natural number raised to the power of a fixed-point integer.
   * <p>
   * Calculate the power of the natural number e raised to the value
   * of a fixed-point integer. This function uses a fixed multiplication map
   * for higher acuracy.
   *
   * @param l the fixed-point integer exponent.
   * @return the natural number e raised to the power of x.
   * @throws ArithmeticException if the function overflows
   * @since KVM CLDC 1.0
   */
  public static long exp(long l) {
    if (l == 0L) {
      return _one;
    }

    boolean flag = l < 0L;
    l = abs(l);

    int  i  = (int) (l >> _fbits);
    long l1 = _one;

    for (int j = 0; j < (i / 4); j++) {
      l1 = mul(l1, e[4] >> (int) _flt);
    }

    if ((i % 4) > 0) {
      l1 = mul(l1, e[i % 4] >> (int) _flt);
    }

    l &= _fmask;

    if (l > 0L) {
      long l2 = _one;
      long l3 = 0L;
      long l4 = 1L;

      for (int k = 0; k < 16; k++) {
        l3 += (l2 / l4);
        l2 = mul(l2, l);
        l4 *= (k + 1);

        if ((l4 > l2) || (l2 <= 0L) || (l4 <= 0L)) {
          break;
        }
      }

      l1 = mul(l1, l3);
    }

    if (flag) {
      l1 = div(_one, l1);
    }

    return l1;
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
   * @param l the fixed-point integer to get the logarithm of.
   * @return the logarithm of l.
   * @throws ArithmeticException if the input is <= 0.
   * @since KVM CLDC 1.0
   */
  public static long log(long l) {
    if (l <= 0L) {
      throw new ArithmeticException("Bad Input");
    }

    long l1 = 0L;
    long l2 = 0L;
    int  i;

    for (i = 0; l >= (_one << 1); i++) {
      l >>= 1;
    }

    long l4 = (long) i * (long) (0xb17218 >> (int) _flt);
    long l5 = 0L;

    if (l < _one) {
      return -log(div(_one, l));
    }

    l -= _one;

    for (int j = 1; j < 20; j++) {
      long l3;

      if (l1 == 0L) {
        l3 = l;
      }
      else {
        l3 = mul(l1, l);
      }

      if (l3 == 0L) {
        break;
      }

      l5 += (((((j % 2) != 0) ? 1L : (-1L)) * l3) / (long) j);
      l1 = l3;
    }

    return l4 + l5;
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
   * @param l the fixed-point integer base.
   * @param l1 the fixed-point integer exponent.
   * @return the natural number s raised to the power of e.
   * @throws ArithmeticException if the function overflows
   * @since KVM CLDC 1.0
   */
  public static long pow(long l, long l1) {
    boolean flag = l1 < 0L;
    long    l2 = _one;
    l1 = abs(l1);

    for (int i = (int) l1 >> _fbits; i-- > 0;) {
      l2 = mul(l2, l);
    }

    if (l2 < 0L) {
      throw new ArithmeticException("Overflow");
    }

    if (l != 0L) {
      l2 = mul(l2, exp(mul(log(l), l1 & _fmask)));
    }
    else {
      l2 = 0L;
    }

    if (flag) {
      return div(_one, l2);
    }
    else {
      return l2;
    }
  }

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
  public static long atan2(long l, long l1) {
    long l2 = 0L;

    if (l1 > 0L) {
      l2 = atan(div(l, l1));
    }
    else if (l1 < 0L) {
      l2 = ((l1 >= 0L) ? PI : (-PI)) - atan(abs(div(l, l1)));
    }
    else {
      if ((l1 == 0L) && (l == 0L)) {
        throw new ArithmeticException("Bad Input");
      }

      l2 = ((l >= 0L) ? PI : (-PI)) / 2L;
    }

    return l2;
  }

  //=============================================================================
  // Main method and Testing
  //=============================================================================
  public static void main(String[] args) {
    long n = Math64FP.toFP(12);
    long m = Math64FP.toFP("14.5");
    long a = n + m;
    a   = a - Math64FP.toFP("1.5");
    a   = Math64FP.div(a, n);
    a   = Math64FP.sqrt(a);
    System.out.println(Math64FP.toString(a));
  }
}
