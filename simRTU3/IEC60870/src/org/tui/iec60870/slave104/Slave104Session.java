/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tui.iec60870.slave104;

import lib.xml.kXMLElement;
import org.tui.iec60870.slave104.layers.STransport;
import org.tui.iec60870.slave101.SApplication101;

/**
 *
 * @author Micha
 */
public class Slave104Session {

    private SApplication101 ACTIVE;
    kXMLElement node;
    public static final String XML_Configuration = "Slave104Session";

    public Slave104Session(kXMLElement anode) {

        //create new MApplication Layer with Transport Layer for 104 communication
        ACTIVE = new SApplication101(anode, new STransport(ACTIVE, anode));
        node = anode;
    }

    public void start() {
        ACTIVE.start();
    }

    public void kill() {
        try {
            ACTIVE.kill();
        } catch (Exception e) {
        }
    }

    public kXMLElement outputToXML() {
        return node;
    }
}
