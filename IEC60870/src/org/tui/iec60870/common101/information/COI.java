/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tui.iec60870.common101.information;

import org.tui.iec60870.common.InformationElement;

/**
 * cause of initialization 7.2.6.21<br>
 * (information element used by asdus id 70)<br>
 * <p><i>
 * <b>UI7	--> dataset[0]</b> <br>
 * <0> local power switch on <br>
 * <1> local manual reset <br>
 * <2> remote reset <br>
 * <3..31> compatible range <br>
 * <32.127> private range <br>
 * <br>
 * <b>BS1	--> dataset[1]</b> <br>
 * <0> init with unchanged local params <br>
 * <1> init after change of local params <br>
 * </i>
 *
 * @author lionnel cauvy
 * @version 1.0
 * @date 12.II.03
 */

public class COI extends InformationElement
{
	/**
	 * constructs COI object<br>
	 * cause of initialization
	 *
	 * @param octet value of 'physical' data
	 */
	public COI(short octet)
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
		octetset[0] = (short)(((Byte)dataset[0]).byteValue() | ((((Boolean)dataset[1]).booleanValue()?1:0) << 7));
	}

	/**
	 * default constructor
	 */
	public COI()
	{
		super(1,2);
		octetset[0] = 0;
		dataset[0] = dataset[1] = null;
	}

}
