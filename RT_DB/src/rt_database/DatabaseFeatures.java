/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rt_database;

/**
 * Constants and features of the database.
 *
 * @author Alexis BIETTI
 */
public interface DatabaseFeatures {
  /** Send or don't send debug info on <tt>System.out</tt> */
  public static final boolean DEBUG = true;

  //---------------------------------------------------------------------------
  // Array sizes
  //---------------------------------------------------------------------------

  /** Default size of history objects. */
  public static final int HIST_DEF_SIZE = 10;

  /**
       *
       */
  public static final int DESC_SIZE = 32;

  //---------------------------------------------------------------------------
  // Data types
  //---------------------------------------------------------------------------
  //  public static final byte DATA_BOOL_INPUT  = 0;
  //  public static final byte DATA_BOOL_OUTPUT = 1;
  //  public static final byte DATA_NUM_INPUT   = 2;
  //  public static final byte DATA_ANA_INPUT   = 3;
  //  public static final byte DATA_ANA_OUTPUT  = 4;
  //---------------------------------------------------------------------------
  // Value classes
  //---------------------------------------------------------------------------
  public static final Class VALUE_BOOLEAN = new Boolean(true).getClass();
  public static final Class VALUE_ANALOG  = new Integer(0).getClass();
  public static final Class VALUE_INTEGER = new Integer(0).getClass();

  //---------------------------------------------------------------------------
  // Quality flags (derived from UCA 2 GOMSFE)
  //---------------------------------------------------------------------------
  public static final byte QUAL_INVALID   = 0x02;
  public static final byte QUAL_COMM_FAIL = 0x04;
  public static final byte QUAL_FORCED    = 0x08;

  /**
       *
       */
  public static final byte QUAL_OVER_RANGE = 0x0f;

  /** Only applicable to Analog values. */
  public static final byte QUAL_BAD_REF = 0x10;
}
