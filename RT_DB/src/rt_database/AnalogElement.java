/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rt_database;

import lib.interfaces.xml.XMLInterface;
import lib.xml.kXMLElement;
import lib.util.ArrayUtils;

/**
 * <p>
 * Common requirements of analog inputs and outputs.
 * </p>
 *
 * @author <a href="mailto:alexis.bietti@sysaware.com">Alexis BIETTI</a>
 *
 * @see AnalogInput
 * @see AnalogOutput
 */
public abstract class AnalogElement extends DataElement implements XMLInterface {
  //---------------------------------------------------------------------------
  // Constants
  //---------------------------------------------------------------------------
  public static final String XML_NONE        = "None";
  public static final String XML_LINEAR      = "Linear";
  public static final String XML_SQRT_LINEAR = "SqrtLinear";
  public static final String XML_LINEAR_SQRT = "LinearSqrt";
  public static final String XML_LAW         = "law";
  public static final String XML_SCALE       = "scale";
  public static final String XML_OFFSET      = "offset";
  public static final String XML_UNIT        = "unit";

  /*
 * Scaling laws identifiers, if any.
 */

  /** a.x + b */
  public static final int LAW_NONE = 0;

  /** a.x + b */
  public static final int LAW_LINEAR = 1;

  /** sqrt(a.x + b) */
  public static final int LAW_SQRT_LINEAR = 2;

  /** a.sqrt(x) + b */
  public static final int LAW_LINEAR_SQRT = 3;

  /**
   *
   */
  public String[] LAW_NAMES =
    new String[] { XML_NONE, XML_LINEAR, XML_SQRT_LINEAR, XML_LINEAR_SQRT };

  //---------------------------------------------------------------------------
  // Attributes
  //---------------------------------------------------------------------------

  /** Scale. */
  public float scale = 1.0F;

  /** Offset. */
  public float offset = 0.0F;

  /** Unit. */
  public String unit = "";

  /** Scaling law of this AnalogInput. */
  public int law = LAW_NONE;

  //---------------------------------------------------------------------------
  // Constructor
  //---------------------------------------------------------------------------

  /**
   * Construct a new analog element with a default history size.
   */
  public AnalogElement() {
    super();

    Record rec = instanciateRecord();
    rec.setValue(DEFAULT_ANALOG);
    rec.quality = Record.Q_INVALID | Record.Q_FORCED;
    writeNewRecord(rec);
  }

  //---------------------------------------------------------------------------
  // Methods
  //---------------------------------------------------------------------------
  public Object defaultValue() {
    return DEFAULT_ANALOG;
  }

  /**
   * <p>
   * See the general contract of the method <tt>instanciateRecord()</tt> in
   * the class DataElement.
   * </p>
   *
   * <p>
   * Return a Record compatible with the AnalogInput class.
   * </p>
   *
   * @return a Record compatible with the AnalogInput class.
   */
  public Record instanciateRecord() {
    return Record.instanciateRecord(Record.REC_FLOAT);
  }

  /**
   * All new values are scaled before they are stored in the database.
   *
   * @see #law
   * @see #scale
   * @see #offset
   */
  public float scaleThisValue(float rawValue) {
    double scaledValue = (double) rawValue;

    switch (law) {
      case LAW_LINEAR_SQRT:
        scaledValue = Math.sqrt(scaledValue);

      // Fall through
      case LAW_LINEAR:
        scaledValue *= scale;
        scaledValue += offset;

        break;

      case LAW_SQRT_LINEAR:
        scaledValue *= scale;
        scaledValue += offset;
        scaledValue = Math.sqrt(scaledValue);

        break;

      // By default, or if the law is NONE, do nothing.
    }

    return (float) scaledValue;
  }

  /**
   *
   */
  public int getDefaultLogSize() {
    return 4;
  }

  //---------------------------------------------------------------------------
  // Interface XMLInterface
  //---------------------------------------------------------------------------

  /**
   * See the general contract of this method in the interface {@link
   * com.itlity.io.XMLInterface}
   */
  public void inputFromXML(kXMLElement xml) throws IllegalArgumentException {
    try {
      unit     = xml.getProperty(XML_UNIT, "");
      offset   = xml.getFloatProperty(XML_OFFSET, 0);
      scale    = xml.getFloatProperty(XML_SCALE, 1);
      law      = ArrayUtils.lastIndexOf(LAW_NAMES, xml.getProperty(XML_LAW));
    } catch (Exception e) {
      throw ILLEGAL_ARGUMENT_EXCEPTION;
    }

    super.inputFromXML(xml);
  }

  /**
   * See the general contract of this method in the interface {@link
   * com.itlity.io.XMLInterface}
   */
  public kXMLElement outputToXML() {
    kXMLElement root = super.outputToXML();

    if ((unit != null) && (unit.length() > 0)) {
      root.addProperty(XML_UNIT, unit);
    }

    // Just in case we have a funny value here, the array index out of bounds
    // is not pleasant.
    if ((law < LAW_NONE) || (law > 3)) {
      law = LAW_NONE;
    }

    if (law != LAW_NONE) {
      root.addProperty(XML_LAW, LAW_NAMES[law]);

      if (scale != 1F) {
        root.addProperty(XML_SCALE, new lib.math.Float(scale));
      }

      if (offset != 0F) {
        root.addProperty(XML_OFFSET, new lib.math.Float(offset));
      }
    }

    return root;
  }
}
