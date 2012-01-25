/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tui.iec60870.common101.information;

import org.tui.iec60870.common.InformationElement;

import java.util.Calendar;

/**
 * CP24Time2a three octet binary time 7.2.6.19<br>
 * (information element used by asdus id 2,4,6,8,10,12,14,16,17,18,19)<br>
 * <p><i>
 * milliseconds <br>
 * 2 octets <br>
 * Milliseconds	--> dataset[0] <br>
 * <br>
 * minutes <br>
 * 6 bits <br>
 * Minutes		--> dataset[1] <br>
 * <br>
 * Reserved 1 <br>
 * 1 bit <br>
 * RES1			--> dataset[2] <br>
 * <br>
 * invalid <br>
 * 1 bit <br>
 * IV			--> dataset[3] <br>
 * </i>
 *
 * @author lionnel cauvy
 * @version 1.0
 * @date 12.II.03
 */

public class CP24Time2a extends InformationElement
{
	/**
	 * constructs CP24Time2a object (reader factory)<br>
	 * three octet binary time 7.2.6.19
	 * @param octet1 value of first octet of 'physical' data
	 * @param octet2 second octet
	 * @param octet3 third octet
	 */
	public CP24Time2a(short octet1,
	                  short octet2,
					  short octet3)
	{
		super(3,4);
		octetset[0] = octet1;
		octetset[1] = octet2;
		octetset[2] = octet3;
		decode();
	}


	/**
	 * builds sub information data from 'physical' data.
	 * called when physical data is all known
	 */
	protected void decode()
	{
		// milliseconds
		dataset[0] = new Integer(octetset[0] | (octetset[1] << 8));
		// minutes
		dataset[1] = new Byte((byte) ( octetset[2] & 0x3F ));;
		// reserve1
		dataset[2] = new Boolean(((( octetset[2] & 0x40 ) >> 6)== 1)?true:false);
		// invalid
		dataset[3] = new Boolean(((( octetset[2] & 0x80 ) >> 7) == 1)?true:false);
		// computes invalidity
		invalidity = (short)((((Boolean)dataset[3]).booleanValue()?1:0) << 1);
		if (invalidity!=0) invalidity |= 2;
	}

	/**
	 * builds  'physical' data from sub information data.
	 * called when sub information data is all known
	 */
	protected void code()
	{
		octetset[0] = (short)(((Integer)dataset[0]).intValue() & 0xFF);
		octetset[1] = (short)((((Integer)dataset[0]).intValue() & 0xFF00)>>8);
		octetset[2] = (short)( ((Byte)dataset[1]).byteValue() | ((((Boolean)dataset[2]).booleanValue()?1:0)<<6) | ((((Boolean)dataset[3]).booleanValue()?1:0)<<7) );
	}

	/**
	 * constructs CP24Time2a object (writer factory)<br>
	 * three octet binary time 7.2.6.19
	 */
	public CP24Time2a()
	{
		super(3,4);
		Calendar calendar = Calendar.getInstance();
		dataset[0] = new Integer(calendar.get(Calendar.MILLISECOND)+1000*calendar.get(Calendar.SECOND));
		dataset[1] = new Byte((byte)calendar.get(Calendar.MINUTE));
		dataset[2] = new Boolean(false);
		dataset[3] = new Boolean(false);
		code();
	}

}
