/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tui.iec60870.common101.information;

import org.tui.iec60870.common.InformationElement;

/**
 * status + status change direction 7.2.6.40<br>
 * (information element used by asdus id 20)<br>
 * <p><i>
 * status bit in pos n							<b>ST	--> dataset[0]</b><br>
 * STn <0> off <br>
 * STn <1> on <br>
 * <br>
 * status change detection bit in bit pos n+16	<b>CD	--> dataset[1]</b><br>
 * CDn <0> no status change detected since last reported <br>
 * CDn <1> at least one status change detected since last reported <br>
 * </i>
 *
 * @author lionnel cauvy
 * @version 1.0
 * @date 12.II.03
 */

public class SCD extends InformationElement
{
	/**
	 * constructs SCD object<br>
	 * @param octet1 value of first octet of 'physical' data
	 * @param octet2 second octet
	 * @param octet3 third octet
	 * @param octet4 fourth octet
	 */
	public SCD(short octet1, short octet2, short octet3, short octet4)
	{
		super(4,2);
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
	protected void decode()
	{
		dataset[0] = new Integer((octetset[0] | (octetset[1] << 8)));
		dataset[1] = new Integer((octetset[2] | (octetset[3] << 8)));
	}

	/**
	 * builds  'physical' data from sub information data.
	 * called when sub information data is all known
	 */
	protected void code()
	{
		octetset[0] = (short)((((Integer)dataset[0]).intValue() & 0x000000FF));
		octetset[1] = (short)((((Integer)dataset[0]).intValue() & 0x0000FF00) >> 8);
		octetset[2] = (short)((((Integer)dataset[1]).intValue() & 0x000000FF));
		octetset[3] = (short)((((Integer)dataset[1]).intValue() & 0x0000FF00) >> 8);
	}

	/**
	 * default constructor
	 */
	public SCD()
	{
		super(4,2);
	}

}
