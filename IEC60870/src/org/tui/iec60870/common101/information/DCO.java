/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tui.iec60870.common101.information;

import org.tui.iec60870.common.AppService;

import org.tui.iec60870.common.InformationElement;
import rt_database.DataElement;
import rt_database.Database;
import rt_database.DatabaseApp;
import rt_database.Record;

/**
 * <p>
 * double command 7.2.6.16<br>
 * (information element used by asdus id 46)<br>
 * <p><i>
 * double command state		<b>DCS	--> dataset[0]</b><br>
 * <0> not permitted <br>
 * <1> off <br>
 * <2> on <br>
 * <3> not permitted <br>
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

public class DCO extends InformationElement
{
	AppService app;

        /**
	 * constructs DCO object<br>
	 * double command state
	 * @param octet value of 'physical' data
	 */
	public DCO(short octet)
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
		Byte tmp = new Byte((byte)(octetset[0] & 0x0003));
                dataset[0] = new Byte((byte)tmp);
                dataset[1] = new Byte((byte)((octetset[0] & 0x007C)>>2));
		dataset[2] = new Boolean((((octetset[0] & 0x0080)>>7)==0)?false:true);
		// computes invalidity
		invalidity = (short)( ( (((Byte)dataset[0]).byteValue()==0) || (((Byte)dataset[0]).byteValue()==3)?1:0) << 1);
		if (invalidity!=0 )invalidity |= 2;
	}

	/**
	 * builds  'physical' data from sub information data.
	 * called when sub information data is all known
	 */
	protected void code()
	{
		octetset[0] = (short)(((Byte)dataset[0]).byteValue() | (((Byte)dataset[1]).byteValue() << 2) | ((((Boolean)dataset[2]).booleanValue()?1:0) << 7));
	}

	/**
	 * default constructor
	 */
	public DCO()
	{
		super(1,3);
	}

        public DCO(AppService app)
        {
            super(1,4);
            this.app=app;
            update();
        }

        private void update()
        {
            //reads from database value
            Database base = DatabaseApp.ACTIVE;
            DataElement elt = base.getDataElement(this.app.notifier.index);
            if (elt == null) {
                return;
            }
            Record rec = elt.lastRecord();
            boolean val = rec.booleanValue();
            if (val) {
                this.octetset[0] = 2;
            } else {
                this.octetset[0] = 1;
            }
        }

}
