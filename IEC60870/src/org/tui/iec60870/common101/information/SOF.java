/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tui.iec60870.common101.information;

import org.tui.iec60870.common.InformationElement;

/**
 * status of file 7.2.6.38<br>
 * (information element used by asdus id 126)<br>
 * <p><i>
 * <b>STATUS		--> dataset[0]</b> <br>
 * <0>		default <br>
 * <1.15>	compatible range <br>
 * <16.32>	private range <br>
 * <br>
 * <b>RES1	--> dataset[1]</b> <br>
 *	 <br>
 * <b>FOR;	--> dataset[2]</b> <br>
 * <0> 		name defines file <br>
 * <1> 		name defines subdirectory <br>
 * <br>
 * <b>FA	--> dataset[3]</b> <br>
 * <0> 		file waits for transfer <br>
 * <1> 		transfer of this file is active <br>
 * </i>
 *
 * @author lionnel cauvy
 * @version 1.0
 * @date 12.II.03
 */

public class SOF extends InformationElement
{
	/**
	 * constructs SOF object<br>
	 * status of file 7.2.6.38
	 * @param octet value of 'physical' data
	 */
	public SOF(short octet)
	{
		super(1,4);
		octetset[0] = octet;
		decode();
	}

	/**
	 * builds sub information data from 'physical' data.
	 * called when physical data is all known
	 */
	protected void decode()
	{
		dataset[0] = new Byte((byte)(octetset[0] & 0x001F));
		dataset[1] = new Boolean((((octetset[0] & 0x0020)>>5)==0)?false:true);
		dataset[2] = new Boolean((((octetset[0] & 0x0040)>>6)==0)?false:true);
		dataset[3] = new Boolean((((octetset[0] & 0x0080)>>7)==0)?false:true);
		// computes invalidity
		invalidity = (short)(((((Byte)dataset[0]).byteValue()==0)?1:0) << 1);
		if (invalidity!=0 )invalidity |= 2;
	}

	/**
	 * builds  'physical' data from sub information data.
	 * called when sub information data is all known
	 */
	protected void code()
	{
		octetset[0] = (short) (((Byte)dataset[0]).byteValue() | ((((Boolean)dataset[1]).booleanValue()?1:0) << 5)| ((((Boolean)dataset[2]).booleanValue()?1:0) << 6) |((((Boolean)dataset[3]).booleanValue()?1:0) << 7));
	}

	/**
	 * default constructor _ called by asdu writer?
	 */
	public SOF()
	{
		super(1,4);
	}

}
