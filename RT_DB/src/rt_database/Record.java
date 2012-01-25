/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rt_database;

import java.util.Vector;

import lib.math.Float;

//-----------------------------------------------------------------------------
// Classes
//-----------------------------------------------------------------------------

/**
 * Record is an abstract class that represent a unit of information in the
 * database.
 *
 * <p>
 * This may be a measure, a status, or a command state. A record has a time
 * stamp. A record also have some customizable flags that can contain
 * information about the quality of the information contained in this record.
 * </p>
 *
 * @author <a href="mailto:alexis.bietti@sysaware.com"> Alexis BIETTI </a>
 */
public abstract class Record {
  //---------------------------------------------------------------------------
  // Constants
  //---------------------------------------------------------------------------

  /*
   * Types of Records.
   */
  protected static final byte REC_DIGITAL = 0;
  protected static final byte REC_ANALOG  = 1;
  protected static final byte REC_INTEGER = 2;
  protected static final byte REC_FLOAT   = 3;

  /*
   * Quality flags defined in UCA2
   */


  public static final short Q_VALID            = 0x0000;
  public static final short Q_INVALID          = 0x0002;
  public static final short Q_COMM_FAIL        = 0x0004;
  public static final short Q_FORCED           = 0x0008;
  public static final short Q_OVER_RANGE       = 0x0010;
  public static final short Q_BAD_REF          = 0x0020;

  // user defined
  public static final short Q_WAIT_FOR_CONNECT = 0x0030;
  public static final short Q_VALID_COMM       = 0x0040;
  public static final short Q_INITIAL          = 0x0050;
  public static final short Q_TIMEOUT          = 0x0060;
  public static final short Q_COUNTER          = 0x0070;
  public static final short Q_NOT_REACHABLE    = 0x0080;

  /*
   * Quality flags defined internally for Luciol applications
   */
  public static final short Q_NOT_COMPL           = 0x0100;
  public static final short Q_NOT_REFRESHED       = 0x0200;
  public static final short Q_FUNCT_HARDW_FAILURE = 0x0400;
  public static final short Q_DENIAL_OF_SERVICE   = 0x0800;

  /*
   * Quality flags reserved for DNP outputs
   */

  // public static final short Q_DNP_COMMAND = 1 << 9;
  //---------------------------------------------------------------------------
  // Attributes
  //---------------------------------------------------------------------------

  /*
   * Defines, if a record represents a remote event
   */

  public boolean remote_event = false;
  public boolean spontaneous  = false;
  public boolean termination  = false;
  public boolean localevent   = false;
  public boolean initial      = true;

  /**
   * <p>
   * Quality flags of this Record. Users should take care before modifying
   * this value.
   * </p>
   *
   * <p></p>
   */
  public short quality = 0;

  /**
   * Time stamp of this record. Users should take care before modifying this
   * value.
   */
  public long timeStamp = 0L;

  //---------------------------------------------------------------------------
  // Methods
  //---------------------------------------------------------------------------

  /**
   * Get the time stamp of this record.
   *
   * @return the time stamp of this record.
   */
  public long getTimeStamp() {
    return timeStamp;
  }

  /**
   * Set the time stamp of this record to the current system time.
   */
  public void setTimeStamp() {
    timeStamp = System.currentTimeMillis();
  }

  /**
   * Set the time stamp of this record to the given system time.
   *
   * @param aTimeStamp a system time.
   */
  public void setTimeStamp(long aTimeStamp) {
    timeStamp = aTimeStamp;
  }

  public void setRemoteEvent()
  {
      this.remote_event=true;
  }

  public void setSpont()
  {
      this.remote_event= false;
      this.spontaneous = true;
      this.termination = false;
      this.localevent  = false;
  }

  public void setTerm()
  {
      this.termination = true;
  }

  public void setLocal()
  {
      this.localevent  = true;
      this.spontaneous = false;
  }

  /**
   * Get the quality flags of this record.
   *
   * @return the quality flags of this record.
   */
  public short getQuality() {
    return quality;
  }

  /**
   * Set the quality flags of this record to the given value.
   *
   * @param aQuality a new quality flags for this record.
   */
  public void setQuality(short aQuality) {
    quality = aQuality;
  }

  /**
   * Test if this record is valid. A valid record has all quality flags set to
   * zero.
   *
   * @return <code> true </code> if this record is valid.
   */
  public boolean isValid() {
    return ((quality == Record.Q_VALID) || (quality == Record.Q_VALID_COMM));
  }

