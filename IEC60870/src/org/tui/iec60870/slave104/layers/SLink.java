/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tui.iec60870.slave104.layers;

import org.tui.iec60870.common.LayerThread;
import lib.xml.kXMLElement;
import org.tui.iec60870.common104.Layers.Link;

/**
 *
 * @author Michal Kratz
 * @version 1.0
 */

/*
 * Specialized Class for 104 Protocol OSI Stack
 *
 */
public class SLink extends Link {
    public SLink(LayerThread parent, kXMLElement anode) {
        super("SLink" + anode.getProperty("name", "unbekannt"));
        upper = parent;
        lower = new SEthernet(this, anode);
    }
}