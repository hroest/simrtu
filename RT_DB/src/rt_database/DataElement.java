/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rt_database;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import lib.math.Float;
import lib.interfaces.xml.XMLInterface;
import lib.xml.kXMLElement;
import lib.util.CircularBuffer;

//-----------------------------------------------------------------------------
// Class
//-----------------------------------------------------------------------------

/**
 * <p>
 * A DataElement is a {@link Database} unit, with an history of some {@link
 * Record}'s
 * </p>
 *
 * @author <a href="mailto:alexis.bietti@sysaware.com">Alexis BIETTI</a>
 */
public abstract class DataElement implements XMLInterface {
  //---------------------------------------------------------------------------
  // Constants
  //---------------------------------------------------------------------------
  public static final IllegalArgumentException ILLEGAL_ARGUMENT_EXCEPTION =
    new IllegalArgumentException();

  /*
 * XML names
 */
  public static final String XML_DIGITAL_INPUT         = "DigitalInput";
  public static final String XML_DIGITAL_OUTPUT        = "DigitalOutput";
  public static final String XML_ANALOG_INPUT          = "AnalogInput";
  public static final String XML_ANALOG_OUTPUT         = "AnalogOutput";
  public static final String XML_DOUBLE_INPUT          = "DoubleInput";
  public static final String XML_DOUBLE_OUTPUT         = "DoubleOutput";
  public static final String XML_INTEGER_INPUT         = "IntegerInput";
  public static final String XML_SELECT_BEFORE_OPERATE = "SelectBeforeOperate";
  public static final String XML_QUICK                 = "quick";
  public static final String XML_SLOW                  = "slow";
  public static final String XML_LOG_SIZE              = "logSize";
  public static final String XML_DESC                  = "desc";
  public static final String XML_ID                    = "id";
  public static final String XML_LOGIC                 = "logic";

  /** Default value of the record history. */
  public static final int DEF_HISTORY_SIZE = 10;

  /** Maximum length in characters of the description strings. */
  public static final int MAX_DESC_SIZE = 32;

  /** Default history size for digitals. */
  public static final int DIGITAL_DEFAULT_HISTORY_SIZE = 10;

  /** Default history size for analogs. */
  public static final int ANALOG_DEFAULT_HISTORY_SIZE = 3;

  /** Default value for digital data. */
  static final Boolean DEFAULT_DIGITAL = new Boolean(true);

  /** Default value for analog data. */
  static final Float DEFAULT_ANALOG = new Float(0F);

  /** Default value for integer data. */
  static final Integer DEFAULT_INTEGER = new Integer(0);

  //---------------------------------------------------------------------------
  // Constants for digital outputs
  //---------------------------------------------------------------------------

  /** Quick access to the data element classes. */
  public static final Hashtable CLASSES = new Hashtable();

  /**
   * Fill the CLASSES table.
   */
  static {
    CLASSES.put(XML_DIGITAL_INPUT, new DigitalInput().getClass());
    CLASSES.put(XML_DIGITAL_OUTPUT, new DigitalOutput().getClass());
    CLASSES.put(XML_ANALOG_INPUT, new AnalogInput().getClass());
    CLASSES.put(XML_ANALOG_OUTPUT, new AnalogOutput().getClass());
    CLASSES.put(XML_INTEGER_INPUT, new IntegerInput().getClass());
    //CLASSES.put(XML_DOUBLE_INPUT, new DoubleInput().getClass());
    //CLASSES.put(XML_DOUBLE_OUTPUT, new DoubleOutput().getClass());
    //CLASSES.put(XML_DOUBLE_OUTPUT, new SelectBeforeOperate().getClass());
  }

  //---------------------------------------------------------------------------
  // Constants for double inputs / double outputs
  //---------------------------------------------------------------------------

  /*
 * Double I/Os speed.
 */
  public static final byte DOUBLE_QUICK = 0;
  public static final byte DOUBLE_SLOW = 0;

  /*
 * Double I/Os speed labels.
 */
  public static final String[] SPEEDS = new String[] { XML_QUICK, XML_SLOW };

  //---------------------------------------------------------------------------
  // Attributes
  //---------------------------------------------------------------------------

  /**
   * Database identifier of this DataElement.
   *
   * <p>
   * Access to this attribute is public to improve performances, but users
   * must never modify this value themselves.
   * </p>
   */
  public int databaseID = -1;

