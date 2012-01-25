/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tui.iec60870.common101.information;

import org.tui.iec60870.common.InformationElement;

/**
 * qualifier of command 7.2.6.26<br>
 * (information element used by asdus id 45,46,47)<br>
 * <p><i>
 * RESERVE					<b>RES	--> dataset[0]</b><br>
 * <br>
 * qualifier of command		<b>QU	--> dataset[1]</b><br>
 * <0> no additional definition <br>
 * <1> short pulse duration (circuit bvreaker), duration determined by a system parameter in the outstation <br>
 * <2> long duration pulse, outstation system parameter <br>
 * <3> persistent output <br>
 * <4..8> reserved for standard definitions (compatible range) <br>
 * <9..15> reserverd for the selection of other predefined functions <br>
 * <16.31> reserved for special use (private range) <br>
 * <br>
 * select/execute			<b>SE	--> dataset[2]</b><br>
 * <0> execute<br>
 * <1> select<br>
 * </i>
 *
 * @author lionnel cauvy
 * @version 1.0
 * @date 12.II.03
 */

public class QOC extends InformationElement
{
	/**
	 * constructs QOC object<br>
	 * qualifier of command
	 * @param octet value of 'physical' data
	 */
	public QOC(short octet)
	{
		super(1,3);
		octetset[0] = octet;
		decode();
	}

	/**
	 * builds sub information data from 'physical' data.
	 * called when physical data is all known
	 */
	protected void decode()
	{
		dataset[0] = new Byte((byte)(octetset[0] & 0x03));
		dataset[1] = new Byte((byte)((octetset[0] & 0x007C)>>2));
		dataset[2] = new Boolean((((octetset[0] & 0x0080)>>7)==0)?false:true);
	}

	/**
	 * builds  'physical' data from sub information data.
	 * called when sub information data is all known
	 */
	protected void code()
	{
		octetset[0] = (short) ((((Byte)dataset[0]).byteValue()) |(((Byte)dataset[1]).byteValue() << 2) | ((((Boolean)dataset[2]).booleanValue()?1:0) << 7) );
	}

	/**
	 * default constructor _ called by asdu writer?
	 */
	public QOC()
	{
		super(1,3);
	}

}
