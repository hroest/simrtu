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
 * Digital Input class.
 *
 * @author <a href="mailto:alexis.bietti@sysaware.com">Alexis BIETTI</a>
 */
public class DigitalInput extends DataElement {
  //---------------------------------------------------------------------------
  // Attributes
  //---------------------------------------------------------------------------

  /** Logic of this Digital Input. */
  public boolean logic = true;

  //---------------------------------------------------------------------------
  // Constructor
  //---------------------------------------------------------------------------

  /**
       *
       */
  public DigitalInput() {
    super();

    Record rec = instanciateRecord();
    rec.setValue(DEFAULT_DIGITAL);
    rec.quality = Record.Q_INVALID | Record.Q_FORCED;
    writeNewRecord(rec);
  }

  /**
   * Number of samples needed before trusting a measure.
   */

  // public short debounce = 5;

  /**
   * For use with Double inputs.
   */

  //---------------------------------------------------------------------------
  // Methods
  //---------------------------------------------------------------------------
  public String getName() {
    return "DIGITAL_INPUT";
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
    rec.setValue(DEFAULT_DIGITAL);
    rec.quality = Record.Q_INVALID | Record.Q_FORCED;
    writeNewRecord(rec);
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
       *
       */
  public String servlet() {
    return "digin";
  }

  /**
   * <p>
   * See the general contract of the method <tt>instanciateRecord()</tt> in
   * the class DataElement.
   * </p>
   *
   * <p>
   * Return a Record compatible with the DigitalInput class.
   * </p>
   *
   * @return a Record compatible with the DigitalInput class.
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
      logic = xml.getBooleanProperty(XML_LOGIC, true);
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

    root.setTagName(XML_DIGITAL_INPUT);
    root.addProperty(XML_LOGIC, logic ? "1" : "0");

    // root.addProperty("debounce", debounce);
    return root;
  }
}