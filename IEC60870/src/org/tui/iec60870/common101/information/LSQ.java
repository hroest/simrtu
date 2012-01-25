/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tui.iec60870.common101.information;

import org.tui.iec60870.common.InformationElement;

/**
 * last section or segment qualifier 7.2.6.31<br>
 * (information element used by asdus id 123)<br>
 * <p><i>
 * <0>	not used
 * <1>	file transfer without deactivation
 * <2>	file transfer with deactivation
 * <3>	section transfer without deactivation
 * <4>	section transfer with deactivation
 * <5.127>		compatible range
 * <128.255>	private range
 * </i>
 *
 * @author lionnel cauvy
 * @version 1.0
 * @date 12.II.03
 */

public class LSQ extends InformationElement
{
	/**
	 * constructs LSQ object<br>
	 * last section or segment qualifier
	 * @param octet value of 'physical' data
	 */
	public LSQ(short octet)
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
	public LSQ()
	{
		super(1,1);
	}

}
