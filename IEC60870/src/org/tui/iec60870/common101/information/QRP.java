/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tui.iec60870.common101.information;

import org.tui.iec60870.common.InformationElement;

/**
 * qualifier of reset process command 7.2.6.27<br>
 * (information element used by asdus id 105)<br>
 * <p><i>
 * <0> not used
 * <1> general reset of process
 * <2> reset of pending information with time tag of the event buffer
 * <3.127> compatible range
 * <128.255> private range
 * </i>
 *
 * @author lionnel cauvy
 * @version 1.0
 * @date 12.II.03
 */

public class QRP extends InformationElement
{
	/**
	 * constructs QRP object<br>
	 * qualifier of reset process command
	 * @param octet value of 'physical' data
	 */
	public QRP(short octet)
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
	public QRP()
	{
		super(1,1);
	}

}
