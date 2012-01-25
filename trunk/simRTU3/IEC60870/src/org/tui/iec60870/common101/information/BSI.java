/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tui.iec60870.common101.information;

import org.tui.iec60870.common.InformationElement;

/**
 * binary state information 7.2.6.13<br>
 * (information element used by asdus id 7,8,33,51)<br>
 * <p><i>
 * BSI value --> dataset[0]
 * </i>
 *
 * @author lionnel cauvy
 * @version 1.0
 * @date 12.II.03
 */

public class BSI extends InformationElement
{
	/**
	 * constructs BSI object<br>
	 * binary state information
	 *
	 * @param octet1 value of first octet of 'physical' data
	 * @param octet2 second octet
	 * @param octet3 third octet
	 * @param octet4 fourth octet
	 */
	public BSI(short octet1, short octet2, short octet3, short octet4)
	{
		super(4,1);
		octetset[0] = octet1;
		octetset[1] = octet2;
		octetset[2] = octet3;
		octetset[3] = octet4;
		decode();
	}

	/**
	 * builds sub information data from 'physical' data.<br>
	 * called when physical data is all known
	 */
	protected void decode()
	{
		dataset[0] = new Integer((octetset[0]) | (octetset[1]<<8) | (octetset[2]<<16) | (octetset[3]<<24));
	}

	/**
	 * builds  'physical' data from sub information data.<br>
	 * called when sub information data is all known
	 */
	protected void code()
	{
		octetset[0] = (short)(((Integer)dataset[0]).intValue() & 0x000000FF);
		octetset[1] = (short)((((Integer)dataset[0]).intValue() & 0x0000FF00) >> 8);
		octetset[2] = (short)((((Integer)dataset[0]).intValue() & 0x00FF0000) >> 16);
		octetset[3] = (short)((((Integer)dataset[0]).intValue() & 0xFF000000) >> 24);
	}

	/**
	 * default constructor
	 */
	public BSI()
	{
		super(4,1);
		octetset[0] = octetset[1] = octetset[2] = octetset[3] = 0x00;
		dataset[0] = null;
	}

}
