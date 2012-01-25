/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tui.iec60870.common101.information;

import org.tui.iec60870.common.InformationElement;

/**
 * qualifier of parameter of measured value 7.2.6.24<br>
 * (information element used by asdus id 110,111,112)<br>
 * <p><i>
 * kind of parameter (1.1)		<b>KPA	--> dataset[0]</b><br>
 * <0> not used <br>
 * <1> threshold value <br>
 * <2> smoothing factor <br>
 * <3> low limit for transmission of measured values <br>
 * <4> high limit for transmission of measured values <br>
 * <5..31> compatible range <br>
 * <32.63> private range <br>
 * <br>
 * local parameter change (6)	<b>LPC	--> dataset[1]</b><br>
 * <0> no change <br>
 * <1> change <br>
 * <br>
 * parameter in operation (6)	<b>POP	--> dataset[2]</b><br>
 * <0> operation <br>
 * <1> not in operation <br>
 * </i>
 *
 * @author lionnel cauvy
 * @version 1.0
 * @date 12.II.03
 */

public class QPM extends InformationElement
{
	/**
	 * QPM octet value
	 * asdu
	 */

	/**

	/**
	 * constructs QPM object<br>
	 * qualifier of parameter of measured values
	 * @param octet value of 'physical' data
	 */
	public QPM(short octet)
	{
		super(1,3);
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
		dataset[1] = new Boolean((((octetset[0] & 0x0040)>>6)==0)?false:true);
		dataset[2] = new Boolean((((octetset[0] & 0x0080)>>7)==0)?false:true);
		// computes invalidity
		invalidity = (short)(((((Boolean)dataset[2]).booleanValue()?1:0) << 10) | (((((Byte)dataset[0]).byteValue()==0)?1:0) << 1) );
		if (invalidity!=0 )invalidity |= 2;
	}

	/**
	 * builds  'physical' data from sub information data.
	 * called when sub information data is all known
	 */
	protected void code()
	{
		octetset[0] = (short) (((Byte)dataset[0]).byteValue() | ((((Boolean)dataset[1]).booleanValue()?1:0) << 6) |((((Boolean)dataset[2]).booleanValue()?1:0) << 7));
	}

	/**
	 * default constructor _ called by asdu writer?
	 */
	public QPM()
	{
		super(1,3);
	}

}
