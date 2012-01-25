/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package lib.text;

import java.util.*;
import lib.util.TimeStamper;

public abstract class DateFormat
{
    private static class HostDateFormat extends DateFormat
    {

        protected String format0(Date date)
        {
            calendar.setTime(date);
            int i = calendar.get(1);
            int j = calendar.get(2);
            int k = calendar.get(5);
            int l = calendar.get(11);
            int i1 = calendar.get(12);
            int j1 = calendar.get(13);
            boolean flag = calendar.get(9) == 1;
            String s = TimeStamper.getTimeZoneName(calendar.getTimeZone(), date);
            StringBuffer stringbuffer = new StringBuffer(50 + s.length());
            writeDateString(stringbuffer, i, j, k);
            writeTimeString(stringbuffer, l, i1, j1, flag, s);
            return stringbuffer.toString();
        }

        private void writeDateString(StringBuffer stringbuffer, int i, int j, int k)
        {
            stringbuffer.append(monthStrings[j]);
            if(k >= 10)
                stringbuffer.append((char)(k / 10 + 48));
            stringbuffer.append((char)(k % 10 + 48));
            stringbuffer.append(dayYearSeparatorString);
            if(dateStyle == 1)
            {
                int l = (i / 100) % 10;
                stringbuffer.append((char)(i / 1000 + 48));
                stringbuffer.append((char)(l + 48));
            }
            int i1 = (i / 10) % 10;
            stringbuffer.append((char)(i1 + 48));
            stringbuffer.append((char)(i % 10 + 48));
            stringbuffer.append(' ');
        }

        private void writeTimeString(StringBuffer stringbuffer, int i, int j, int k, boolean flag, String s)
        {
            if(i == 0 || i == 12)
                stringbuffer.append("12:");
            else
            if(i == 11 || i == 23)
                stringbuffer.append("11:");
            else
            if(i == 10 || i == 22)
            {
                stringbuffer.append("10:");
            } else
            {
                i %= 12;
                stringbuffer.append((char)(i % 10 + 48));
                stringbuffer.append(':');
            }
            stringbuffer.append((char)(j / 10 + 48));
            stringbuffer.append((char)(j % 10 + 48));
            if(timeStyle == 1)
            {
                stringbuffer.append(':');
                stringbuffer.append((char)(k / 10 + 48));
                stringbuffer.append((char)(k % 10 + 48));
                if(flag)
                    stringbuffer.append(" PM ").append(s);
                else
                    stringbuffer.append(" AM ").append(s);
            } else
            if(flag)
                stringbuffer.append(" PM");
            else
                stringbuffer.append(" AM");
        }

        private static final String monthNames[] = {
            "January ", "February ", "March ", "April ", "May ", "June ", "July ", "August ", "September ", "October ",
            "November ", "December "
        };
        private static final String monthNumbers[] = {
            "1/", "2/", "3/", "4/", "5/", "6/", "7/", "8/", "9/", "10/",
            "11/", "12/"
        };
        private static final int MIN_DATE_TIME_STRING_SIZE = 50;
        private int dateStyle;
        private int timeStyle;
        private String monthStrings[];
        private String dayYearSeparatorString;


        private HostDateFormat(int i, int j)
        {
            if(i != 1 && i != 3)
                throw new IllegalArgumentException("Only DateFormat.LONG and DateFormat.SHORT date formats supported");
            if(j != 1 && j != 3)
                throw new IllegalArgumentException("Only DateFormat.LONG and DateFormat.SHORT time formats supported");
            dateStyle = i;
            timeStyle = j;
            if(i == 1)
            {
                monthStrings = monthNames;
                dayYearSeparatorString = ", ";
            } else
            {
                monthStrings = monthNumbers;
                dayYearSeparatorString = "/";
            }
            calendar = Calendar.getInstance(TimeStamper.getTimeZoneToUse());
        }

    }


    public static final DateFormat getDateTimeInstance(int i, int j)
    {
        return new HostDateFormat(i, j);
    }

    public void setTimeZone(TimeZone timezone)
    {
        calendar.setTimeZone(timezone);
    }

    public TimeZone getTimeZone()
    {
        return calendar.getTimeZone();
    }

    public final String format(Date date)
    {
        return format0(date);
    }

    protected abstract String format0(Date date);

    protected DateFormat()
    {
    }

    public static final int LONG = 1;
    public static final int SHORT = 3;
    protected Calendar calendar;
}
