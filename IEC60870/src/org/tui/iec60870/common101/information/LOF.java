/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tui.iec60870.common101.information;

import org.tui.iec60870.common.InformationElement;

/**
 * length of file 7.2.6.33<br>
 * (information element used by asdus id 120,121,126)<br>
 * <p><i>
 * <0>			not used <br>
 * <1.16777215>	number of octets of the complete file or section <br>
 * </i>
 *
 * @author lionnel cauvy
 * @version 1.0
 * @date 12.II.03
 */

public class LOF extends InformationElement
{
	/**
	 * constructs LOF object<br>
	 * length of file or section 7.2.6.35
	 *
	 * @param octet1 value of first octet of 'physical' data
	 * @param octet2 second octet
	 * @param octet3 third octet
	 */
	public LOF(short octet1, short octet2, short octet3)
	{
		super(3,1);
		octetset[0] = octet1;
		octetset[1] = octet2;
		octetset[2] = octet3;
		decode();
	}

	/**
	 * builds sub information data from 'physical' data.
	 * called when physical data is all known
	 */
	protected void decode()
	{
		dataset[0] = new Integer((octetset[0]) | (octetset[1]<<8) | (octetset[2]<<16));
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
		octetset[2] = (short)((((Integer)dataset[0]).intValue() & 0x00FF0000) >> 16);
	}

	/**
	 * default constructor
	 */
	public LOF()
	{
		super(3,1);
	}

}