  /**
   * A short description of this DataElement.
   *
   * <p>
   * Access to this attribute is public to improve performances, but users
   * must never modify this value themselves.
   * </p>
   */
  public String description = null;

  /**
   * Contains the DataEventListeners that want to be notified by this
   * DataElement
   */
  private Vector listeners = null;

  /**
   * This field contains a reference to an object that is called to write a
   * new Record on request.
   */
  private DataRequestListener owner = null;

  /** History of records. */
  private CircularBuffer records = null;

  //---------------------------------------------------------------------------
  // Constructor
  //---------------------------------------------------------------------------

  /**
   * Construct a new data element with a default history size.
   *
   * @see #getDefaultLogSize()
   */
  public DataElement() {
    super();
    records = new CircularBuffer(getDefaultLogSize());
  }

  /**
   * Construct a new data element with the specified history size.
   *
   * @param historySize a strictly positive integer.
   */
  public DataElement(int historySize) {
    super();
    records = new CircularBuffer(historySize);
  }

  //---------------------------------------------------------------------------
  // Methods
  //---------------------------------------------------------------------------

  /**
   * Return a common name for this data element.
   */
  public abstract String getName();

  /**
   * Return a default value for this {@link DataElement}.
   *
   * @return a default value for this {@link DataElement}.
   */
  public abstract Object defaultValue();

  /**
   * Instanciate a new Record compliant with this DataElement.
   *
   * @return a newly allocated Record object.
   */
  public abstract Record instanciateRecord();

  /**
   * Get the name of the servlet used to configure this DataElement.
   * <p>
   * Implementation note: this feature is not used in the current implementation
   * of the configurator.
   *
   * @return the name of the servlet used to configure this DataElement.
   */
  public abstract String servlet();

  /**
   * Return a default size for the history record of this element.
   * <p>
   * Subclasses must provide a positive integer that fits the needs of the
   * particular type of data element. The elements that do not need to store
   * histories of data should return 1.
   *
   * @return a strictly positive integer value.
   */
  public abstract int getDefaultLogSize();

  //---------------------------------------------------------------------------
  // Accessors / modifiers
  //---------------------------------------------------------------------------

  /**
   * Return the database ID of this DataElement.
   *
   * @return the database ID of this DataElement.
   */
  public int getID() {
    return this.databaseID;
  }

  /**
   * Get the description field of this DataElement.
   *
   * @return description field of this DataElement.
   */
  public String getDescription() {
    return this.description;
  }

  /**
   * Get the number of Records that this DataElement can contain.
   *
   * @return the number of Records that this DataElement can contain.
   */
  public int getLogSize() {
    return this.records.capacity();
  }

  /**
   * Set the description field of this DataElement.
   *
   * @param aDescription a new description for this DataElement.
   */
  public void setDescription(String aDescription) {
    this.description = aDescription;
  }

  //---------------------------------------------------------------------------
  // Other methods
  //---------------------------------------------------------------------------

  /**
   * This method writes a new invalid {@link Record} in the data history of
   * this element.
   * <p>
   * This method may be called when the object is created.
   */
  public void setInvalid() {
    Record rec = instanciateRecord();
    rec.setValue(defaultValue());
    rec.quality = Record.Q_INVALID | Record.Q_FORCED;
    writeNewRecord(rec);
  }

  /**
   * Set who is the owner of this data element. The owner is responsible to
   * refresh the value on request.
   *
   * @param owner the new owner of this DataElement.
   *
   * @see DataRequestListener
   */
  public void setDataRequestListener(DataRequestListener owner) {
    this.owner = owner;
  }

  /**
   * Removes a request listener. Checks first if listener passed as argument
   * is effectively the owner of this data element.
   *
   * @see DataRequestListener
   */
  public void removeDataRequestListener(DataRequestListener listener) {
    if (owner != null) {
      if (owner == listener) {
        this.owner = null;
      }
    }
  }

  /**
   * Add a DataEventListener to this DataElement.
   *
   * @param listener a DataEventListener
   */
  public void addDataEventListener(DataEventListener listener) {
    if (listeners == null) {
      listeners = new Vector(1, 1); // Init Vector if none
    }

    if (!listeners.contains(listener)) {
      listeners.addElement(listener);
    }
  }

