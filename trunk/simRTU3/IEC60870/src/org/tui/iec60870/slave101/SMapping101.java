/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tui.iec60870.slave101;

import org.tui.iec60870.common.AppService;
import org.tui.iec60870.common.AsduKey;
import org.tui.iec60870.common101.Asdu101;
import org.tui.iec60870.common101.AsduKey101;
import org.tui.iec60870.slave.SMapping;

import java.util.Vector;
import java.util.Enumeration;

/**
 * <p>
 * This class defines mapping for slave 101 application.
 *
 * @author lionnel cauvy
 * @version 1.0
 * @date 01.II.03
 */
public class SMapping101 extends SMapping
{
	private Vector counter;
        private String name="";

	public void addCounter(AppService asdu)
	{
		counter.addElement(asdu);
	}
	public Enumeration enumerateCounters()
	{
		return counter.elements();
	}
	/**
	 * <p>
	 * Generates a mapping key. Asdu from 101 protocol contains supplementary
	 * data such as Function Type & Information Number. These attributes are
	 * used to map 101 data to luciol database. So this only interest of this
	 * class is ability to create 101 mapping key.
	 *
	 * @param object	a 101 formatted asdu to turn into mapping key.
	 *
	 * @return	the requested mapping key.
	 */
        @Override
	protected AsduKey instanciateKey(Object object)
	{
		if (object instanceof Asdu101)
		{
			Asdu101 asdu = (Asdu101)object;

			AsduKey101 key = new AsduKey101(
				asdu.id,
				AsduKey101.NOTUSED,
				asdu.caa
			);
			return key;
		}

		return null;
	}
	/**
	 * <p>
	 * Allocates a mapping for slave 101 application.
	 *
	 * @param manager	Application layer manager reference.
	 * @param nbWriter	Number of writer asdus for this application.
	 * @param nbEvent	Number of event asdus.
	 * @param nbGi		Number of general interrogation asdus.
	 * @param nbMeas	Number of measurands asdus.
	 */
	public SMapping101(SAppManager101 manager)
	{
		super(manager);
                this.name = "IEC60870 Slave " + manager.name + " ";
		counter = new Vector();
	}

        @Override
        public String toString()
        {
            return this.name;
        }
}
