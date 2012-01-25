/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tui.iec60870.common101.information;

import org.tui.iec60870.common.InformationElement;

/**
 * scaled value 7.2.6.7<br>
 * (information element used by asdus id 11,12,35,49,111)<br>
 * <p><i>
 * </i>
 *
 * @author lionnel cauvy
 * @version 1.0
 * @date 12.II.03
 */

public class SVA extends InformationElement
{
	/**
	 * constructs SVA object<br>
	 * scaled value
	 * @param octet1 value of first octet of 'physical' data
	 * @param octet2 second octet
	 */
	public SVA(short octet1, short octet2)
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
    @Override
	protected void decode()
	{
		dataset[0] = new Short((short)( octetset[0] | (octetset[1]<< 8)));
		//dataset[1] = new Boolean(((( octetset[1] & 0x80 ) >> 7)==1)?true:false);
	}

	/**
	 * builds  'physical' data from sub information data.
	 * called when sub information data is all known
	 */
    @Override
	protected void code()
	{
 		octetset[0] = (short)(((Short)dataset[0]).shortValue() & 0x00FF);
		octetset[1] = (short)( (((((Short)dataset[0]).shortValue()) & 0xFF00) >> 8) /*| ( ((((Boolean)dataset[1]).booleanValue())?1:0)<<7 )*/ );
	}

        

	/**
	 * default constructor
	 */
	public SVA()
	{
		super(2,1);
	}

}
