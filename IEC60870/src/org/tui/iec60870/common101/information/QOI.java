/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tui.iec60870.common101.information;

import org.tui.iec60870.common.InformationElement;

/**
 * qualifier of interrogation 7.2.6.22<br>
 * (information element used by asdu id 100)<br>
 * <p><i>
 * <0> not used <br>
 * <1..19> compatible range <br>
 * <20> station interrogation (global) <br>
 * <21> group1 interrogation <br>
 * <22> group2 interrogation <br>
 * <23> group3 interrogation <br>
 * <24> group4 interrogation <br>
 * <25> group5 interrogation <br>
 * <26> group6 interrogation <br>
 * <27> group7 interrogation <br>
 * <28> group8 interrogation <br>
 * <29> group9 interrogation <br>
 * <30> group10 interrogation <br>
 * <31> group11 interrogation <br>
 * <32> group12 interrogation <br>
 * <33> group13 interrogation <br>
 * <34> group14 interrogation <br>
 * <35> group15 interrogation <br>
 * <36> group16 interrogation <br>
 * <37..63> compatible range <br>
 * <64.255> private range <br>
 * </i>
 * @author lionnel cauvy
 * @version 1.0
 * @date 12.II.03
 */

public class QOI extends InformationElement
{
	/**
	 * constructs QOI object<br>
	 * qualifier of interrogation
	 * @param octet value of 'physical' data
	 */
	public QOI(short octet)
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
	public QOI()
	{
		super(1,1);
		octetset[0] = 0x00;
	}

}
