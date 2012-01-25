/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tui.iec60870.common101.information;

import org.tui.iec60870.common.InformationElement;

/**
 * name of section 7.2.6.34<br>
 * (information element used by asdus id 121,122,123,124,125)<br>
 * <p><i>
 * <0>		not used <br>
 * <1.255>	name <br>
 * </i>
 *
 * @author lionnel cauvy
 * @version 1.0
 * @date 12.II.03
 */

public class NOS extends InformationElement
{
	/**
	 * constructs NOS object<br>
	 * name of section
	 * @param octet value of 'physical' data
	 */
	public NOS(short octet)
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
	public NOS()
	{
		super(1,1);
	}

}
