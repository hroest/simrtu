/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tui.iec60870.common101.information;

import org.tui.iec60870.common.InformationElement;

/**
 * binary counter reading 7.2.6.9 <br>
 * (information element used by asdus id 15,16,37) <br>
 * <p><i>
 * counter reading (2.1)<br>
 * 4 octets <br>
 * CR	--> dataset[0] <br>
 * <br>
 * sequence notation (1.1)<br>
 * 5 bits <br>
 * SQ	--> dataset[1] <br>
 * <br>
 * counter overflow (6)<br>
 * 1 bit <br>
 * CY	--> dataset[2] <br>
 * <br>
 * counter adjustment (6)<br>
 * 1 bit <br>
 * CA	--> dataset[3] <br>
 * <br>
 * read validity (6)<br>
 * 1 bit <br>
 * IV	--> dataset[4] <br>
 * </i>
 *
 * @author lionnel cauvy
 * @version 1.0
 * @date 12.II.03
 */

public class BCR extends InformationElement
{
	/**
	 * constructs BCR object<br>
	 * binary counter reading
	 * @param octet1 value of first octet of 'physical' data
	 * @param octet2 second octet
	 * @param octet3 third octet
	 * @param octet4 fourth octet
	 * @param octet5 fourth octet
	 */
	public BCR(short octet1, short octet2, short octet3, short octet4, short octet5)
	{
		super(5,5);
		octetset[0] = octet1;
		octetset[1] = octet2;
		octetset[2] = octet3;
		octetset[3] = octet4;
		octetset[4] = octet5;
		decode();
	}

	/**
	 * builds sub information data from 'physical' data.
	 * called when physical data is all known
	 */
	protected void decode()
	{
		dataset[0] = new Integer(octetset[0] | (octetset[1]<<8) | (octetset[2]<<16) | (octetset[3]<<24));
		dataset[1] = new Byte((byte)(octetset[4] & 0x001F));
		dataset[2] = new Boolean((((octetset[4] & 0x0020)>>5)==0)?false:true);
		dataset[3] = new Boolean((((octetset[4] & 0x0040)>>6)==0)?false:true);
		dataset[4] = new Boolean((((octetset[4] & 0x0080)>>7)==0)?false:true);
		// computes invalidity
		invalidity = (short)(((((Boolean)dataset[2]).booleanValue()?1:0) << 4) | ((((Boolean)dataset[3]).booleanValue()?0:1) << 9) | ((((Boolean)dataset[4]).booleanValue()?1:0) << 1) );
		if (invalidity!=0)invalidity |= 2;
	}

	/**
	 * builds  'physical' data from sub information data.
	 * called when sub information data is all known
	 */
	protected void code()
	{
		octetset[0] = (short)((((Integer)dataset[0]).intValue() & 0x000000FF));
		octetset[1] = (short)((((Integer)dataset[0]).intValue() & 0x0000FF00) >> 8);
		octetset[2] = (short)((((Integer)dataset[0]).intValue() & 0x00FF0000) >> 16);
		octetset[3] = (short)((((Integer)dataset[0]).intValue() & 0xFF000000) >> 24);
		octetset[4] = (short)(((Byte)dataset[1]).byteValue() | ((((Boolean)dataset[2]).booleanValue()?1:0) << 5) | ((((Boolean)dataset[3]).booleanValue()?1:0) << 6) | ((((Boolean)dataset[3]).booleanValue()?1:0) << 7));
	}

	/**
	 * default constructor
	 */
	public BCR()
	{
		super(5,5);
		octetset[0] = octetset[1] = octetset[2] = octetset[3] = octetset[4] = 0x00;
		dataset[0] = dataset[1] = dataset[2] = dataset[3] = dataset[4] = null;
	}

}
