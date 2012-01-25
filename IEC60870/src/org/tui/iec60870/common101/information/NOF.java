/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tui.iec60870.common101.information;

import org.tui.iec60870.common.InformationElement;

/**
 * name of file 7.2.6.33<br>
 * (information element used by asdus id 120,121,122,123,124,125,126)<br>
 * <p><i>
 * <0>			not used <br>
 * <1.65535>	name <br>
 * </i>
 *
 * @author lionnel cauvy
 * @version 1.0
 * @date 12.II.03
 */

public class NOF extends InformationElement
{
	/**
	 * constructs NOF object<br>
	 * name of file 7.2.6.33
	 * @param octet1 value of first octet of 'physical' data
	 * @param octet2 second octet
	 */
	public NOF(short octet1, short octet2)
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
		dataset[0] = new Integer( (octetset[0]) | (octetset[1]<<8));
		// computes invalidity
		invalidity = (short)(((((Integer)dataset[0]).intValue()==0)?1:0) << 1);
		if (invalidity!=0 )invalidity |= 2;
	}

	/**
	 * builds  'physical' data from sub information data.
	 * called when sub information data is all known
	 */
	protected void code()
	{
		octetset[0] = (short)(((Integer)dataset[0]).intValue() & 0x000000FF);
		octetset[1] = (short)((((Integer)dataset[0]).intValue() & 0x0000FF00) >> 8);
	}

	/**
	 * default constructor
	 */
	public NOF()
	{
		super(2,1);
	}

}