  /**
   * Remove the specified DataEventListener of this DataElement.
   *
   * @param listener the DataEventListener to remove.
   */
  public void removeDataEventListener(DataEventListener listener) {
    if (listeners == null) {
      return;
    }

    listeners.removeElement(listener);

    if (listeners.size() == 0) // Remove Vector if empty
     {
      listeners = null;
    }
  }

  /**
   * Notify all the Listeners that are monitoring this DataElement.
   */
  protected void notifyAllListeners() {
    if (listeners == null) {
      return;
    }

    for (Enumeration e = listeners.elements(); e.hasMoreElements();) {
        try {
            ((DataEventListener) e.nextElement()).dataEventPerformed(this);
        } catch (Exception exception) {
            //lost handle - can't do anything...
        }
    }
  }

  /**
   * This method returns the last Record written to this DataElement.
   *
   * @return the most recent Record, or <tt>null</tt> if no Record is
   *         available.
   */
  public Record lastRecord() {
    return (Record) records.lastElement();
  }

  /**
   * Refresh this data element with a new record before returning the last
   * record.
   *
   * <p>
   * If no writer is set, this method has the same effect than lastRecord().
   * </p>
   *
   * @return the most recent Record, or <tt>null</tt> if no Record is
   *         available.
   */
  public Record requestRefreshedRecord() {
    // Do we have an owner ?
    if (owner != null) {
      // Yes, inform it that we want a refreshed record
      owner.dataRequestPerformed(this);
    }

    return lastRecord();
  }

  /**
   * This method returns an Enumeration of all the Record in this DataElement.
   *
   * @return an Enumeration of all the Record in this DataElement.
   */
  public Enumeration allRecords() {
    return records.elements(true);
  }

  /**
   * Write a new Record in the History.
   *
   * @param rec a Record to write in the history.
   *
   * @throws DatabaseException if the record is not compliant
   */
  public synchronized void writeNewRecord(Record rec) throws DatabaseException {
    writeNewRecord(rec, true);
  }

  /**
   * Write a new Record in the History.
   *
   * @param rec a Record to write in the history.
   * @param writeTimeStamp whether we must write a time stamp ourselves or not
   *
   * @throws DatabaseException if the record is not compliant
   */
  public synchronized void writeNewRecord(Record rec, boolean writeTimeStamp)
    throws DatabaseException {
    if (writeTimeStamp) {
      rec.timeStamp = System.currentTimeMillis();
    }

    // Add the new record in the history and notify listeners
    records.addElement(rec);

    // This method returns the remaining size in the queue, or the value
    // -1 if the element was not enqueued.
    // TO DO : save somewhere the number of unqueued elements.
    if (this.listeners != null) {
      DatabaseApp.toNotify.enqueue(this);
    }
  }

  /**
   * This method clears all the history
   */
  public synchronized void clearAllRecords() {
    records.clear();
  }

  /**
   * @see java.lang.Object#toString()
   */
    @Override
  public final String toString() {
    String list="Listeners: ";

    if (listeners != null)
      {
          for (int ii=0; ii < listeners.size(); ii++)
          {
              list = list + listeners.get(ii).toString() + ", ";
          }
      }

      return getName() + "[id=" + databaseID + ",listeners="
    + ((listeners == null) ? 0 : listeners.size()) + "," + list +",desc=" + description
    + ']';
  }

  //---------------------------------------------------------------------------
  // Interface XMLInterface
  //---------------------------------------------------------------------------

  /**
   * See the general contract of this method in the interface {@link
   * com.itlity.io.XMLInterface}
   */
    @Override
  public void inputFromXML(kXMLElement xml) throws IllegalArgumentException {
    int logSize = xml.getProperty(XML_LOG_SIZE, getDefaultLogSize());

    // Copy attributes
    description   = xml.getProperty(XML_DESC, "");
    databaseID    = xml.getIntProperty(XML_ID);
    records       = new CircularBuffer(logSize);

    // Erase listeners and owner
    listeners   = null;
    owner       = null;

    // Write a new invalid record
    setInvalid();
  }

  /**
   * See the general contract of this method in the interface {@link
   * com.itlity.io.XMLInterface}
   */
  public kXMLElement outputToXML() {
    kXMLElement root = new kXMLElement();

    root.addProperty(XML_DESC, description);
    root.addProperty(XML_ID, databaseID);

    if (this.records.capacity() != getDefaultLogSize()) {
      root.addProperty(XML_LOG_SIZE, this.records.capacity());
    }

    return root;
  }
}
