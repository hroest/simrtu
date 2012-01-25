/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tui.iec60870.slave101.services;

/**
 *
 * @author Michael
 */
import org.tui.iec60870.slave101.*;
import org.tui.iec60870.common.MapAsdu;
import org.tui.iec60870.common101.AsduKey101;
import org.tui.iec60870.common101.Interoperability101;
import org.tui.iec60870.slave.services.SDataAppSvc;

public class SEIAppSvc101 extends SDataAppSvc {

    /**
     * Cause of initialization. See common101.information.COI
     */
    public short coi = 2;

    /**
     * <p>
     * Allocates a new procedure for remote station general interrogation triggered
     * by database notifier.
     *
     * @param manager	Application layer manager.
     * @param caa		Common address of asdu.
     */
    public SEIAppSvc101(SAppManager101 manager, int caa) {
        super(
                manager,
                // asdu to build
                new MapAsdu(
                new AsduKey101(
                Interoperability101.ID_M_EI_NA_1,
                Interoperability101.COT_INITIALIZED,
                caa)));
    }
}
