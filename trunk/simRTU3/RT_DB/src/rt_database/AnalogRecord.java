/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rt_database;

import lib.math.Math32FP;

//-----------------------------------------------------------------------------
// AnalogRecord Class
//-----------------------------------------------------------------------------

/**
 * An analog record stores fixed point values.
 *
 * @author Alexis BIETTI
 */
public class AnalogRecord extends Record {
  /** Inner value of this record. */
  int fpValue = 0;

  /**
   * DOCUMENT ME!
   *
   * @return DOCUMENT ME!
   */
  public Record copy() {
    AnalogRecord copy = new AnalogRecord();
    copy.fpValue     = fpValue;
    copy.timeStamp   = timeStamp;
    copy.quality     = quality;

    return copy;
  }

  /**
   * DOCUMENT ME!
   *
   * @return DOCUMENT ME!
   */
  public boolean booleanValue() {
    return fpValue != 0;
  }

  /**
   * DOCUMENT ME!
   *
   * @return DOCUMENT ME!
   */
  public int intValue() {
    return Math32FP.toInt(fpValue);
  }

  /**
   * DOCUMENT ME!
   *
   * @return DOCUMENT ME!
   */
  public int fixedPointValue() {
    return fpValue;
  }

  /**
   * DOCUMENT ME!
   *
   * @return DOCUMENT ME!
   */
  public String stringValue() {
    return Math32FP.toString(fpValue);
  }

  /**
   * DOCUMENT ME!
   *
   * @return DOCUMENT ME!
   */
  public Object objectValue() {
    return new Math32FP(fpValue);
  }

  /**
   * DOCUMENT ME!
   *
   * @return DOCUMENT ME!
   */
  public float floatValue() {
    return Math32FP.toFloat(fpValue);
  }

  /**
   * DOCUMENT ME!
   *
   * @param newValue DOCUMENT ME!
   */
    @Override
  public void setValue(Object newValue) {
    if (newValue instanceof Math32FP) {
      fpValue = ((Math32FP) newValue).fpValue();
    } else if (newValue instanceof Float)
    {
      fpValue = Math32FP.toFP(((Float)newValue).floatValue());
    } else if (newValue instanceof Integer) {
      fpValue = Math32FP.toFP(((Integer) newValue).intValue());
    } else if (newValue instanceof Short) {
      fpValue = Math32FP.toFP(((Short) newValue).shortValue());
    } else if (newValue instanceof Byte) {
      fpValue = Math32FP.toFP(((Byte) newValue).byteValue());
    } else if (newValue instanceof String) {
      fpValue = Math32FP.toFP((String) newValue);
    } else {
      fpValue = (newValue == null) ? 0 : Math32FP.ONE;
    }
  }



}
