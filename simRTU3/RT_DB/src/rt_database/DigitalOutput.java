/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rt_database;

import lib.interfaces.xml.XMLInterface;
import lib.xml.kXMLElement;
import lib.util.ArrayUtils;

//-----------------------------------------------------------------------------
// Class
//-----------------------------------------------------------------------------

/**
 * Digital output class.
 *
 * @author <a href="mailto:alexis.bietti@sysaware.com">Alexis BIETTI</a>
 */
public class DigitalOutput extends DataElement implements XMLInterface,
  Command {
  public static final String XML_MAINTAIN_TIME = "maintainTime";
  public static final String XML_PERMANENT = "permanent";
  public static final String XML_SHORT     = "short";
  public static final String XML_LONG      = "long";

  /*
   * Maintain time constants.
   */
  public static final int PERMANENT = 0;
  public static final int SHORT = 1;
  public static final int LONG  = 2;

  /*
   * Maintain time labels.
   */
  public static final String[] TIMES =
    new String[] { XML_PERMANENT, XML_SHORT, XML_LONG };

  //---------------------------------------------------------------------------
  // Attributes
  //---------------------------------------------------------------------------

  /** Logic of this DigitalOutput. */
  public boolean logic = true;

  /**
   * Command mode of this DigitalOutput.
   *
   * @see #PERMANENT
   * @see #SHORT
   * @see #LONG
   */
  public int maintainTime = PERMANENT;

  /**
   * The select before operate that is responsible of this digital output. By
   * default, it is set to -1, meaning no SBO.
   */
  public int sboID = -1;

  //---------------------------------------------------------------------------
  // Constructor
  //---------------------------------------------------------------------------

  /**
       *
       */
  public DigitalOutput() {
    super();
    Record rec = instanciateRecord();
    rec.setValue(DEFAULT_DIGITAL);
    rec.quality = Record.Q_INVALID | Record.Q_FORCED;
    writeNewRecord(rec);
  }

  //---------------------------------------------------------------------------
  // Methods
  //---------------------------------------------------------------------------
  public String getName() {
    return "DIGITAL_OUTPUT";
  }

  /**
   * DOCUMENT ME!
   *
   * @return DOCUMENT ME!
   */
  public Object defaultValue() {
    return DEFAULT_DIGITAL;
  }

  /**
   * DOCUMENT ME!
   *
   * @return DOCUMENT ME!
   */
  public int getDefaultLogSize() {
    return 4;
  }

  /**
   * DOCUMENT ME!
   *
   * @return DOCUMENT ME!
   */
  public int getSelectBeforeOperateID() {
    return sboID;
  }

  /**
   * DOCUMENT ME!
   *
   * @return DOCUMENT ME!
   */
  public String servlet() {
    return "digout";
  }

  /**
   * <p>
   * See the general contract of the method <tt>instanciateRecord()</tt> in
   * the class DataElement.
   * </p>
   *
   * <p>
   * Return a Record compatible with the DigitalOutput class.
   * </p>
   *
   * @return a Record compatible with the DigitalOutput class.
   */
  public Record instanciateRecord() {
    return Record.instanciateRecord(Record.REC_DIGITAL);
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
      maintainTime =
        ArrayUtils.lastIndexOf(TIMES, xml.getProperty(XML_MAINTAIN_TIME));
      this.logic   = xml.getBooleanProperty(XML_LOGIC, true);
      this.sboID   = xml.getProperty(XML_SBOID, -1);

      // Default maintainTime value = PERMANENT
      if (maintainTime == -1) {
        maintainTime = PERMANENT;
      }
    } catch (Exception e) // NullPointerException or IllegalArgumentException
     {
      e.printStackTrace();
      throw ILLEGAL_ARGUMENT_EXCEPTION;
    }

    // Retrieve the parameters defined in the super class
    super.inputFromXML(xml);
  }

  /**
   * See the general contract of this method in the interface {@link
   * com.itlity.io.XMLInterface}
   */
  public kXMLElement outputToXML() {
    // Get the parameters defined in the super class
    kXMLElement root = super.outputToXML();

    // Add all necessary properties
    root.setTagName(XML_DIGITAL_OUTPUT);
    root.addProperty(XML_LOGIC, logic);

    if (maintainTime != PERMANENT) {
      try {
        root.addProperty(XML_MAINTAIN_TIME, TIMES[maintainTime]);
      } catch (ArrayIndexOutOfBoundsException aioobe) {
        System.err.print(maintainTime);
        aioobe.printStackTrace();
      }
    }

    if (sboID >= 0) {
      root.addProperty(XML_SBOID, sboID);
    }

    return root;
  }
}
