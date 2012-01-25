/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rt_database;

import lib.xml.kXMLElement;

//-----------------------------------------------------------------------------
// Class
//-----------------------------------------------------------------------------

/**
 * <p>
 * Select before operate (SBO) locks the access to an output data element.
 * </p>
 *
 * <p>
 * The SELECT request is sent by launching the method <tt>
 * getRefreshedRecord() </tt>.
 * </p>
 *
 * <p>
 * The method returns a {@link Record} whose value is either true (SELECT
 * succes) or false (SELECT failed), and whose quality may content
 * information about the state of the device. Don't trust the {@link Record}
 * if the flag INVALID is set.
 * </p>
 *
 * @author <a href="mailto:alexis.bietti@sysaware.com">Alexis BIETTI</a>
 * @version 1.0
 */
public class SelectBeforeOperate extends DataElement {
  //---------------------------------------------------------------------------
  // Constants
  //---------------------------------------------------------------------------
  public static final boolean SELECT_ACCEPTED = true;
  public static final boolean SELECT_REJECTED = false;

  //---------------------------------------------------------------------------
  // Attributes
  //---------------------------------------------------------------------------

  /**
   * Database ID of the command associated with this Select Before Operate
   * object.
   */
  public int commandID = -1;

  //---------------------------------------------------------------------------
  // Constructors
  //---------------------------------------------------------------------------

  /**
   * Standard constructor.
   */
  public SelectBeforeOperate() {
    // Remember the last value only, in no way we shall manage several values.
    super();

    // Put a default value
    Record rec = instanciateRecord();
    rec.setValue(new Boolean(SELECT_REJECTED));
    rec.quality = Record.Q_FORCED | Record.Q_INVALID;
  }

  //---------------------------------------------------------------------------
  // Methods
  //---------------------------------------------------------------------------
  public String getName() {
    return "SELECT_BEFORE_OPERATE";
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
   */
  public void setInvalid() {
    Record rec = instanciateRecord();
    rec.setValue(false);
    rec.quality = Record.Q_INVALID | Record.Q_FORCED;
    writeNewRecord(rec);
  }

  /**
   * DOCUMENT ME!
   *
   * @return DOCUMENT ME!
   */
  public int getDefaultLogSize() {
    return 1;
  }

  /**
   * <p>
   * See the general contract of this method in the super class {@link
   * com.itlity.luciol.database.DataElement}
   * </p>
   */
  public Record instanciateRecord() {
    return Record.instanciateRecord(Record.REC_DIGITAL);
  }

  /**
   * See the general contract of this method in the super class {@link
   * com.itlity.luciol.database.DataElement}
   */
  protected DataElement copy() {
    SelectBeforeOperate aCopy = new SelectBeforeOperate();
    aCopy.commandID = commandID;

    return aCopy;
  }

  /**
   * See the general contract of this method in the super class {@link
   * com.itlity.luciol.database.DataElement}
   */
  public String servlet() {
    return "sbo";
  }

  /**
   * Return the ID of the data element monitored by this Select Before
   * Operate.
   *
   * @return a strictly positive integer.
   */
  public int getCommandID() {
    return commandID;
  }

  //---------------------------------------------------------------------------
  // Interface XMLInterface
  //---------------------------------------------------------------------------

  /**
   * See the general contract of this method in the interface {@link
   * com.itlity.io.XMLInterface}
   */
  public kXMLElement outputToXML() {
    kXMLElement xml = super.outputToXML();

    xml.setTagName("selectBeforeOperate");
    xml.addProperty("command", this.commandID);

    return xml;
  }

  /**
   * See the general contract of this method in the interface {@link
   * com.itlity.io.XMLInterface}
   */
  public void inputFromXML(kXMLElement xml) throws IllegalArgumentException {
    try {
      this.commandID = xml.getIntProperty("command");
    } catch (Exception e) {
      e.printStackTrace();
      throw new IllegalArgumentException("Invalid attribute command in SBO");
    }

    super.inputFromXML(xml);
  }
}
