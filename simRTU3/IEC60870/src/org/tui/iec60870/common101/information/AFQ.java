/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tui.iec60870.common101.information;

import org.tui.iec60870.common.InformationElement;

/**
 * <p>
 * ACK file or section qualifier 7.2.6.32<br>
 * (information element used by asdu id 24)
 * <p><i>
 * <0>		not used<br>
 * <1>		ack file transfer<br>
 * <2>		nack file transfer<br>
 * <3>		ack section transfer<br>
 * <4>		nack section transfer<br>
 * <5.10>	compatible range<br>
 * <11.15>	private range<br>
 * UI4		--> dataset[0]<br>
 * <br>
 * <0>	default<br>
 * <1>		requested memory space not available<br>
 * <2>		checksum failed<br>
 * <3>		unexpected communication service<br>
 * <4>		unexpected name of file<br>
 * <5>		unexpected name of section<br>
 * <6.10>	compatible range<br>
 * <11.15>	private range<br>
 * UI4b;	--> dataset[1]<br>
 * </i>
 *
 * @author lionnel cauvy
 * @version 1.0
 * @date 12.II.03
 */

public class AFQ extends InformationElement
{

	/**
	 * constructs AFQ object<br>
	 * acknowledge file or section reading 7.2.6.32
	 *
	 * @param octet value of 'physical' data
	 */
	public AFQ(short octet)
	{
		super(1,2);
		octetset[0] = octet;
		decode();
	}

	/**
	 * builds sub information data from 'physical' data.<br>
	 * called when physical data is all known
	 */
	protected void decode()
	{
		dataset[0] = new Byte((byte)(octetset[0] & 0x000F));
		dataset[1] = new Byte((byte)((octetset[0] & 0x00F0)>>4));
		// computes invalidity
		invalidity = (short)( (((((Byte)dataset[0]).byteValue()==0)?1:0) << 5) | (((((Byte)dataset[1]).byteValue()<6)?1:0) << 5) );
		if (invalidity!=0 )invalidity |= 2 ;
	}

	/**
	 * builds  'physical' data from sub information data.<br>
	 * called when sub information data is all known
	 */
	protected void code()
	{
		octetset[0] = (short)(((Byte)dataset[0]).byteValue() | (((Byte)dataset[1]).byteValue() << 4) );
	}

	/**
	 * default constructor _ called by asdu writer?
	 */
	public AFQ()
	{
		super(1,2);
		octetset[0] = 0x00;
		dataset[0] = dataset[1] = null;
	}

}
