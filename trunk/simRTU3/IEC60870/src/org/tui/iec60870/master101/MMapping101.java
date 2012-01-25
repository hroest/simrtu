/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tui.iec60870.master101;

import org.tui.iec60870.common.AsduKey;
import org.tui.iec60870.common101.Asdu101;
import org.tui.iec60870.common101.AsduKey101;
import org.tui.iec60870.master.MAppManager;
import org.tui.iec60870.master.MMapping;

/**
 * <p>
 * This class specializes single station mapping for
 * 101 master & slave applications. It only implements
 * instanciateKey method to match with 101 asdu format.
 *
 * @author lionnel cauvy
 * @version 1.0
 * @date 18.II.03
 */

public class MMapping101 extends MMapping
{
	/**
	 * Generates a mapping key. Asdu from 101 protocol contains supplementary
	 * data Originator Address. This attribute is used to map 101 data to luciol
	 * database. So only interest of this class is ability to create 101 mapping
	 * key.
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
	 * Allocates a mapping for 101 master application according to
	 * a single remote station identified in Network by its address.
	 */
	public MMapping101(MAppManager manager)
	{
		super(manager);
	}

}
