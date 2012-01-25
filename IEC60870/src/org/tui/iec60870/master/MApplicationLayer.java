/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tui.iec60870.master;

import org.tui.iec60870.common.ApplicationLayer;

import java.util.Hashtable;
import java.util.Enumeration;

/**
 *
 * @author mikra
 */
public abstract class MApplicationLayer extends ApplicationLayer {

    public Hashtable manager;

    public void start() {
        launch();
        for (Enumeration e = manager.elements(); e.hasMoreElements();) {
            MAppManager mngr = (MAppManager) e.nextElement();
            mngr.start();
        }
    }

    public MApplicationLayer(String Appname, int size) {
        super(Appname);
        manager = new Hashtable(size);
    }

    @Override
    public void init_from_lower() {
    }

    @Override
    public void init_from_upper() {
    }

    @Override
    public void comm_error(boolean val) {
    }


}
