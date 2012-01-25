/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rt_database;

import lib.xml.kXMLElement;

/**
 * A {@link DoubleOutput} is a command that affects two single {@link
 * DigitalOutput} commands.
 *
 * <p>
 * When a {@link Record} is written to this double output, then the same
 * {@link Record} is written to the both single outputs.
 * </p>
 *
 * @author Alexis BIETTI
 * @version 1.0
 */
public class DoubleOutput extends DigitalOutput {
  public static final String XML_OUTPUT1 = "output1";
  public static final String XML_OUTPUT2 = "output2";

  //---------------------------------------------------------------------------
  // Attributes
  //---------------------------------------------------------------------------
  DigitalOutput dOutput1 = null;
  DigitalOutput dOutput2 = null;

  /** ID of the first {@link DigitalOutput}. */
  protected int output1 = -1;

  /** ID of the second {@link DigitalOutput}. */
  protected int output2 = -1;

  //---------------------------------------------------------------------------
  // Constructor
  //---------------------------------------------------------------------------

  /**
   * Standard constructor.
   */
  public DoubleOutput() {
    super();
  }

  //---------------------------------------------------------------------------
  // Methods
  //---------------------------------------------------------------------------
  public String getName() {
    return "DOUBLE_OUTPUT";
  }

  /**
   * Return the database ID of the first {@link DigitalOutput} monitored by
   * this {@link DoubleOutput}
   */
  public int getOutput1() {
    return output1;
  }

  /**
   * Return the database ID of the second {@link DigitalOutput} monitored by
   * this {@link DoubleOutput}
   */
  public int getOutput2() {
    return output2;
  }

  //---------------------------------------------------------------------------
  // Methods overriden from DataElement
  //---------------------------------------------------------------------------

  /**
   * See the general contract of this method in the super class {@link
   * DataElement}
   */
  public Record instanciateRecord() {
    return Record.instanciateRecord(Record.REC_DIGITAL);
  }

  /**
   * See the general contract of this method in the super class {@link
   * DataElement}
   */
  public String servlet() {
    return "dbo";
  }

  /**
   * DOCUMENT ME!
   *
   * @return DOCUMENT ME!
   */
  public int getDefaultLogSize() {
    return 3;
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
      int anOutput1 = -1; // Temp input 1
      int anOutput2 = -1; // Temp input 2
      int anSBO     = -1; // Select Before Operate

      // Chack XML name
      if (!xml.getTagName().equals(XML_DOUBLE_OUTPUT)) {
        throw new IllegalArgumentException();
      }

      // Retrieve XML parameters
      anOutput1   = xml.getProperty(XML_OUTPUT1, -1);
      anOutput2   = xml.getProperty(XML_OUTPUT2, -1);
      anSBO       = xml.getProperty(XML_SBOID, -1);

      // All is ok, perform the changing
      output1   = anOutput1;
      output2   = anOutput2;
    } catch (Exception e) // Catch all kinds of exceptions
     {
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

    root.setTagName(XML_DOUBLE_OUTPUT);
    root.addProperty(XML_OUTPUT1, output1);
    root.addProperty(XML_OUTPUT2, output2);

    return root;
  }

  /**
   * @see com.itlity.luciol.database.DataElement#writeNewRecord(Record,
   *      boolean)
   */
  public synchronized void writeNewRecord(Record rec, boolean writeTimeStamp)
    throws DatabaseException {
    super.writeNewRecord(rec, writeTimeStamp);

    if (dOutput1 == null) {
      dOutput1 = (DigitalOutput) DatabaseApp.ACTIVE.getDataElement(output1);
    }

    if (dOutput2 == null) {
      dOutput2 = (DigitalOutput) DatabaseApp.ACTIVE.getDataElement(output2);
    }

    if (dOutput1 != null) {
      dOutput1.writeNewRecord(rec.copy(), writeTimeStamp);
    }

    if (dOutput2 != null) {
      dOutput2.writeNewRecord(rec.copy(), writeTimeStamp);
    }
  }
}
