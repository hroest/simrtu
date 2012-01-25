/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tui.iec60870.common;

/**
 * IEC608705Exception
 *
 * @author lionnel cauvy
 * @version 1.0
 * @date 01.II.03
 */
public class IEC608705Exception extends Exception
{
	public IEC608705Exception()
	{
		super();
	}

	public IEC608705Exception(String msg)
	{
		super(msg);
	}

        public IEC608705Exception(Exception e)
        {
            super(e);
        }

        public IEC608705Exception(Exception e, String msg)
        {
            this(e.toString() + "Ursache:" + msg + " // ");
        }

}
