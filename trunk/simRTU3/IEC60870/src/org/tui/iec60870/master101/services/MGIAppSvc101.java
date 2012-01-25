/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tui.iec60870.master101.services;

import org.tui.iec60870.master.MAppManager;
import org.tui.iec60870.master.MAppService;
import org.tui.iec60870.common.DbLocator;
import org.tui.iec60870.common101.Interoperability101;
import org.tui.iec60870.common.MapAsdu;
import org.tui.iec60870.common101.AsduKey101;

/**
 *
 * @author Micha
 */
public class MGIAppSvc101 extends MAppService {

    /**
     * Qualifier of interrogation QOI.
     */
    public short qoi;
    public int ioa = 0;

    /**
     * <p>
     * Allocates a new procedure for remote station general interrogation triggered
     * by database notifier.
     *
     * @param manager	Application layer manager.
     * @param notifier	Database notifier.
     * @param qoi		Qualifier of interrogation. Global:0, Group1:1, ..., Group16:16
     */
    public MGIAppSvc101(MAppManager manager, DbLocator notifier, int caa, short qoi) {

        super(
                // asdu to build
                new MapAsdu(
                new AsduKey101(
                Interoperability101.ID_C_IC_NA_1,
                Interoperability101.COT_ACTIVATION,
                caa)),
                manager.lower,
                
                // Database notifier
                notifier,
                // Execution context
                manager.casdu);
        this.qoi = (short) (qoi + 20);
    }
}
