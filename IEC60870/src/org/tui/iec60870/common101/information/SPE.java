/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tui.iec60870.common101.information;

import org.tui.iec60870.common.InformationElement;

/**
 * start event of protection equipment 7.2.6.11<br>
 * (information element used by asdus id 18,39)<br>
 * <p><i>
 * general start of operation				<b>GS	--> dataset[0]</b><br>
 * <0> no <br>
 * <1> yes <br>
 * <br>
 * start of operation phase L1				<b>SL1	--> dataset[1]</b><br>
 * <0> no <br>
 * <1> yes <br>
 * <br>
 * start of operation phase L2				<b>SL2	--> dataset[2]</b><br>
 * <0> no <br>
 * <1> yes <br>
 * <br>
 * start of operation phase L3				<b>SL3	--> dataset[3]</b><br>
 * <0> no <br>
 * <1> yes <br>
 * <br>
 * start of operation IE (earth current)	<b>SIE	--> dataset[4]</b><br>
 * <0> no <br>
 * <1> yes <br>
 * <br>
 * start of operation in reverse direction	<b>SRD	--> dataset[5]</b><br>
 * <0> no <br>
 * <1> yes <br>
 * <br>
 * RESERVE									<b>RES	--> dataset[6]</b><br>
 * </i>
 *
 * @author lionnel cauvy
 * @version 1.0
 * @date 12.II.03
 */

public class SPE extends InformationElement
{
	/**
	 * constructs SPE object<br>
	 * @param octet value of 'physical' data
	 */
	public SPE(short octet)
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

		dataset[0] = new Boolean(((octetset[0] & 0x0001)==0)?false:true);
		dataset[1] = new Boolean((((octetset[0] & 0x0002)>>1)==0)?false:true);
		dataset[2] = new Boolean((((octetset[0] & 0x0004)>>2)==0)?false:true);
		dataset[3] = new Boolean((((octetset[0] & 0x0008)>>3)==0)?false:true);
		dataset[4] = new Boolean((((octetset[0] & 0x0010)>>4)==0)?false:true);
		dataset[5] = new Boolean((((octetset[0] & 0x0020)>>5)==0)?false:true);
		dataset[6] = new Byte((byte)((octetset[0] & 0x00C0)>>6));
		// computes invalidity
		invalidity = (short)(((((Boolean)dataset[0]).booleanValue()?0:1) << 10) | ((((Boolean)dataset[1]).booleanValue()?0:1) << 10) | ((((Boolean)dataset[2]).booleanValue()?0:1) << 10) | (((Boolean)dataset[3]).booleanValue()?0:1 << 10) | (((Boolean)dataset[4]).booleanValue()?0:1 << 10) | ((((Boolean)dataset[5]).booleanValue()?0:1) << 10));
		if (invalidity!=0){
			invalidity |= 2;
		}
	}

	/**
	 * builds  'physical' data from sub information data.
	 * called when sub information data is all known
	 */
	protected void code()
	{
		octetset[0] = (short)((((Boolean)dataset[0]).booleanValue()?1:0) | ((((Boolean)dataset[1]).booleanValue()?1:0) << 1) | ((((Boolean)dataset[2]).booleanValue()?1:0) << 2) | ((((Boolean)dataset[3]).booleanValue()?1:0) << 3) | ((((Boolean)dataset[4]).booleanValue()?1:0) << 4) | ((((Boolean)dataset[5]).booleanValue()?1:0) << 5) | (((Byte)dataset[6]).byteValue() << 6) );
	}

	/**
	 * default constructor
	 */
	public SPE()
	{
		super(1,7);
	}

}