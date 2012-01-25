/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tui.iec60870.common101.information;

import org.tui.iec60870.common.InformationElement;

/**
 * value with transcient state indication 7.2.6.5<br>
 * (information element used by asdus id 5,6,32)<br>
 * <p><i>
 * value (2.1)<br>
 * 7 bits<br>
 * <b>V	--> dataset[0]</b><br>
 * <br>
 * transient (6)<br>
 * 1 bit<br>
 * <b>T	--> dataset[1]</b><br>
 * </i>
 *
 * @author lionnel cauvy
 * @version 1.0
 * @date 12.II.03
 */

public class VTI extends InformationElement
{
	/**
	 * constructs VTI object<br>
	 * value with transient state indication, can be used for step position of transformers or other step position information
	 * @param octet value of 'physical' data
	 */
	public VTI(short octet)
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
	public VTI()
	{
		super(1,2);
	}

}
