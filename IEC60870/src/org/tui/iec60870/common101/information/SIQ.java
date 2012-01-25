/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tui.iec60870.common101.information;

import org.tui.iec60870.common.InformationElement;

/**
 * <p>
 * single point information with quality descriptor 7.2.6.1<br>
 * (information element used by asdus id 1,2,30)<br>
 * <p><i>
 * single point information (6)<br>
 * <0>	off<br>
 * <1>	on<br>
 * SPI	--> dataset[0]<br>
 * <br>
 * RESERVE<br>
 * RES	--> dataset[1]<br>
 * <br>
 * blocked (6)<br>
 * 1 bit<br>
 * <br>
 * BL	--> dataset[2]<br>
 * <br>
 * substituted (6)<br>
 * 1 bit<br>
 * SB	--> dataset[3]<br>
 * <br>
 * not topical (6)<br>
 * 1 bit<br>
 * NT	--> dataset[4]<br>
 * <br>
 * invalid (6)<br>
 * 1 bit<br>
 * IV	--> dataset[5]<br>
 *</i>
 *
 * @author lionnel cauvy
 * @version 1.0
 * @date 12.II.03
 */

public class SIQ extends InformationElement
{
	/**
	 * constructs SIQ object<br>
	 * single point information with quality descriptor
	 * @param octet value of 'physical' data
	 */
	public SIQ(short octet)
	{
		super(1,6);
		octetset[0] = octet;
		decode();
	}

	/**
	 * builds sub information data from 'physical' data.
	 * called when physical data is all known
	 */
        @Override
	protected void decode()
	{
		dataset[0] = new Boolean(((octetset[0] & 0x0001)==0)?false:true);
		dataset[1] = new Byte((byte)((octetset[0] & 0x000E) >> 1));
		dataset[2] = new Boolean((((octetset[0] & 0x0010)>>4)==0)?false:true);
		dataset[3] = new Boolean((((octetset[0] & 0x0020)>>5)==0)?false:true);
		dataset[4] = new Boolean((((octetset[0] & 0x0040)>>6)==0)?false:true);
		dataset[5] = new Boolean((((octetset[0] & 0x0080)>>7)==0)?false:true);
		// computes invalidity
		invalidity = (short)(((((Boolean)dataset[2]).booleanValue()?1:0) << 3) | ((((Boolean)dataset[3]).booleanValue()?1:0) << 3) | ((((Boolean)dataset[4]).booleanValue()?1:0) << 9) | ((((Boolean)dataset[5]).booleanValue()?1:0) << 1));
		if (invalidity!=0)invalidity |= 2;
	}

	/**
	 * builds  'physical' data from sub information data.
	 * called when sub information data is all known
	 */
        @Override
	protected void code()
	{
		octetset[0] = (short) ( (((Byte)dataset[0]).byteValue()) | (((Byte)dataset[1]).byteValue() << 1) | ((((Boolean)dataset[2]).booleanValue()?1:0) << 4) | ((((Boolean)dataset[3]).booleanValue()?1:0) << 5) | ((((Boolean)dataset[4]).booleanValue()?1:0) << 6) | ((((Boolean)dataset[5]).booleanValue()?1:0) << 7));
	}

	/**
	 * default constructor
	 */
	public SIQ()
	{
		super(1,6);
	}

}
