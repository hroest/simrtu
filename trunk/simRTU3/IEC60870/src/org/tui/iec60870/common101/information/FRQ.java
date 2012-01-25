/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tui.iec60870.common101.information;

import org.tui.iec60870.common.InformationElement;

/**
 * file ready qualifier 7.2.6.28<br>
 * (information element used by asdus id 120)<br>
 * <p><i>
 * <b>UI7	--> dataset[0]</b> <br>
 * <0>		default <br>
 * <1.63>	compatible range <br>
 * <64.127>	private range <br>
 * <br>
 * <b>BS1	--> dataset[1]</b><br>
 * <0>		positive confirm of select, request, deactivate or delete <br>
 * <1> 		negative confirm of select, request, deactivate or delete <br>
 * </i>
 *
 * @author lionnel cauvy
 * @version 1.0
 * @date 12.II.03
 */

public class FRQ extends InformationElement
{
	/**
	 * constructs FRQ object<br>
	 * file ready qualifier
	 * @param octet value of 'physical' data
	 */
	public FRQ(short octet)
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
		dataset[0] = new Byte((byte)(octetset[0] & 0x007F));
		dataset[1] = new Boolean((((octetset[0] & 0x0080)>>7)==0)?false:true);
		// computes invalidity
		invalidity = (short)(((((Boolean)dataset[1]).booleanValue()?1:0) << 5) | (((((Byte)dataset[0]).byteValue()==0)?1:0) << 5) );
		if (invalidity!=0 )invalidity |= 2;
	}

	/**
	 * builds  'physical' data from sub information data.
	 * called when sub information data is all known
	 */
	protected void code()
	{
		octetset[0] = (short)((((Byte)dataset[0]).byteValue() | (((Boolean)dataset[1]).booleanValue()?1:0) << 7));
	}

	/**
	 * default constructor
	 */
	public FRQ()
	{
		super(1,2);
	}

}
