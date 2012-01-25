/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tui.iec60870.common101.information;

import org.tui.iec60870.common.InformationElement;

/**
 * length of segment 7.2.6.36<br>
 * (information element used by asdus id 125)<br>
 * <p><i>
 * <0>			not used <br>
 * <1.16777215>	number of octets of the segment <br>
 * </i>
 *
 * @author lionnel cauvy
 * @version 1.0
 * @date 12.II.03
 */

public class LOS extends InformationElement
{
	/**
	 * constructs LOS object<br>
	 * length of segment 7.2.6.36
	 * @param octet value of 'physical' data
	 */
	public LOS(short octet)
	{
		super(1,1);
		octetset[0] = octet;
		decode();
	}

	/**
	 * builds sub information data from 'physical' data.
	 * called when physical data is all known
	 */
	protected void decode()
	{
		dataset[0] = new Short(octetset[0]);
		// computes invalidity
		invalidity = (short)(((((Short)dataset[0]).shortValue()==0)?1:0) << 1);
		if (invalidity!=0 )invalidity |= 2;
	}

	/**
	 * builds  'physical' data from sub information data.
	 * called when sub information data is all known
	 */
	protected void code()
	{
		octetset[0] = ((Short)dataset[0]).shortValue();
	}

	/**
	 * default constructor
	 */
	public LOS()
	{
		super(1,1);
	}

}
