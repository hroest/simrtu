/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tui.iec60870.common101.information;

import org.tui.iec60870.common.InformationElement;

/**
 * OCTET not defined by IEC, used for segment information.
 *
 * @author lionnel cauvy
 * @version 1.0
 * @date 12.II.03
 */

public class OCTET extends InformationElement
{
	/**
	 * OCTET value
	 * asdu 125
	 */

	/**
	 * constructs OCTET object
	 * @param octet value of 'physical' data
	 */
	public OCTET(short octet)
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
	public OCTET()
	{
		super(1,1);
		octetset[0] = 0x00;
	}

}
