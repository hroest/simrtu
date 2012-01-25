/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tui.iec60870.common101.information;

import org.tui.iec60870.common.InformationElement;

/**
 * checksum 7.2.6.37<br>
 * (information element used by asdus id 123)<br>
 * <p><i>
 * </i>
 *
 * @author lionnel cauvy
 * @version 1.0
 * @date 12.II.03
 */

public class CHS extends InformationElement
{
	/**
	 * constructs CHS object<br>
	 * checksum
	  * @param octet value of 'physical' data
	 */
	public CHS(short octet)
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
	public CHS()
	{
		super(1,1);
		octetset[0] = 0x00;
		dataset[0] = null;
	}

}
