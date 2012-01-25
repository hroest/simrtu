/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tui.iec60870.common101.information;

import org.tui.iec60870.common.InformationElement;

import java.util.Calendar;

/**
 * <p>
 * CP56Time2a seven octet binary time<br>
 * (information element used by asdus id 30,31,32,33,34,35,36,37,38,39,40,103,126)<br>
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
 * <br>
 * hours <br>
 * 5 bits <br>
 * Hours		--> dataset[4] <br>
 * <br>
 * Reserved 2 <br>
 * 2 bits <br>
 * RES2			--> dataset[5] <br>
 * <br>
 * summer time <br>
 * 1 bit <br>
 * SU			--> dataset[6] <br>
 * <br>
 * day of month <br>
 * 5 bits <br>
 * Month_day	--> dataset[7] <br>
 * <br>
 * day of week <br>
 * 3 bits MUST BE ZERO!<br>
 * Week_day		--> dataset[8] <br>
 * <br>
 * months <br>
 * 4 bits <br>
 * Months		--> dataset[9] <br>
 * <br>
 * Reserved 3 <br>
 * 4 bits <br>
 * RES3			--> dataset[10] <br>
 * <br>
 * years <br>
 * 7 bits <br>
 * Years		--> dataset[11] <br>
 * <br>
 * Reserved 4 <br>
 * 1 bit <br>
 * RES4			--> dataset[12] <br>
 * </i>
 *
 * @author lionnel cauvy
 * @version 1.0
 * @date 12.II.03
 */

public class CP56Time2a extends InformationElement
{
	/**
	 * constructs CP56Time2a object (reader factory)<br>
	 * seven octet binary time 7.2.6.29
	 * @param octet1 value of first octet of 'physical' data
	 * @param octet1 second octet
	 * @param octet1 third octet
	 * @param octet1 fourth octet
	 * @param octet1 fifth octet
	 * @param octet1 sixth octet
	 * @param octet7 seventh octet
	 */
	public CP56Time2a(short octet1,
	                  short octet2,
					  short octet3,
					  short octet4,
					  short octet5,
					  short octet6,
					  short octet7)
	{
		super(7,13);
		octetset[0] = octet1;
		octetset[1] = octet2;
		octetset[2] = octet3;
		octetset[3] = octet4;
		octetset[4] = octet5;
		octetset[5] = octet6;
		octetset[6] = octet7;
		decode();
	}

        @Override
	protected void decode()
	{
		// milliseconds
		dataset[0] = new Integer(octetset[0] | (octetset[1] << 8));
		// minutes
		dataset[1] = new Byte((byte) ( octetset[2] & 0x3F ));
		// reserve1
		dataset[2] = new Boolean(((( octetset[2] & 0x40 ) >> 6)== 1)?true:false);
		// invalid
		dataset[3] = new Boolean(((( octetset[2] & 0x80 ) >> 7) == 1)?true:false);
		// hours
		dataset[4] = new Byte((byte) ( octetset[3] & 0x1F ));
		// reserve2
		dataset[5] = new Byte((byte) ( ( octetset[3] & 0x60 ) >> 5 ));
		// summer time
		dataset[6] = new Boolean(((( octetset[3] & 0x80 ) >> 7) == 1)?true:false);
		// day of month
		dataset[7] = new Byte((byte) ( octetset[4] & 0x1F ));
		// day of week
		dataset[8] = new Byte((byte) ( ( octetset[4] & 0xE0 ) >> 5 ));
		// months
		dataset[9] = new Byte((byte) ( octetset[5] & 0x0F ));
		// reserve3
		dataset[10] = new Byte((byte) ( ( octetset[5] & 0xF0 ) >> 4 ));
		// years
		dataset[11] = new Byte((byte) ( octetset[6] & 0x7F ));
		// reserve4
		dataset[12] = new Boolean(((( octetset[6] & 0x80 ) >> 7) == 1)?true:false);
		// computes invalidity
		invalidity = (short)((((Boolean)dataset[3]).booleanValue()?1:0) << 1);
		if (invalidity!=0)invalidity |= 2;
	}

	/**
	 * builds  'physical' data from sub information data.
	 * called when sub information data is all known
	 */

        @Override
	protected void code()
	{
		octetset[0] = (short)(((Integer)dataset[0]).intValue() & 0xFF); //Millisecond
		octetset[1] = (short)((((Integer)dataset[0]).intValue() & 0xFF00)>>8); //Millisecond
		octetset[2] = (short)( ((Byte)dataset[1]).byteValue()); // Minutes
		octetset[3] = (short)( ((Byte)dataset[4]).byteValue() | (((((Boolean)dataset[6]).booleanValue())?1:0)<<7) ); //summertime
		octetset[4] = (short)( (((Byte)dataset[7]).byteValue()) | (((Byte)dataset[8]).byteValue()<<5)); // weekday day of month
		octetset[5] = (short)( (((Byte)dataset[9]).byteValue()) | (((Byte)dataset[10]).byteValue()<<4)); // month
                octetset[6] = (short)( (((Byte)dataset[11]).byteValue())); // year
	}

	/**
	 * constructs CP56Time2a object (writer factory)<br>
	 * seven octet binary time 7.2.6.29
	 */
	public CP56Time2a()
	{

                //CP56Time2a := CP56 {Millisekunden, Minuten, RES1, ungültig, Stunden, RES2, Sommerzeit,
                //                Monatstag, Wochentag, Monat, RES3, Jahre, RES4}

            super(7,13);
		Calendar calendar = Calendar.getInstance();

                dataset[0] = new Integer(calendar.get(Calendar.MILLISECOND)+1000*calendar.get(Calendar.SECOND));
		dataset[1] = new Byte((byte)calendar.get(Calendar.MINUTE));
		dataset[2] = new Boolean(false);
		dataset[3] = new Boolean(false);
		dataset[4] = new Byte((byte)calendar.get(Calendar.HOUR_OF_DAY));
		dataset[5] = new Boolean(false);
		dataset[6] = new Boolean(true); //summertime
		dataset[7] = new Byte((byte)calendar.get(Calendar.DAY_OF_MONTH));
		dataset[8] = new Byte((byte)calendar.get(Calendar.DAY_OF_WEEK));
		dataset[9] = new Byte((byte)(calendar.get(Calendar.MONTH)+1));
		dataset[10] = new Byte((byte)0x00);
		
                int year = calendar.get(Calendar.YEAR);
                
                year = Math.round((float)Math.IEEEremainder(year, 100));
                
                dataset[11] = new Byte((byte)(year));
		dataset[12] = new Boolean(false);
		code();
	}

}
