/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package lib.util;

public class PropUtils
{

    protected PropUtils()
    {
    }

    public static int getIntProperty(String s, int i)
    {
        String s1 = System.getProperty(s);
        int j;
        if(s1 == null)
            j = i;
        else
            try
            {
                if(s1.startsWith("0x") || s1.startsWith("0X"))
                    j = (int)Long.parseLong(s1.substring(2), 16);
                else
                    j = (int)Long.parseLong(s1);
            }
            catch(Exception exception)
            {
                throw new RuntimeException("Invalid Parameter: " + s + "=" + s1);
            }
        return j;
    }

    public static boolean getBooleanProperty(String s, boolean flag)
    {
        String s1 = System.getProperty(s);
        boolean flag1;
        if(s1 == null)
            flag1 = flag;
        else
        if(s1.equals("true"))
            flag1 = true;
        else
        if(s1.equals("false"))
            flag1 = false;
        else
            flag1 = flag;
        return flag1;
    }
}
