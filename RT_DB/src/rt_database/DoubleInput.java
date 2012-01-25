/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rt_database;

import lib.xml.kXMLElement;

/**
 * <p>
 * A double input is a DataElement that checks the complementarity of two
 * DigitalInput elements.
 * </p>
 *
 * <p>
 * After a configurable amount of time, if both DigitalInput have the same
 * value, all their Records are marked as invalid.
 * </p>
 *
 * @author <a href="mailto:alexis.bietti@sysaware.com">Alexis BIETTI</a>
 */
public class DoubleInput extends DigitalInput {
  public static final String XML_INPUT1 = "input1";
  public static final String XML_INPUT2 = "input2";
  public static final String XML_TIME   = "time";

  //---------------------------------------------------------------------------
  // Static Fields
  //---------------------------------------------------------------------------
  static final Record softErrorRecord =
    Record.instanciateRecord(Record.REC_DIGITAL);

  static {
    softErrorRecord.quality = Record.Q_INVALID | Record.Q_BAD_REF;
  }

  //---------------------------------------------------------------------------
  // Attributes
  //---------------------------------------------------------------------------

  /** Database ID of the first input. */
  public int input1 = -1;

  /** Database ID of the second input. */
  public int input2 = -1;

  /**
   * The amount of time in milliseconds after which we can tell this
   * DoubleInput is in an illegal state.
   */
  public short waitTime = 0;

  /*
   * Following fields are here to avoid GC
   */
  DigitalInput in1  = null;
  DigitalInput in2  = null;
  Record       rec1 = null;
  Record       rec2 = null;

  //---------------------------------------------------------------------------
  // Constructor
  //---------------------------------------------------------------------------

  /**
   * Empty constructor is forbidden.
   */
  public DoubleInput() {
    super();
  }

  //---------------------------------------------------------------------------
  // Methods
  //---------------------------------------------------------------------------
  public String getName() {
    return "DOUBLE_INPUT";
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
       *
       */
  public DataElement copy() {
    DoubleInput copy = new DoubleInput();
    copy.input1     = this.input1;
    copy.input2     = this.input2;
    copy.waitTime   = this.waitTime;

    return copy;
  }

  /**
   * <p>
   * <b>Warning:</b> This method returns always <tt>null</tt>. It is to avoid
   * that anybody can write new Records in this element.
   * </p>
   *
   * <p>
   * Only the <tt>DoubleInput</tt> itself should write new Records.
   * </p>
   *
   * @return a Record compatible with the DoubleInput element type.
   */
  public Record instanciateRecord() {
    return Record.instanciateRecord(Record.REC_DIGITAL);
  }

  /**
   * See the general contract of this method in the class <tt>
   * com.itlity.luciol.database.DataElement </tt>
   */
  public String servlet() {
    return "dbi";
  }

  /**
   * For a double input, the last record is always recomputed on the fly. It
   * requests values from the two inputs matching inputs in the active
   * database and checks that they are complementary.
   *
   * @return a new Record object computed with the last values of the two
   *         inputs
   */
  public synchronized Record lastRecord() {
    try {
      in1   = (DigitalInput) DatabaseApp.ACTIVE.getDataElement(this.input1);
      in2   = (DigitalInput) DatabaseApp.ACTIVE.getDataElement(this.input2);

      rec1   = in1.lastRecord();
      rec2   = in2.lastRecord();

      Record newRecord = Record.instanciateRecord(Record.REC_DIGITAL);
      newRecord.setValue(rec1.booleanValue() != rec2.booleanValue());

      return newRecord;
    } catch (Exception e) {
    }

    return softErrorRecord;
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
      int          anInput1 = -1; // Temp input 1
      int          anInput2 = -1; // Temp input 2
      short        aTime    = 0; // Temp wait time
      DigitalInput tmpInput; // To perform id validation

      // Chack XML name
      if (!xml.getTagName().equals(XML_DOUBLE_INPUT)) {
        throw new IllegalArgumentException();
      }

      // Retrieve XML parameters
      anInput1   = xml.getProperty(XML_INPUT1, -1);
      anInput2   = xml.getProperty(XML_INPUT2, -1);
      aTime      = (short) xml.getProperty(XML_TIME, 0);

      // All is ok, perform the changing
      input1     = anInput1;
      input2     = anInput2;
      waitTime   = aTime;
    } catch (Exception e) // Catch all kinds of exceptions
     {
      e.printStackTrace();
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

    root.setTagName(XML_DOUBLE_INPUT);
    root.addProperty(XML_INPUT1, input1);
    root.addProperty(XML_INPUT2, input2);
    root.addProperty(XML_TIME, waitTime);

    return root;
  }

}
