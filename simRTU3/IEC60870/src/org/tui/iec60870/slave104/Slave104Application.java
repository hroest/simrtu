/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tui.iec60870.slave104;

import lib.interfaces.application.Configurable;
import lib.xml.kXMLElement;

import java.util.Enumeration;

/**
 *
 * @author Micha
 */
public class Slave104Application implements Configurable {

    Slave104Session[] slave104sessions;
    private static final Slave104Application INST = new Slave104Application();
    public static final String XML_Configuration = "Slave104";

    kXMLElement node;

    public static Slave104Application getInstance() {
        return INST;
    }

    @Override
    public kXMLElement outputToXML() {
       return node;
    }

    @Override
    public void inputFromXML(kXMLElement xml) {
       this.node=xml;
    }

     public void restart_app()
    {
        int count = 0;

         //no slave station definied
        if (node==null)
        {
            return;
        }

        for (Enumeration e = node.enumerateChildren(); e.hasMoreElements();) {
            kXMLElement child = (kXMLElement) e.nextElement();
            if (child.getTagName().equals(Slave104Session.XML_Configuration)) {
                count++;
            }
        }

        if (slave104sessions != null) {
            for (int ii = 0; ii < slave104sessions.length; ii++) {
                if (slave104sessions[ii] != null) {
                    slave104sessions[ii].kill();
                }
            }
        }

       slave104sessions = new Slave104Session[count];

        int ii = 0;
        for (Enumeration e = node.enumerateChildren(); e.hasMoreElements();) {
            kXMLElement child = (kXMLElement) e.nextElement();
            if (child.getTagName().equals(Slave104Session.XML_Configuration)) {
                slave104sessions[ii] = new Slave104Session(child);
                slave104sessions[ii].start();
                ii++;
            }
        }
    }

}
