/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tui.iec60870.common101.information;

import org.tui.iec60870.common.InformationElement;

import java.util.Calendar;

/**
 * CP16Time2a two octet binary time 7.2.6.20<br>
 * (information element used by asdus id 17,18,19,38,39,40)<br>
 * <p><i>
 * milliseconds <br>
 * 2 octets <br>
 * Milliseconds	--> dataset[0] <br>
 * </i>
 *
 * @author lionnel cauvy
 * @version 1.0
 * @date 12.II.03
 */

public class CP16Time2a extends InformationElement
{
	/**
	 * constructs CP16Time2a object (reader factory)<br>
	 * two octet binary time 7.2.6.19
	 * @param octet value of first octet of 'physical' data
	 * @param octet2 second octet
	 */
	public CP16Time2a(short octet1,
	                  short octet2)
	{
		super(2,1);
		octetset[0] = octet1;
		octetset[1] = octet2;
		decode();
	}

	/**
	 * builds sub information data from 'physical' data.
	 * called when physical data is all known
	 */
	protected void decode()
	{
		dataset[0] = new Integer(octetset[0] | (octetset[1] << 8));
	}

	/**
	 * builds  'physical' data from sub information data.
	 * called when sub information data is all known
	 */
	protected void code()
	{
		octetset[0] = (short)(((Integer)dataset[0]).intValue() & 0xFF);
		octetset[1] = (short)((((Integer)dataset[0]).intValue() & 0xFF00)>>8);
	}

	/**
	 * constructs CP16Time2a object (writer factory)<br>
	 * two octet binary time 7.2.6.19
	 */
	public CP16Time2a()
	{
		super(2,1);
		Calendar calendar = Calendar.getInstance();
		dataset[0] = new Integer(calendar.get(Calendar.MILLISECOND)+1000*calendar.get(Calendar.SECOND));
		code();
	}

}
