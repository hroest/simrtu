/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package lib.math;

/**
 * TSE extensions to Double to support parsing and toString
 *
 * @author Chris Smith
 *
 * @date 04/29/2002
 */
public class Double {
  public static final char DECIMAL_CHAR = '.';
  double                   val = 0;

  /**
   * Creates a new Double object.
   */
  public Double() {
    this(0.0);
  }

  /**
   * Creates a new Double object with the given <code>float</code> value
   *
   * @param f a float value
   */
  public Double(float f) {
    val = (double) f;
  }

  /**
   * Creates a new Double object with the given <code>double</code> value
   *
   * @param d DOCUMENT ME!
   */
  public Double(double d) {
    val = d;
  }

  /**
   * Creates a new Double object with the given value.
   *
   * @param s a <code>double</code> litteral
   */
  public Double(String s) throws NumberFormatException {
    val = parseDouble(s);
  }

  //---------------------------------------------------------------------------
  // Methods
  //---------------------------------------------------------------------------

  /**
   * Get the value of this Double.
   *
   * @return a <code>double</code> value
   */
  public double doubleValue() {
    return val;
  }

  /**
   * Get a string representation of this Double.
   *
   * @return a <code>double</code> litteral
   *
   * @see toString(double)
   */
  public String toString() {
    return toString(val);
  }

  /**
   * DOCUMENT ME!
   *
   * @param o DOCUMENT ME!
   *
   * @return DOCUMENT ME!
   */
  public boolean equals(Object o) {
    if ((o != null) && (o instanceof Double)) {
      return val == ((Double) o).val;
    }

    return false;
  }

  /**
   * DOCUMENT ME!
   *
   * @return DOCUMENT ME!
   */
  public int hashCode() {
   //modified by Michael Kratz --> com.ajile.lang.Double.doubleToLongBits replaced by
   // java.lang.Double.doubleToLongBits

      long bits = java.lang.Double.doubleToLongBits(val);

    return (int) (bits ^ (bits >>> 32));
  }

  /**
   * Parse a double value.
   *
   * @param value a String representing a double value
   *
   * @return the double represented by the parameter
   *
   * @throws NumberFormatException if value is not a well formed double
   */
  public static double parseDouble(String value) throws NumberFormatException {
    // Format the string a bit before parsing

    // modified by Michael Kratz --> com.ajile.lang.Math.pow replaced by
    // java.lang.Math.pow

    value = value.trim().toLowerCase();

    int decimalPosition;
    int caratPosition;
    int integerPart    = 0;
    int fractionalPart = 0;
    int decimalPlaces  = 0;
    int exponentPart   = 0;

    // Special characters
    decimalPosition   = value.indexOf(DECIMAL_CHAR);
    caratPosition     = value.indexOf('e');

    if (caratPosition >= 0) {
      exponentPart = Integer.parseInt(value.substring(caratPosition + 1));
    }

    if (decimalPosition >= 0) {
      integerPart = Integer.parseInt(value.substring(0, decimalPosition));

      if (caratPosition >= 0) {
        fractionalPart =
          Integer.parseInt(
            value.substring(decimalPosition + 1, caratPosition));
        decimalPlaces = decimalPosition - caratPosition + 1;
      } else {
        fractionalPart =
          Integer.parseInt(value.substring(decimalPosition + 1));
        decimalPlaces = decimalPosition - value.length() + 1;
      }
    } else {
      if (caratPosition >= 0) {
        integerPart = Integer.parseInt(value.substring(0, caratPosition));
      } else {
        integerPart = Integer.parseInt(value);
      }
    }

    double d =
      (integerPart
      + (fractionalPart * java.lang.Math.pow(10.0, decimalPlaces))) * java.lang.Math
      .pow(10.0, exponentPart);

    return d;
  }

  /**
   * Get a String representation of a double.
   *
   * @param d a double
   *
   * @return the String representation of the parameter.
   */
  public static String toString(double d) {
    long         wholePart      = 0L;
    long         fractionalPart = 0L;
    StringBuffer buf            = new StringBuffer();

    if (d < 0.0) {
      buf.append('-');
      d = -d;
    }

    wholePart        = (long) d;
    fractionalPart   = (long) (100000.0 + ((d % 1.0) * 100000.0));

    buf.append(wholePart);
    buf.append(DECIMAL_CHAR);

    if (fractionalPart == 100000L) {
      buf.append('0');
    } else {
      buf.append(Long.toString(fractionalPart).substring(1));
    }

    return buf.toString();
  }
}
