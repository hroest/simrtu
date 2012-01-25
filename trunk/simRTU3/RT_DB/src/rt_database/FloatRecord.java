/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rt_database;

import lib.math.Math32FP;
import lib.math.Float;

//-----------------------------------------------------------------------------
// FloatRecord Class
//-----------------------------------------------------------------------------

/**
 * A float record stores floating point values.
 *
 * @author <a href="mailto:alexis.bietti@sysaware.com">Alexis BIETTI</a>
 */
public class FloatRecord extends Record {
  /** Inner value of this Record. */
  float floatValue = 0;

  /**
   * DOCUMENT ME!
   *
   * @return DOCUMENT ME!
   */
  public Record copy() {
    FloatRecord copy = new FloatRecord();
    copy.floatValue   = floatValue;
    copy.timeStamp    = timeStamp;
    copy.quality      = quality;

    return copy;
  }

  /**
   * DOCUMENT ME!
   *
   * @return DOCUMENT ME!
   */
  public boolean booleanValue() {
    return (floatValue != 0);
  }

  /**
   * DOCUMENT ME!
   *
   * @return DOCUMENT ME!
   */
  public int intValue() {
    return (int) floatValue;
  }

  /**
   * DOCUMENT ME!
   *
   * @return DOCUMENT ME!
   */
  public int fixedPointValue() {
    return Math32FP.toFP(floatValue);
  }

  /**
   * DOCUMENT ME!
   *
   * @return DOCUMENT ME!
   */
  public String stringValue() {
    return Float.toString(floatValue);
  }

  /**
   * DOCUMENT ME!
   *
   * @return DOCUMENT ME!
   */
  public Object objectValue() {
    return new Float(floatValue);
  }

  /**
       *
       */
  public float floatValue() {
    return floatValue;
  }

  /**
       *
       */
    @Override
  public void setValue(Object newValue) {
    if (newValue instanceof lib.math.Float) {
      floatValue = ((lib.math.Float) newValue).floatValue();
    } else if (newValue instanceof Math32FP) {
      floatValue = ((Math32FP) newValue).floatValue();
    } else if (newValue instanceof Integer) {
      floatValue = (float) ((Integer) newValue).intValue();
    } else if (newValue instanceof Boolean) {
      floatValue = ((Boolean) newValue).booleanValue() ? 1 : 0;
    } else if (newValue instanceof String) {
      floatValue = Float.parseFloat((String) newValue);
    } else if (newValue instanceof java.lang.Float)
    {
      floatValue = (java.lang.Float)newValue;
    }
  }
}
