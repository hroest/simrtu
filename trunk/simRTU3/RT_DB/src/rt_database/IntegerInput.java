/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rt_database;

import lib.interfaces.xml.XMLInterface;
import lib.xml.kXMLElement;


//-----------------------------------------------------------------------------
// Class
//-----------------------------------------------------------------------------

/**
 * <p>
 * Class for <tt> Integer Input </tt> data elements.
 * </p>
 *
 * @author <a href="mailto:alexis.bietti@sysaware.com">Alexis BIETTI</a>
 */
public class IntegerInput extends DataElement implements XMLInterface {
  //---------------------------------------------------------------------------
  // Constructor
  //---------------------------------------------------------------------------
  public IntegerInput() {
    super();

    Record rec = instanciateRecord();
    rec.setValue(new Integer(0));
    rec.quality = Record.Q_INVALID | Record.Q_FORCED;
    writeNewRecord(rec);
  }

  //---------------------------------------------------------------------------
  // Methods
  //---------------------------------------------------------------------------
  public String getName() {
    return "INTEGER_INPUT";
  }

  /**
   * DOCUMENT ME!
   *
   * @return DOCUMENT ME!
   */
  public Object defaultValue() {
    return DEFAULT_INTEGER;
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
  public String servlet() {
    return "inti";
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
    return Record.instanciateRecord(Record.REC_INTEGER);
  }

  //---------------------------------------------------------------------------
  // Interface XMLInterface
  //---------------------------------------------------------------------------

  /**
   * See the general contract of this method in the interface {@link
   * com.itlity.io.XMLInterface}
   */
  public void inputFromXML(kXMLElement xml) throws IllegalArgumentException {
    if (!xml.getTagName().equals(XML_INTEGER_INPUT)) {
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
    root.setTagName(XML_INTEGER_INPUT);

    return root;
  }
}
