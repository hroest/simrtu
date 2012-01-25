/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tui.iec60870.common101.information;

import org.tui.iec60870.common.InformationElement;
import lib.math.Math32FP;

/**
 * normalized value 7.2.6.6<br>
 * (information element used by asdus id 9,10,21,34,48,110)<br>
 * <p><i>
 * </i>
 *
 * @author lionnel cauvy
 * @version 1.0
 * @date 12.II.03
 */

public class NVA extends InformationElement
{
	/**
	 * constructs NVA object<br>
	 * normalized value
	 * @param octet1 value of first octet of 'physical' data
	 * @param octet2 second octet
	 */
	public NVA(short octet1, short octet2)
	{
		super(2,1);
		octetset[0] = octet1;
		octetset[1] = octet2;

		decode();
	}

	/**
	 * builds sub information data from 'physical' data.
	 * called when physical data is all known
	 */
	protected void decode()
	{
		dataset[0] = new Math32FP((short)( octetset[0] | (octetset[1] <<8)));
	}

	/**
	 * builds  'physical' data from sub information data.
	 * called when sub information data is all known
	 */
	protected void code()
	{
 		octetset[0] = (short)(((Math32FP)dataset[0]).fpValue() & 0x00FF);
		octetset[1] = (short)((((Math32FP)dataset[0]).fpValue() & 0xFF00) >> 8);
	}

	/**
	 * default constructor
	 */
	public NVA()
	{
		super(2,1);
	}

}
