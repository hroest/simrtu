/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rt_database;

import lib.interfaces.xml.XMLInterface;
import lib.xml.kXMLElement;


/**
 * <p>
 * Class for <tt> AnalogInput </tt> data elements.
 * </p>
 *
 * @author <a href="mailto:alexis.bietti@sysaware.com">Alexis BIETTI</a>
 */
public class AnalogInput extends AnalogElement implements XMLInterface {
  //---------------------------------------------------------------------------
  // Constructor
  //---------------------------------------------------------------------------
  public AnalogInput() {
    super();
  }

  //---------------------------------------------------------------------------
  // Methods
  //---------------------------------------------------------------------------
  public String getName() {
    return "ANALOG_INPUT";
  }

  /**
   * DOCUMENT ME!
   *
   * @return DOCUMENT ME!
   */
  public String servlet() {
    return "anain";
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
    root.setTagName(XML_ANALOG_INPUT);

    return root;
  }
}
