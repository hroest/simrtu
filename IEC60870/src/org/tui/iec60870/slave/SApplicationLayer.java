/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tui.iec60870.slave;

import org.tui.iec60870.common.ApplicationLayer;

import java.util.Enumeration;
import java.util.Hashtable;

/**
 * <p>
 * This class defines an implementation of IEC 608705 application layer from EPA model.<br>
 * Application layer is in charge of:<br>
 * - application service data units (iec 60870-5-3).<br>
 * - information elements (iec 60870-5-4).
 *
 * @author lionnel cauvy
 * @version 1.0
 * @date 01.II.03
 */
public abstract class SApplicationLayer extends ApplicationLayer
{

    public Hashtable manager;
    protected boolean comm_error=false;


    public SApplicationLayer(String Appname, int size) {
        super(Appname);
        manager = new Hashtable(size);
    }

    public void start() {
        launch();
        for (Enumeration e = manager.elements(); e.hasMoreElements();) {
            SAppManager mngr = (SAppManager) e.nextElement();
            mngr.start();
        }
    }

    @Override
    public void init_from_lower() {
    }

    @Override
    public void init_from_upper() {
    }

    @Override
    public void comm_error(boolean val) {
        this.comm_error=val;
    }

}
