/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tui.iec60870.common101.information;

import org.tui.iec60870.common.InformationElement;

/**
 * output circuit command of protection equipment 7.2.6.12<br>
 * (information element used by asdus id 19,40)<br>
 * <p><i>
 * general command to output circuit		<b>GC	--> dataset[0]</b><br>
 * <0> no <br>
 * <1> yes <br>
 * <br>
 * command to output circuit phase L1		<b>CL1	--> dataset[1]</b><br>
 * <0> no <br>
 * <1> yes <br>
 * <br>
 * command to output circuit phase L2		<b>CL2	--> dataset[2]</b><br>
 * <0> no <br>
 * <1> yes <br>
 * <br>
 * command to output circuit phase L3		<b>CL3	--> dataset[3]</b><br>
 * <0> no <br>
 * <1> yes <br>
 * <br>
 * RESERVE									<b>RES	--> dataset[4]</b><br>
 * </i>
 *
 * @author lionnel cauvy
 * @version 1.0
 * @date 12.II.03
 */

public class OCI extends InformationElement
{
	/**
	 * constructs OCI object<br>
	 * output circuit command of protection equipment
	 * @param octet value of 'physical' data
	 */
	public OCI(short octet)
	{
		super(1,5);
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
		dataset[4] = new Byte((byte)((octetset[0] & 0x00F0)>>4));
		// computes invalidity
		invalidity = (short)(((((Boolean)dataset[0]).booleanValue()?0:1) << 10) | ((((Boolean)dataset[1]).booleanValue()?0:1) << 10) | ((((Boolean)dataset[2]).booleanValue()?0:1) << 10) | ((((Boolean)dataset[3]).booleanValue()?0:1) << 10));
		if (invalidity!=0)invalidity |= 2;
	}

	/**
	 * builds  'physical' data from sub information data.
	 * called when sub information data is all known
	 */
	protected void code()
	{
		octetset[0] = (short)((((Boolean)dataset[0]).booleanValue()?1:0) | ((((Boolean)dataset[1]).booleanValue()?1:0) << 1) | ((((Boolean)dataset[2]).booleanValue()?1:0) << 2) | ((((Boolean)dataset[3]).booleanValue()?1:0) << 3) | (((Byte)dataset[4]).byteValue() << 4) );
	}

	/**
	 * default constructor
	 */
	public OCI()
	{
		super(1,5);
	}

}
