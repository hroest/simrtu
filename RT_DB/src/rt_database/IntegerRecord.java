/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rt_database;

import lib.math.Math32FP;

//-----------------------------------------------------------------------------
// IntegerRecord Class
//-----------------------------------------------------------------------------

/**
 * An integer record stores integer values.
 *
 * @author Alexis BIETTI
 */
public class IntegerRecord extends Record {
  /** Inner value of this record. */
  int value = 0;

  /**
   * DOCUMENT ME!
   *
   * @return DOCUMENT ME!
   */
  public Record copy() {
    IntegerRecord copy = new IntegerRecord();
    copy.value       = value;
    copy.timeStamp   = timeStamp;
    copy.quality     = quality;

    return copy;
  }

  /**
   * DOCUMENT ME!
   *
   * @return DOCUMENT ME!
   */
  public float floatValue() {
    return (float) value;
  }

  /**
   * DOCUMENT ME!
   *
   * @return DOCUMENT ME!
   */
  public boolean booleanValue() {
    return value != 0;
  }

  /**
   * DOCUMENT ME!
   *
   * @return DOCUMENT ME!
   */
  public int intValue() {
    return value;
  }

  /**
   * DOCUMENT ME!
   *
   * @return DOCUMENT ME!
   */
  public int fixedPointValue() {
    return Math32FP.toFP(value);
  }

  /**
   * DOCUMENT ME!
   *
   * @return DOCUMENT ME!
   */
  public String stringValue() {
    return Integer.toString(value);
  }

  /**
   * DOCUMENT ME!
   *
   * @return DOCUMENT ME!
   */
  public Object objectValue() {
    return new Integer(value);
  }

  /**
   * DOCUMENT ME!
   *
   * @param newValue DOCUMENT ME!
   */
  public void setValue(Object newValue) {
    if (newValue instanceof Integer) {
      value = ((Integer) newValue).intValue();
    } else if (newValue instanceof Short) {
      value = ((Short) newValue).shortValue();
    } else if (newValue instanceof Byte) {
      value = ((Byte) newValue).byteValue();
    } else if (newValue instanceof Math32FP) {
      value = Math32FP.toInt(((Math32FP) newValue).fpValue());
    } else if (newValue instanceof String) {
      value = Integer.parseInt((String) newValue);
    } else if (newValue instanceof lib.math.Float) {
      value = (int) ((lib.math.Float) newValue).longValue();
    } else {
      value = (newValue == null) ? 0 : 1;
    }
  }
}