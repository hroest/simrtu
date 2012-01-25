/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tui.iec60870.slave;

import org.tui.iec60870.common.AppService;
import org.tui.iec60870.common.MapAsdu;
import org.tui.iec60870.common.Layer;

/**
 * <p>
 * This class defines an acknowledgement of general command service asdu.
 * <p>
 * Slave application has to answer such an asdu or a NACK one when receiving
 * a general command from master station.
 *
 * @author	lionnel cauvy
 * @version	1.0
 * @date 19.II.03
 */

public class SDataClass2AppSvc extends AppService {

    /**
     * <p>
     * Instanciates a new asdu service to close general interrogation
     * procedure
     */
    public short cot = 0;

    public SDataClass2AppSvc(SAppManager manager, Layer lower, MapAsdu asdu, int casdu) {
        super(
                asdu,
                // related link service
                lower,
                // no database notification
                null,
                casdu);
    }

    public SDataClass2AppSvc(SAppManager manager, Layer lower, MapAsdu asdu, int casdu, short cot) {
        super(
                asdu,
                // related link service
                lower,
                // no database notification
                null,
                casdu);

        this.cot = cot;
    }
}