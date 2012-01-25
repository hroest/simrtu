/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package lib.util;

import java.util.Date;

/**
 * <p>
 * Lightweight implementation of the gregorian calendar.
 * <p>
 * This calendar has some limitations compared to the standard Java
 * {@link java.util.Calendar} :
 * <ul>
 *   <li> The fields are limited (see the field list below)
 *   <li> There is no support for time zones
 *   <li> There is no support for summer / winter times
 * </ul>
 * <p>
 * An instance of this class can be accessed with the method
 * {@link #getInstance()} It can be useful to avoid constructing temporary
 * instances.
 * <p>
 * Based on an implementation by Patrice MOUSSET.
 *
 * @author <a href="mailto:patrice.mousset@sysaware.com">Patrice MOUSSET</a>
 * @author <a href="mailto:alexis.bietti@sysaware.com">Alexis BIETTI</a>
 */
public class Calendar {
  //---------------------------------------------------------------------------
  // Constants
  //---------------------------------------------------------------------------

  /*
 * Fields Identifiers.
 */
  public static final short YEAR         = 0;
  public static final short MONTH        = 1;
  public static final short DAY_OF_MONTH = 2;
  public static final short HOUR         = 3;
  public static final short MINUTE       = 4;
  public static final short SECOND       = 5;
  public static final short MILLIS       = 6;
  public static final short DAY_OF_WEEK  = 7;

  /*
 * Month identifiers
 */
  public static final int JANUARY   = 1;
  public static final int FEBRUARY  = 2;
  public static final int MARCH     = 3;
  public static final int APRIL     = 4;
  public static final int MAY       = 5;
  public static final int JUNE      = 6;
  public static final int JULY      = 7;
  public static final int AUGUST    = 8;
  public static final int SEPTEMBER = 9;
  public static final int OCTOBER   = 10;
  public static final int NOVEMBER  = 11;
  public static final int DECEMBER  = 12;

  /*
 * Days identifiers
 */
  public static final int MONDAY    = 0;
  public static final int TUESDAY   = 1;
  public static final int WEDNESDAY = 2;
  public static final int THURSDAY  = 3;
  public static final int FRIDAY    = 4;
  public static final int SATURDAY  = 5;
  public static final int SUNDAY    = 6;

  /*
 * Time constants in milliseconds
 */

  /** One second expressed in number of milliseconds */
  public static final int ONE_SECOND = 1000;

  /** One minute expressed in number of milliseconds */
  public static final int ONE_MINUTE = 60000;

  /** One hour expressed in number of milliseconds */
  public static final int ONE_HOUR = 3600000;

  /**
 * Number of millis between 01/01/00@00:00:00 and 01/01/1970@00:00:00
 */
  private static final long ABSOLUTE_TIME_REFERENCE_MILLI = 12219379200000L;

  /**
 * Short names of the month.
 */
  private static final String[] NAMES_OF_MONTHS =
    new String[] {
      "Jan", "Feb", "Mar", "Avr", "May", "Jun", "Jul", "Aug", "Sep", "Oct",
      "Nov", "Dec"
    };

  /**
 * Short names of the days.
 */
  private static final String[] NAMES_OF_DAYS =
    new String[] { "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun" };

  /**
 * This instance has a private access. It can be shared by many applications
 * and thus save CPU and memory usage.
 * <p>
 * A reference to this instance is returned by the static method
 * <tt> getInstance() </tt>.
 */
  private static Calendar INSTANCE = null;

  //---------------------------------------------------------------------------
  // Attributes
  //---------------------------------------------------------------------------

  /**
 * The currently set time for this calendar, expressed in milliseconds after
 * January 1, 1970, 0:00:00 GMT.
 */
  private long time = 0;

  /**
 * The fields of this Calendar. From YEAR to MILLIS.
 */
  private int[] fields = new int[7];

  /**
 * This field contains the day of week.
 */
  private int dayOfWeek = 0;

  //---------------------------------------------------------------------------
  // Constructors
  //---------------------------------------------------------------------------

  /**
 * Constructs a new Calendar set to the current real time.
 *
 * @see com.itlity.util.Clock
 */
  public Calendar() {
    this(System.currentTimeMillis());
  }

