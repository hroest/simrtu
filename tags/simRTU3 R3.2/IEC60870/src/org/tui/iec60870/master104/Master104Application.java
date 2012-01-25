/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tui.iec60870.master104;

import lib.interfaces.application.Configurable;
import lib.xml.kXMLElement;

import java.util.Enumeration;

/**
 *
 * @author Micha
 */
public class Master104Application implements Configurable {

    Master104Session[] master104sessions;
    private static final Master104Application INST = new Master104Application();
    public static final String XML_Configuration = "Master104";

    kXMLElement node;

    @Override
    public kXMLElement outputToXML() {
        return node;
    }

    @Override
    public void inputFromXML(kXMLElement xml) {
        this.node=xml;        
    }

    public static Master104Application getInstance() {
        return INST;
    }

    public void restart_app()
    {
        int count = 0;
        
        //no master station definied
        if (node==null)
        {
            return;
        }
        for (Enumeration e = node.enumerateChildren(); e.hasMoreElements();) {
            kXMLElement child = (kXMLElement) e.nextElement();
            if (child.getTagName().equals(Master104Session.XML_Configuration)) {
                count++;
            }
        }
        
        if (master104sessions != null) {
            for (int ii = 0; ii < master104sessions.length; ii++) {
                if (master104sessions[ii] != null) {
                    master104sessions[ii].kill();
                }
            }
        }

        master104sessions = new Master104Session[count];

        int ii = 0;
        for (Enumeration e = node.enumerateChildren(); e.hasMoreElements();) {
            kXMLElement child = (kXMLElement) e.nextElement();
            if (child.getTagName().equals(Master104Session.XML_Configuration)) {
                master104sessions[ii] = new Master104Session(child);
                master104sessions[ii].start();
                ii++;
            }
        }
    }

}
