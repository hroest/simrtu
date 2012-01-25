/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rt_database;

import lib.math.Math32FP;

/**
 * A digital record stores boolean values.
 *
 * @author Alexis BIETTI
 */
public class DigitalRecord extends Record {
  /** Inner value of this Record. */
  //boolean value = true;

  /*
   * 0 - unbestimmt aus
   * 1 - bestimmt aus
   * 2 - bestimmt ein
   * 3 - unbestimmt ein
   */

  int value=1;

  /**
   * DOCUMENT ME!
   *
   * @return DOCUMENT ME!
   */
  @Override
  public Record copy() {
    DigitalRecord copy = new DigitalRecord();
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

  @Override
  public boolean booleanValue() {
      switch (value)
      {
          case 0:
              return false;

          case 1:
              return false;


          case 2:
              return true;

          case 3:
              return true;

          default:
              return false;
      }

  }

  /**
   * DOCUMENT ME!
   *
   * @return DOCUMENT ME!
   */
  @Override
  public int intValue() {
    return value;
  }

  /**
   * DOCUMENT ME!
   *
   * @return DOCUMENT ME!
   */
  @Override
  public int fixedPointValue() {
    return Math32FP.toFloatBits(value);
  }

  /**
   * DOCUMENT ME!
   *
   * @return DOCUMENT ME!
   */
  @Override
  public String stringValue() {
        switch (value)
      {
          case 0:
              return "unbestimmt aus";

          case 1:
              return "bestimmt aus";


          case 2:
              return "bestimmt ein";

          case 3:
              return "unbestimmt ein";

          default:
              return "unbekannt";
      }
  }

  /**
   * DOCUMENT ME!
   *
   * @return DOCUMENT ME!
   */
  @Override
  public Object objectValue() {
    return new Integer(value);
  }

  /**
   * DOCUMENT ME!
   *
   * @return DOCUMENT ME!
   */
  @Override
  public float floatValue() {
    return value;
  }

  /**
   * DOCUMENT ME!
   *
   * @param newValue DOCUMENT ME!
   */
  @Override
  public void setValue(Object newValue) {
  
      if (newValue instanceof Boolean) {
          if ((Boolean) newValue) {
              this.value = 2;
          } else {
              this.value = 1;
          }
      } else if (newValue instanceof Integer) {
          value = (((Integer) newValue).intValue());
      } else if (newValue instanceof Math32FP)
      {
          value = ((Math32FP)newValue).intValue();
      } else if (newValue instanceof lib.math.Float)
      {
          value = ((lib.math.Float)newValue).intValue();
      } else if (newValue instanceof java.lang.Float)
      {
          value = ((java.lang.Float)newValue).intValue();
      }



  }
}
