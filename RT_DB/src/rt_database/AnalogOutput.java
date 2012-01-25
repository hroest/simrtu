/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rt_database;

import lib.interfaces.xml.XMLInterface;
import lib.xml.kXMLElement;

/**
 * Class for Analog Output data elements.
 *
 * @author <a href="mailto:alexis.bietti@sysaware.com">Alexis BIETTI</a>
 */
public class AnalogOutput extends AnalogElement implements XMLInterface,
  Command {
  //---------------------------------------------------------------------------
  // Attributes
  //---------------------------------------------------------------------------

  /** ID of the {@link com.itlity.luciol.database.SelectBeforeOperate} */
  public int selectBeforeOperateID = -1;

  //---------------------------------------------------------------------------
  // Constructor
  //---------------------------------------------------------------------------
  public AnalogOutput() {
    super();
  }

  //---------------------------------------------------------------------------
  // Methods
  //---------------------------------------------------------------------------
  public String getName() {
    return "ANALOG_OUTPUT";
  }

  /**
   * DOCUMENT ME!
   *
   * @return DOCUMENT ME!
   */
  public int getSelectBeforeOperateID() {
    return selectBeforeOperateID;
  }

  /**
   * DOCUMENT ME!
   *
   * @return DOCUMENT ME!
   */
  public String servlet() {
    return "anaout";
  }

  //---------------------------------------------------------------------------
  // Interface XMLInterface
  //---------------------------------------------------------------------------

  /**
   * See the general contract of this method in the interface {@link
   * com.itlity.io.XMLInterface}
   */
  public void inputFromXML(kXMLElement xml) throws IllegalArgumentException {
    super.inputFromXML(xml);
  }

  /**
   * See the general contract of this method in the interface {@link
   * com.itlity.io.XMLInterface}
   */
  public kXMLElement outputToXML() {
    kXMLElement root = super.outputToXML();
    root.setTagName(XML_ANALOG_OUTPUT);

    if (selectBeforeOperateID > -1) {
      root.addProperty(XML_SBOID, this.selectBeforeOperateID);
    }

    return root;
  }
}