  /**
 * Constructs a new Calendar set to the given time.
 *
 * @param time
 */
  public Calendar(long time) {
    this.time = time;
    computeFields();
  }

  /**
 * Constructs a new Calendar set to the given date.
 *
 * @param date
 */
  public Calendar(Date date) {
    this.time = date.getTime();
    computeFields();
  }

  /**
 * Constructs a new Calendar with the given fields.
 *
 * @param year    a year
 * @param month   a month, 0 to 11
 * @param day     a day
 * @param hour    an hour
 * @param minute  a minute
 * @param second  a second
 *
 * @throws IllegalArgumentException
 */
  public Calendar(
    int year, int month, int day, int hour, int minute, int second, int millis)
    throws IllegalArgumentException {
    fields[YEAR]           = year;
    fields[MONTH]          = month;
    fields[DAY_OF_MONTH]   = day;
    fields[HOUR]           = hour;
    fields[MINUTE]         = minute;
    fields[SECOND]         = second;
    fields[MILLIS]         = millis;

    computeTime();
  }

  /*
 * Precondition: 0 <= i < 10**digits
 */
  private static String int2digits(int val, int digits) {
    String ans = Integer.toString(val);

    while (ans.length() < digits) {
      ans = "0" + ans;
    }

    return ans;
  }

