/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tui.iec60870.common101.information;

import org.tui.iec60870.common.InformationElement;
import lib.math.Float;

/**
 * IEEE STD 754 7.2.6.8<br>
 * (information element used by asdus id 13,14,36,50,112)<br>
 * <p><i>
 * </i>
 *
 * @author lionnel cauvy
 * @version 1.0
 * @date 12.II.03
 */

public class R32_23 extends InformationElement
{
	//private int fraction; // 2(-23) to 2(-1) (octet 1 ... octet 3)
	//private short exposant; // 2(0) to 2(7) (octet 3 ... octet 4)
	//private boolean signe; // (octet 4 8eme bit)


	/**
	 * constructs R32_23 object<br>
	 * @param octet value of first octet of 'physical' data
	 * @param octet second octet
	 * @param octet third octet
	 * @param octet fourth octet
	 */
	public R32_23(short octet1, short octet2, short octet3, short octet4)
	{
		super(4,1);
		octetset[0] = octet1;
		octetset[1] = octet2;
		octetset[2] = octet3;
		octetset[3] = octet4;
		decode();
	}

	/**
	 * builds sub information data from 'physical' data.
	 * called when physical data is all known
	 */
    @Override
	protected void decode()
	{
		float job = Float.intBitsToFloat(octetset[0]  | (octetset[1]<<8) | (octetset[2] << 16) | (octetset[3]<<24));
                Float value = new Float(job);
                dataset[0] = value;
	}

	/**
	 * builds  'physical' data from sub information data.
	 * called when sub information data is all known
	 */
    @Override
	protected void code()
	{
		float val = ((Float)dataset[0]).floatValue();
                int integer = Float.floatToIntBits(val);
                
		octetset[0] = (short)((integer & 0x000000FF));
		octetset[1] = (short)((integer & 0x0000FF00) >> 8);
		octetset[2] = (short)((integer & 0x00FF0000) >> 16);
		octetset[3] = (short)((integer & 0xFF000000) >> 24);

                decode();
    }

	/**
	 * default constructor
	 */
	public R32_23()
	{
		super(4,1);
	}

}