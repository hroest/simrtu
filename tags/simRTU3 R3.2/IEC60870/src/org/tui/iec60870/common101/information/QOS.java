/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tui.iec60870.common101.information;

import org.tui.iec60870.common.InformationElement;

/**
 * qualifier of set point command 7.2.6.39<br>
 * (information element used by asdus id 48,49,50)<br>
 * <p><i>
 * qualifier (1.1)	<b>QL	--> dataset[0]</b><br>
 * <0> default <br>
 * <1..63> compatible range <br>
 * <64.127> special use (private range) <br>
 * <br>
 * select/execute (6)	<b>SE	--> dataset[1]</b><br>
 * <0> execute <br>
 * <1> select <br>
 * </i>
 *
 * @author lionnel cauvy
 * @version 1.0
 * @date 12.II.03
 */

public class QOS extends InformationElement
{
	/**
	 * constructs QOS object<br>
	 * qualifier of set point comand
	 *
	 * @param octet value of 'physical' data
	 */
	public QOS(short octet)
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
		invalidity = (short)(((((Byte)dataset[0]).byteValue()==0)?1:0) << 1);
		if (invalidity!=0 )invalidity |= 2;
	}

	/**
	 * builds  'physical' data from sub information data.
	 * called when sub information data is all known
	 */
	protected void code()
	{
		octetset[0] = (short) ((((Byte)dataset[0]).byteValue()) | ((((Boolean)dataset[1]).booleanValue()?1:0) << 7) );
	}

	/**
	 * default constructor _ called by asdu writer?
	 */
	public QOS()
	{
		super(1,2);
		octetset[0] = 0x00;
	}

}
