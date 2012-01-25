/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package lib.util;

public class JCB
{

    private JCB()
    {
    }

    //static native void setJVM_Number(int i);

    //native int getJVM_Number();

    //public static native JCB getCurrentJCB();

    //static native TMCB getTMCB();

    //public static native void setDebugInfo(int ai[]);

    //public static native int getRuntimeAddr();

    //public native int getCSAListHeader();

    //public native int getSystemProperties();

    //native int getMemoryLayout();

    public static int resources;

    public static byte[] getResourceAsArray(String s)
    {
      System.out.print("JCB");
        if(resources != 0 && s != null)
        {
           // Object aobj[] = (Object[])JEM_rawJEM.toObject(resources);
          //  int i = aobj.length;
            //for(int j = 0; j < i; j += 2)
              //  if(s.equals(aobj[j]))
                //    return (byte[])aobj[j + 1];

        }
        return null;
    }
}
