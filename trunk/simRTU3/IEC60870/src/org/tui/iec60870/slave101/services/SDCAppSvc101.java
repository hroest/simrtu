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
import org.tui.iec60870.common.Asdu;

/**
 *
 * @author Micha
 */
public class SDCAppSvc101 extends SDataAppSvc {

    public int ioa = 0;
    public Asdu raw_asdu=null;

    /**
     * <p>
     * Allocates a new procedure for Double Command Confirmation Purposes
     *
     * @param manager	Application layer manager.
     * @param caa		Common address of asdus.
     * @param cot		Activation confirmation or activation termination.
     */
    public SDCAppSvc101(SAppManager101 manager, int caa, byte cot, Asdu asdu) {

        super(
                manager,
                // asdu to build
                new MapAsdu(
                new AsduKey101(Interoperability101.ID_C_DC_NA_1, (byte)0, caa)));
         this.raw_asdu=asdu;
         this.cot=cot;
         this.id ="dc";
         // purpose is to determine wheater an appservice send to or recieves from central station
         this.to_central=true;
    }

}
