/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tui.iec60870.common101.information;

import org.tui.iec60870.common.InformationElement;

/**
 * fixed test pattern 7.2.6.14<br>
 * (information element used by asdus id 104)<br>
 * <p><i>
 * FBP value = 0x55AA
 * </i>
 *
 * @author lionnel cauvy
 * @version 1.0
 * @date 12.II.03
 */

public class FBP extends InformationElement
{
	/**
	 * constructs FBP object<br>
	 * fixed test pattern
	 */
	public FBP()
	{
		super(1,1);
		octetset[0] = 0x55AA;
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

}
