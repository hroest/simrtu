/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tui.iec60870.master101.services;

import org.tui.iec60870.master.MAppService;
import org.tui.iec60870.master.MAppManager;
import org.tui.iec60870.common.DbLocator;
import org.tui.iec60870.common.MapAsdu;
import org.tui.iec60870.common101.AsduKey101;
import org.tui.iec60870.common101.Interoperability101;

/**
 *
 * @author mikra
 */
public class MCmdAppSvc101 extends MAppService {

    /**
	* Information object address.
    */
	public int ioa;
	public byte state;
	public byte qualifier;
	public boolean se;

       /**
	 * Instanciates a new triggered single command asdu.
	 *
	 * @param manager	Application layer manager.
	 * @param notifier	Luciol database notifier.
	 * @param id		Type identification. Single, double, regulating step, ... commands.
	 * @param caa		Common address of asdus.
	 * @param ioa		Information object address to read.
	 * @param act		Activation/Deactivation flag.
	 * @param state		ON/OFF or LOW/HIGH according to id.
	 * @param qualifier	short pulse, long pulse, persistent or no additional...
	 * @param se		False for select, true for execute
         **/

    public MCmdAppSvc101(MAppManager manager, DbLocator notifier, int ioa ,AsduKey101 key, AsduKey101 key2)
    {
        super(new MapAsdu(key),
                manager.lower,
                // database locator as luciol notifier
                notifier,
                // execution context
                manager.casdu);

        this.ioa = ioa;
        this.state = 0;
        this.qualifier = 0;
        this.se = false;
    }


    public MCmdAppSvc101(MAppManager manager, DbLocator notifier, byte id, int caa, int ioa, boolean act, byte state, byte qualifier, boolean se)
    {
        super(
                // asdu to build
                new MapAsdu(
                new AsduKey101(
                id,
                (act) ? Interoperability101.COT_ACTIVATION : Interoperability101.COT_DEACTIVATION,
                caa)),
                // related link service
                manager.lower,
                // database locator as luciol notifier
                notifier,
                // execution context
                manager.casdu);
        this.ioa = ioa;
        this.state = state;
        this.qualifier = qualifier;
        this.se = se;
        //this.notifier = notifier;
    }
}
