/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tui.iec60870.master;

import org.tui.iec60870.common.AppService;
import org.tui.iec60870.common.AsduKey;
import org.tui.iec60870.common.DbLocator;
import org.tui.iec60870.common.MapAsdu;
import org.tui.iec60870.common.Layer;

/**
 * <p>
 * This class specializes general application procedure for master application.
 * <p>
 * Master specialization implies add of an expected asdu to be returned by
 * the secondary (slave) station.
 *
 * @author	lionnel cauvy
 * @version	1.0
 * @date	28.II.03
 */
public class MAppService extends AppService
{
	/**
	 *
	 */
	public final static short	DEFAULT_VSQ	= 129;
	public final static int		DEFAULT_CAA	= 1;
	
	/**
	 * <p>
	 * Instanciates a procedure for master application.
	 *
	 * @param asdu			The asdu to send to a remote.
	 * @param svc			The related link service.
	 * @param identifier            The procedure identification.
	 * @param notifier		The database element that will trigger the procedure.
	 * @param context		The execution context. Give current values to update during asdu building.
	 * @param expectedAsdu          Required asdu to validate procedure.
	 */
	public MAppService(MapAsdu asdu, Layer lower, DbLocator notifier, int casdu)
	{
		super(asdu, lower, notifier, casdu);
	}
}
