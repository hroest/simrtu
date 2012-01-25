/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tui.iec60870.common101.information;

import org.tui.iec60870.common.InformationElement;

/**
 * single event of protection equipment 7.2.6.10 <br>
 * (information element used by asdus id 17,38) <br>
 * <p><i>
 * event state (1.1) <br>
 * 2 bits <br>
 * <0>	indeterminate<br>
 * <1>	off <br>
 * <2>	on <br>
 * <3>	indeterminate state <br>
 * ES	--> dataset[0] <br>
 * <br>
 * RESERVE <br>
 * RES	--> dataset[1] <br>
 * <br>
 * elapsed time (6) <br>
 * <0> valid <br>
 * <1> invalid <br>
 * EI	--> dataset[2]<br>
 * <br>
 * blocked (6) <br>
 * <0> not blocked <br>
 * <1> blocked <br>
 * <br>
 * BL	--> dataset[3] <br>
 * <br>
 * substituted (6) <br>
 * <0> not substituted <br>
 * <1> substituted <br>
 * SB	--> dataset[4] <br>
 * <br>
 * not topical (6 )<br>
 * <0> topical <br>
 * <1> not topical <br>
 * NT	--> dataset[5] <br>
 * <br>
 * invalid (6)<br>
 * <0> event valid <br>
 * <1> event invalid <br>
 * IV	--> dataset[6]<br>
 *</i>
 *
 * @author lionnel cauvy
 * @version 1.0
 * @date 12.II.03
 */

public class SEP extends InformationElement
{
	/**
	 * constructs SEP object<br>
	 * @param octet value of 'physical' data
	 *
	 */
	public SEP(short octet)
	{
		super(1,7);
		octetset[0] = octet;
		decode();

	}

	/**
	 * builds sub information data from 'physical' data.
	 * called when physical data is all known
	 */
	protected void decode()
	{
		dataset[0] = new Byte((byte)(octetset[0] & 0x0003));
		dataset[1] = new Boolean((((octetset[0] & 0x0004)>>2)==0)?false:true);
		dataset[2] = new Boolean((((octetset[0] & 0x0008)>>3)==0)?false:true);
		dataset[3] = new Boolean((((octetset[0] & 0x0010)>>4)==0)?false:true);
		dataset[4] = new Boolean((((octetset[0] & 0x0020)>>5)==0)?false:true);
		dataset[5] = new Boolean((((octetset[0] & 0x0040)>>6)==0)?false:true);
		dataset[6] = new Boolean((((octetset[0] & 0x0080)>>7)==0)?false:true);
		// computes invalidity
		invalidity = (short)(((((Boolean)dataset[3]).booleanValue()?1:0) << 3) | ((((Boolean)dataset[4]).booleanValue()?1:0) << 3) | ((((Boolean)dataset[5]).booleanValue()?1:0) << 9) | ((((Boolean)dataset[6]).booleanValue()?1:0) << 1) | ((((Boolean)dataset[2]).booleanValue()?1:0) << 8));
		if (invalidity!=0)invalidity |= 2;
	}

	/**
	 * builds  'physical' data from sub information data.
	 * called when sub information data is all known
	 */
	protected void code()
	{
		octetset[0] = (short) (((Byte)dataset[0]).byteValue() | ((((Boolean)dataset[1]).booleanValue()?1:0) << 3) |((((Boolean)dataset[2]).booleanValue()?1:0) << 4) | ((((Boolean)dataset[3]).booleanValue()?1:0) << 5) | ((((Boolean)dataset[4]).booleanValue()?1:0) << 6) | ((((Boolean)dataset[5]).booleanValue()?1:0) << 7));
	}

	/**
	 * default constructor _ called by asdu writer?
	 */
	public SEP()
	{
		super(1,7);
	}

}
