/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tui.iec60870.common101;

import org.tui.iec60870.common.Asdu;
import org.tui.iec60870.common.IEC608705Exception;
import org.tui.iec60870.common.InformationElement;
import org.tui.iec60870.common.InformationObject;

/**
 * <p>
 * Asdu application service data unit for 101/104 protocol.
 * <p>
 * This companion standard defines following points:
 * <p>
 * - Cause of transmission field can be 1 or 2 octets sized. The second octet,
 * if present, defines originator address eg station that sent the related msg.
 * <p>
 * - Common Address of Asdus can be 1 or 2 octets sized.
 * <p>
 * - Information Object Address can have a length from 1 up to 3 octets.
 *
 * @author lionnel cauvy
 * @version 1.0
 * @date 18.II.03
 */
public class Asdu101 extends Asdu
{
	/**
	 * <p>
	 * Originator address. Second octet from cause of transmission field.<br>
	 * -1 value stands for not used.
	 */
	public short oa	= -1;
	/**
	 * Get accessor on a single value hold by asdu from its index positions.
	 *
	 * @param ioa	Information object address.
	 * @param pie	Position of an ie into sequence of information elements. From zero value.
	 * @param pval	Position of value into sequence of values. From zero value.
	 *
	 * @return the value as an Object. It can be a Byte, a Short, a Math32FP, a Float....
	 */
        @Override
	public Object value(int ioa, int index) throws IEC608705Exception
	{
		InformationObject obj = null;
		for(int i=0;i<io.size();i++)
		{
			obj = (InformationObject)io.elementAt(i);
			if(obj.address==ioa)
				break;
		}
		InformationElement elt = (InformationElement)obj.ie.elementAt(index);
		//@todo generic interface, especially for later expressions...

                return 	elt.dataset[index];
	}

	/**
	 * empty constructor
	 */
	public Asdu101()
	{
	}
}