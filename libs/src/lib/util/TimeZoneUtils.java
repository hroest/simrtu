/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package lib.util;

import java.util.Date;
import java.util.TimeZone;

public final class TimeZoneUtils
{

    private TimeZoneUtils()
    {
    }

    public static native void setDefault(TimeZone timezone);

    public static boolean inDaylightTime(TimeZone timezone, Date date)
    {
        return false;
    }

    public static String getDisplayName(TimeZone timezone, boolean flag)
    {
        return timezone.getID();
    }
}
