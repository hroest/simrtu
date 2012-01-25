/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tui.iec60870.common101.information;

import org.tui.iec60870.common.InformationElement;

/**
 * select and call qualifier 7.2.6.30<br>
 * (information element used by asdus id 122)<br>
 * <p><i>
 * <b>UI4	--> dataset[0]</b> <br>
 * <0>		not used <br>
 * <1>		select file <br>
 * <2>		request file <br>
 * <3>		deactivate file <br>
 * <4>		delete file <br>
 * <5>		select section <br>
 * <6>		request section <br>
 * <7>		deactivate section <br>
 * <8.10>	compatible range <br>
 * <11.15>	private range <br>
 * <br>
 * <b>UI4	--> dataset[1]</b> <br>
 * <0>		default <br>
 * <1>		requested memory space not available <br>
 * <2>		checksum failed <br>
 * <3>		unexpected communication service <br>
 * <4>		unexpected name of file <br>
 * <5>		unexpected name of section <br>
 * <6.10>	compatible range <br>
 * <11.15>	private range <br>
 * </i>
 *
 * @author lionnel cauvy
 * @version 1.0
 * @date 12.II.03
 */

public class SCQ extends InformationElement
{
	/**
	 * constructs SCQ object<br>
	 * section and call qualifier
	 * @param octet value of 'physical' data
	 */
	public SCQ(short octet)
	{
		super(1,2);
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
		dataset[0] = new Byte((byte)(octetset[0] & 0x000F));
		dataset[1] = new Byte((byte)((octetset[0] & 0x00F0)>>4));
		// computes invalidity
		invalidity = (short)( (((((Byte)dataset[0]).byteValue()==0)?1:0) << 5) | (((((Byte)dataset[1]).byteValue()<6)?1:0) << 5) );
		if (invalidity!=0 )invalidity |= 2;
	}

	/**
	 * builds  'physical' data from sub information data.
	 * called when sub information data is all known
	 */
    @Override
	protected void code()
	{
		octetset[0] = (short)(((Byte)dataset[0]).byteValue() | (((Byte)dataset[1]).byteValue() << 4) );
	}

	/**
	 * default constructor _ called by asdu writer?
	 */
	public SCQ()
	{
		super(1,2);
	}

}
