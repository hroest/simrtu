/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tui.iec60870.master104;

import org.tui.iec60870.master101.MApplication101;
import org.tui.iec60870.master104.layers.MTransport;
import lib.xml.kXMLElement;

/**
 *
 * @author mikra
 */
public class Master104Session {

    private MApplication101 ACTIVE;
    kXMLElement node;

    public static final String XML_Configuration = "Master104Session";

    public Master104Session(kXMLElement anode) {

        //create new MApplication Layer with Transport Layer for 104 communication
        ACTIVE = new MApplication101(anode, new MTransport(ACTIVE, anode));
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

