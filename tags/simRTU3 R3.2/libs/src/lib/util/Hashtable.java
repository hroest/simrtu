/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package lib.util;

public class Hashtable extends java.util.Hashtable
    implements Cloneable
{

    public Hashtable(int i)
    {
        super(i);
    }

    public Hashtable()
    {
    }

    public synchronized Object clone()
    {
        return clone0();
    }

    private native Hashtable clone0();

    public Object getGenericClone()
    {
        return clone0();
    }

    public synchronized String toString()
    {
        return super.toString();
    }
}
