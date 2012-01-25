/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package lib.interfaces.xml;

import lib.xml.kXMLElement;

/**
 *
 * @author mikra
 */
/**
 * <p>
 * This interface describes two methods to read/write object to and from XML
 * elements.
 * </p>
 *
 * <p>
 * The XML object format is <tt>kXMLElement</tt>, from NanoXML.
 * </p>
 *
 * @author Alexis BIETTI
 *
 * @see com.itlity.io.kXMLElement
 */
public interface XMLInterface {
  /**
   * <p>
   * This method parses the specified XML element. This object is then
   * initialized from the XML element, but only if it is a VALID XML element
   * for this object.
   * </p>
   *
   * <p>
   * If <tt>xml</tt> is not a valid XML element, then an
   * <tt>IllegalArgumentException</tt> is thrown.
   * </p>
   *
   * <p>
   * The element <tt>xml</tt> is invalid if at least one of these conditions
   * is not respected:
   *
   * <ul>
   * <li>
   * <tt>xml</tt> matches the expected name
   * </li>
   * <li>
   * <tt>xml</tt> attributes are compliant (names and values)
   * </li>
   * <li>
   * <tt>xml</tt> children respect the same conditions
   * </li>
   * </ul>
   * </p>
   *
   * <p>
   * Keep in mind that XML is CaSe SeNsItIvE when you implement this method.
   * </p>
   *
   * @param xml a kXMLElement
   *
   * @throws IllegalArgumentException if <tt>xml</tt> is not a valid XML
   *         element.
   */
  public void inputFromXML(kXMLElement xml) throws IllegalArgumentException;

  /**
   * <p>
   * This method returns an XML representation of this object.
   * </p>
   *
   * <p>
   * The XML element returned by this method MUST be compliant with the input
   * method. In a perfect world, it means that the object would strictely
   * retrieve its initial state if we apply the following sequence:
   * </p>
   *
   * <p>
   * <pre>
   * kXMLElement xml = object.outputToXML();
   * object.inputFromXML(xml);
   * </pre>
   * </p>
   *
   * @return an XML element representing this object.
   */
  public kXMLElement outputToXML();
}
