/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tui.iec60870.common101.information;

import org.tui.iec60870.common.InformationElement;
import org.tui.iec60870.common.AppService;

import rt_database.DataElement;
import rt_database.Database;
import rt_database.DatabaseApp;
import rt_database.Record;

/**
 * single command 7.2.6.15<br>
 * (information element used by asdus id 45)<br>
 * <p><i>
 * single command state		<b>SCS	--> dataset[0]</b><br>
 * <0> off <br>
 * <1> on <br>
 * <br>
 * BS1 must be 0			<b>BS1	--> dataset[1]</b><br>
 * <br>
 * qualifier of command		<b>QU	--> dataset[2]</b><br>
 * <0> no additional definition <br>
 * <1> short pulse duration (circuit bvreaker), duration determined by a system parameter in the outstation <br>
 * <2> long duration pulse, outstation system parameter <br>
 * <3> persistent output <br>
 * <4..8> reserved for standard definitions (compatible range) <br>
 * <9..15> reserverd for the selection of other predefined functions <br>
 * <16.31> reserved for special use (private range) <br>
 * <br>
 * select/execute			<b>SE	--> dataset[3]</b><br>
 * <0> execute<br>
 * <1> select<br>
 * </i>
 *
 * @author lionnel cauvy
 * @version 1.0
 * @date 12.II.03
 */

public class SCO extends InformationElement
{
	/**
	 * constructs SCO object<br>
	 * @param octet value of 'physical' data
	 */
        AppService app;

	public SCO(short octet)
	{
		super(1,4);
		octetset[0] = octet;
		decode();
	}

	/**
	 * builds sub information data from 'physical' data.
	 * called when physical data is all known
	 */
        @Override
	protected void decode()
	{
		dataset[0] = new Boolean((((octetset[0] & 0x0001))==0)?false:true);
		dataset[1] = new Boolean((((octetset[0] & 0x0002)>>1)==0)?false:true);
		dataset[2] = new Byte((byte)((octetset[0] & 0x007C)>>2));
		dataset[3] = new Boolean((((octetset[0] & 0x0080)>>7)==0)?false:true);
	}

	/**
	 * builds  'physical' data from sub information data.
	 * called when sub information data is all known
	 */
        @Override
	protected void code()
	{
		octetset[0] = (short)((((Boolean)dataset[0]).booleanValue()?1:0) | ((((Boolean)dataset[1]).booleanValue()?1:0) << 1) | (((Byte)dataset[2]).byteValue() << 2) | ((((Boolean)dataset[3]).booleanValue()?1:0) << 7));
	}

	/**
	 * default constructor
	 */
	public SCO()
	{
		super(1,4);
	}

        public SCO(AppService app)
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
                this.octetset[0] = 1;
            } else {
                this.octetset[0] = 0;
            }
        }
        

}
