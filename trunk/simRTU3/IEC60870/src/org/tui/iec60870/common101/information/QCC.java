/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tui.iec60870.common101.information;

import org.tui.iec60870.common.InformationElement;

/**
 * qualifier of counter command 7.2.6.23<br>
 * (information element used by asdu id 101)<br>
 * <p><i>
 * request (1.1)		<b>RQT	--> dataset[0]</b><br>
 * <0> no counter requested <br>
 * <1> request counter group 1 <br>
 * <2> request counter group 2 <br>
 * <3> request counter group 3 <br>
 * <4> request counter group 4 <br>
 * <5> general request counter <br>
 * <6..31> compatible range <br>
 * <32.63> private range <br>
 * <br>
 * freeze (1.1)			<b>FRZ	--> dataset[1]</b><br>
 * <0> no freeze or reset <br>
 * <1> counter freeze without reset <br>
 * <2> counter freeze with reset <br>
 * <3> counter reset <br>
 * </i>
 *
 * @author lionnel cauvy
 * @version 1.0
 * @date 12.II.03
 */
public class QCC extends InformationElement
{
	/**
	 * constructs QCC object<br>
	 * qualifier of counter interrogation command
	 * @param octet value of 'physical' data
	 */
	public QCC(short octet)
	{
		super(1,2);
		octetset[0] = octet;
		decode();
	}
	/**
	 * builds sub information data from 'physical' data.
	 * called when physical data is all known
	 */
	protected void decode()
	{
		dataset[0] = new Byte((byte)(octetset[0] & 0x003F));
		dataset[1] = new Byte((byte)((octetset[0] & 0x00C0)>>6));
	}
	/**
	 * builds  'physical' data from sub information data.
	 * called when sub information data is all known
	 */
	protected void code()
	{
		octetset[0] = (short)(((Byte)dataset[0]).byteValue() | (((Byte)dataset[1]).byteValue() << 6) );
	}
	/**
	 * default constructor _ called by asdu writer?
	 */
	public QCC()
	{
		super(1,2);
		octetset[0] = 0x00;
	}
}
