/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tui.iec60870.slave101.services;

import org.tui.iec60870.slave101.*;
import org.tui.iec60870.common.MapAsdu;
import org.tui.iec60870.common101.AsduKey101;
import org.tui.iec60870.common101.Interoperability101;
import org.tui.iec60870.slave.services.SDataAppSvc;

/**
 *
 * @author Michael
 */
public class SEvtAppSvc101 extends SDataAppSvc{

    public int ioa = 0;

    /**
     * <p>
     * Allocates a new procedure for remote station general interrogation triggered
     * by database notifier.
     *
     * @param manager	Application layer manager.
     * @param caa		Common address of asdus.
     * @param cot		Activation confirmation or activation termination.
     */
    public SEvtAppSvc101(SAppManager101 manager, int caa, byte cot) {
        super(
                manager,
                // asdu to build
                new MapAsdu(
                new AsduKey101(
                Interoperability101.ID_C_IC_NA_1,
                cot,
                caa)));
    }

}
