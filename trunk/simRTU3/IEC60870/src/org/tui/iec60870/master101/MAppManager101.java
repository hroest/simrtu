/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tui.iec60870.master101;

import org.tui.iec60870.master.MApplicationLayer;
import org.tui.iec60870.master.MAppManager;

/**
 *
 * @author mikra
 */
public class MAppManager101 extends MAppManager {

    public MAppManager101(MApplicationLayer layer, int casdu)
    {
        super(layer, casdu);
    }

}
