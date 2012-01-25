/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package lib.util;

import java.util.TimeZone;
import lib.text.DateFormat;
import java.util.Date;

// Referenced classes of package com.ajile.util:
//            TimeZoneUtils

public class TimeStamper
{



    private TimeStamper()
    {
    }

    public static long getCurTimeStampToUse()
    {
        if(useY2KTimeStamp)
            return y2kNoonGMTMillis;
        else
            return System.currentTimeMillis();
    }

    public static Date getCurDateToUse()
    {
        return new Date(getCurTimeStampToUse());
    }

    public static String getCurDateStringToUse()
    {
        Date date = getCurDateToUse();
        DateFormat dateformat = DateFormat.getDateTimeInstance(1, 1);
        dateformat.setTimeZone(getTimeZoneToUse());
        return dateformat.format(date);
    }

    public static TimeZone getTimeZoneToUse()
    {
        return defTimeZone;
    }

    public static void setTimeZoneToUse(String s)
    {
        defTimeZone = TimeZone.getTimeZone(s);
        TimeZoneUtils.setDefault(defTimeZone);
    }

    public static String getTimeZoneName(TimeZone timezone, Date date)
    {
        boolean flag = TimeZoneUtils.inDaylightTime(timezone, date);
        String s = TimeZoneUtils.getDisplayName(timezone, flag);
        return s;
    }

    private static long y2kMidnightGMTMillis = 0xdc6acfac00L;
    private static long y2kNoonGMTMillis = 0xdc6d62da00L;
    private static boolean useY2KTimeStamp;
    private static TimeZone defTimeZone;

    static
    {
        String s = System.getProperty("useY2kStamp");
        if(s == null)
            useY2KTimeStamp = false;
        else
            useY2KTimeStamp = s.equals("true");
        String s1 = System.getProperty("useGmtTz");
        if(s1 == null)
            defTimeZone = TimeZone.getDefault();
        else
        if(s1.equals("true"))
        {
            defTimeZone = TimeZone.getTimeZone("GMT");
            TimeZoneUtils.setDefault(defTimeZone);
        } else
        {
            defTimeZone = TimeZone.getDefault();
        }
    }
}