  /**
 * This method returns an instance of {@link Calendar} that is shared.
 * <p>
 * If this instance is to be used by several {@link java.lang.Thread}s, then
 * all access to this instance should be synchronized, in order to avoid
 * corrupting this instance.
 *
 * @return an instance of {@link Calendar}
 */
  public static Calendar getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new Calendar();
    }

    return INSTANCE;
  }

  //------------------------------------------------------------------------------
  // * Public Methods
  //------------------------------------------------------------------------------

  /**
 * Return the currently set time for this calendar, expressed in
 * milliseconds after January 1, 1970, 0:00:00 GMT.
 *
 * @return the currently set time for this calendar
 */
  public long getTime() {
    return time;
  }

  /**
 * Return the value of the field identified by <tt>field</tt>.
 *
 * @param field
 *
 * @return the value of the field identified by <tt>field</tt>.
 *
 * @see #YEAR
 * @see #MONTH
 * @see #DAY_OF_MONTH
 * @see #HOUR
 * @see #MINUTE
 * @see #SECOND
 *
 * @throws ArrayIndexOutOfBoundsException
 */
  public int get(int field) {
    if (field == DAY_OF_WEEK) {
      return this.dayOfWeek;
    }

    return fields[field];
  }

  /**
 * Modify one of this Calendar's fields. The day of week can not be set with
 * this method.
 *
 * @param field   a field identifier
 * @param value   a new value for the field
 *
 * @throws ArrayIndexOutOfBoundsException
 */
  public void setField(int field, int value) {
    fields[field] = value;
    computeTime();
  }

  /**
 * Modify the time of this calendar. Fields are computed again.
 *
 * @param time   a new time in millis
 */
  public void setTime(long time) {
    this.time = time;
    computeFields();
  }

  /**
 * Return a long String representation of this Calendar.
 *
 * @return a String representation of this Calendar.
 */
  public String toString() {
    StringBuffer buf = new StringBuffer();

    buf.append(NAMES_OF_DAYS[dayOfWeek]);
    buf.append(' ');
    buf.append(fields[DAY_OF_MONTH]);
    buf.append(' ');
    buf.append(NAMES_OF_MONTHS[fields[MONTH] - 1]);
    buf.append(' ');
    buf.append(fields[YEAR]);
    buf.append(' ');

    buf.append(int2digits(fields[HOUR], 2));
    buf.append(':');
    buf.append(int2digits(fields[MINUTE], 2));
    buf.append(':');
    buf.append(int2digits(fields[SECOND], 2));
    buf.append(':');
    buf.append(int2digits(fields[MILLIS], 3));
    buf.append(" UTC");

    return buf.toString();
  }

  /**
 * Return a short String representation of this Calendar.
 *
 * @return a String representation of this Calendar.
 */
  public String toShortString() {
    StringBuffer buf = new StringBuffer();

    buf.append(int2digits(fields[DAY_OF_MONTH], 2));
    buf.append(' ');
    buf.append(NAMES_OF_MONTHS[fields[MONTH] - 1]);
    buf.append(' ');
    buf.append(fields[YEAR]);
    buf.append(' ');

    buf.append(int2digits(fields[HOUR], 2));
    buf.append(':');
    buf.append(int2digits(fields[MINUTE], 2));
    buf.append(':');
    buf.append(int2digits(fields[SECOND], 2));
    buf.append(':');
    buf.append(int2digits(fields[MILLIS], 3));
    buf.append(" UTC");

    return buf.toString();
  }

  //------------------------------------------------------------------------------
  // * Private Methods
  //------------------------------------------------------------------------------

  /**
 * Compute the fields from the absolute time.
 */
  private void computeFields() {
    long aGregorian;
    long aYear;
    long aMonth;
    long aDay;
    long millis  = time + ABSOLUTE_TIME_REFERENCE_MILLI;
    int  aMillis = (int) (millis % 1000L);

    aGregorian   = (millis / 86400000L);
    dayOfWeek    = ((int) aGregorian + THURSDAY) % 7;
    aGregorian += 578041L;
    aYear        = ((aGregorian << 2) - 1L) / 146097L;
    aGregorian   = ((aGregorian << 2) - 1L) - (aYear * 146097L);
    aDay         = (aGregorian >> 2);
    aGregorian   = ((aDay << 2) + 3L) / 1461L;
    aDay         = ((aDay << 2) + 3L) - (aGregorian * 1461L);
    aDay         = (aDay + 4L) >> 2;
    aMonth       = ((aDay * 5L) - 3L) / 153L;
    aDay         = ((aDay * 5L) - 3L) - (aMonth * 153L);
    aDay         = (aDay + 5L) / 5L;
    aYear        = (aYear * 100L) + aGregorian;

    if (aMonth < 10L) {
      aMonth = aMonth + 3L;
    } else {
      aMonth   = aMonth - 9L;
      aYear    = aYear + 1L;
    }

    fields[YEAR]           = (int) aYear;
    fields[MONTH]          = (int) aMonth;
    fields[DAY_OF_MONTH]   = (int) aDay;

    millis   = (millis / 1000L) % 86400L;

    // millis contains the number of hours in seconds now.
    fields[HOUR]     = (int) (millis / 3600L);
    fields[MINUTE]   = (int) ((millis % 3600L) / 60L);
    fields[SECOND]   = (int) (millis % 60L);
    fields[MILLIS]   = aMillis;
  }

  /**
 * Compute the absolute time from the fields.
 */
  private void computeTime() {
    int year  = fields[YEAR];
    int month = fields[MONTH];

    if (month > 2) {
      month = month - 3;
    }
    else {
      month   = month + 9;
      year    = year - 1;
    }

    int century = year / 100;
    year   = year - (century * 100);

    // Get now a time as a continuous long integer - this algorithm was
    // used to calculate the ABSOLUTE_TIME_REFERENCE_DAY milli just below
    // represents a number of days in days.
    time =
      (long) ((((((long) century) * 146097L) >> 2)
      + ((((long) year) * 1461L) >> 2) + (((((long) month) * 153L) + 2L) / 5L)
      + ((long) fields[DAY_OF_MONTH])) - 578041L);

    // Get the number of days converted in milli-seconds.
    time   = time * 86400000L;

    // Add the sub-day time values i.e. anHour, aMinute, aSecond aMilli
    time =
      time + (((long) fields[HOUR]) * 3600000L)
      + (((long) fields[MINUTE]) * 60000L) + (((long) fields[SECOND]) * 1000L)
      + ((long) fields[MILLIS]);

    // Substract the ABSOLUTE_REFERENCE_TIME_MILLI to get
    // the relative time w.r.t. ClockObject fixed absolute time.
    time -= ABSOLUTE_TIME_REFERENCE_MILLI;
  }

  /**
 * For testing.
 */
  public static void main(String[] args) {
    // Our calendar
    Calendar c = new Calendar();
    System.out.println(c);

    // Java reference implementation
    java.util.Calendar cc = java.util.Calendar.getInstance();
    System.out.println(cc);
  }
}