  //---------------------------------------------------------------------------
  // Abstract Methods
  //---------------------------------------------------------------------------

  /**
   * Get the value of this record cast as a boolean.
   *
   * @return the value of this record cast as a boolean.
   */
  public abstract boolean booleanValue();

  /**
   * Get the value of this record cast as an integer.
   *
   * @return the value of this record cast as an integer.
   */
  public abstract int intValue();

  /**
   * Get the value of this record cast as a fixed point decimal.
   *
   * @return the value of this record cast as a fixed point decimal.
   *
   * @see com.itlity.math.Math32FP
   */
  public abstract int fixedPointValue();

  /**
   * Get the value of this record cast as a {@link java.lang.String}
   *
   * @return the value of this record cast as a {@link java.lang.String}.
   */
  public abstract String stringValue();

  /**
   * Get the value of this record as an {@link java.lang.Object}
   *
   * <p>
   * The class of this object depends on the type of this record.
   * </p>
   *
   * <p>
   * Implementation note: this method will become useful when we will have
   * complex object types.
   * </p>
   *
   * @return the value of this record as an Object.
   */
  public abstract Object objectValue();

  /**
   * Get the value of this record as a float.
   *
   * @return the value of this record cast as a String.
   */
  public abstract float floatValue();

  /**
   * Change the value of this record with the given value.
   *
   * <p>
   * The <code> newValue </code> may be accepted or refused
   * </p>
   *
   * @param newValue the new value for this Record
   *
   * @throws DatabaseException if the class of newValue is not compliant.
   */
  public abstract void setValue(Object newValue);

  /**
   * Try and set the value of this Record with the given value.
   *
   * @param newValue the new value for this Record
   *
   * @throws DatabaseException if the class of newValue is not compliant.
   */
  public void setValue(boolean newValue) {
    setValue(new Boolean(newValue));
  }

  /**
   * Try and set the value of this Record with the given value.
   *
   * @param newValue the new value for this Record
   *
   * @throws DatabaseException if the class of newValue is not compliant.
   */
  public void setValue(int newValue) {
    setValue(new Integer(newValue));
  }

  /**
   * Try and set the value of this Record with the given value.
   *
   * @param newValue the new value for this Record
   *
   * @throws DatabaseException if the class of newValue is not compliant.
   */
  public void setValue(float newValue) {
    setValue(new Float(newValue));
  }

  /**
   * Allocate a new Record object and copy all our attributes in this new
   * Record
   *
   * @return a newly allocated Record
   */
  public abstract Record copy();

  /**
   * Instanciate a new Record compliant with the specified type.
   *
   * @see #REC_DIGITAL
   * @see #REC_ANALOG
   * @see #REC_INTEGER
   * @see #REC_FLOAT
   */
  protected static Record instanciateRecord(byte kind)
    throws IllegalArgumentException {
    if (kind == REC_DIGITAL) {
      return new DigitalRecord();
    }

    if (kind == REC_ANALOG) {
      return new AnalogRecord();
    }

    if (kind == REC_INTEGER) {
      return new IntegerRecord();
    }

    if (kind == REC_FLOAT) {
      return new FloatRecord();
    } else {
      throw new IllegalArgumentException();
    }
  }

  /**
   * Get a string representation of this Record.
   */
    @Override
  public String toString() {
    return "{Record value='" + stringValue() + "',quality="
    + Integer.toBinaryString(quality) + ",date=" + timeStamp + "}";
  }

  /**
   * This method takes all quality information contained in the quality field
   * and compute it as a set of printable strings.
   *
   * @return a vector containing all quality messages.
   */
  public Vector qualityMessages() {
    Vector aVector = new Vector();

    // Add a message for each bit set.
    if ((quality & Q_INVALID) != 0) {
      aVector.addElement("Invalid");
    }

    if ((quality & Q_BAD_REF) != 0) {
      aVector.addElement("Bad reference");
    }

    if ((quality & Q_COMM_FAIL) != 0) {
      aVector.addElement("Communication Failed");
    }

    if ((quality & Q_FORCED) != 0) {
      aVector.addElement("Value forced");
    }

    if ((quality & Q_OVER_RANGE) != 0) {
      aVector.addElement("Over range");
    }

    if ((quality & Q_NOT_COMPL) != 0) {
      aVector.addElement("Not complementary");
    }

    aVector.trimToSize();

    return aVector;
  }
}
