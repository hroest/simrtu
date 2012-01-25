/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tui.iec60870.common;

/**
 * <p>
 * This class defines general application procedure.
 * <p>
 * AppService is handled by application layer and manager. It can be triggered
 * in an event way by database notification, must be able to build an asdu and
 * related link service in order to construct a compliant frame to send to a
 * remote.
 * <p>
 * REMEMBER AppService ONLY CONCERNS APPLICATION LAYER. AT LINK LAYER LEVEL, WE
 * USE LnkService OBJECTS.
 *
 * @author	lionnel cauvy
 * @version	1.0
 * @date	28.II.03
 */
public class AppService
{
	public final static int		NOTUSED		= -1;
	/**
	 * <p>
	 * Database locator to enable launching of this procedure by
	 * external process via a database notification. For each
	 * procedure, application MAY record a database event listener.
	 * Notice that it can be null for internal use.
	 */
	public DbLocator notifier;

        public String id;

        public boolean to_central=false;

	public MapAsdu asdu;
	/**
	 * <p>
	 * The link service associated with this procedure.
	 */
	public Layer lower;
	/**
	 * <p>
	 * Execution context. This is used to give some current values possibly
	 * needed to build an asdu.
	 */
	public int casdu;

        public short cot;

	/**
	 * <p>
	 * Instanciates a procedure object with an asdu to build.
	 *
	 * @param asdu			The asdu to send to a remote.
	 * @param svc			The related link service.
	 * @param notifier		The database element that will trigger the procedure.
	 * @param context		The execution context. Give current values to update during asdu building.
	 */

	public AppService(MapAsdu asdu, Layer lower, DbLocator notifier, int casdu)
	{
            this.asdu = asdu;
            this.lower = lower;
            this.notifier = notifier;
            this.casdu = casdu;
	}

        public AppService(AppService svc)
        {
            this.asdu=new MapAsdu(svc.asdu);
            this.lower=svc.lower;
            this.notifier=svc.notifier;
            this.casdu=svc.casdu;
            this.cot=svc.cot;
            this.to_central=svc.to_central;
        }

}
