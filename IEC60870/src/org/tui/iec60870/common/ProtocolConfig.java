/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tui.iec60870.common;

import lib.xml.kXMLElement;

/**
 *
 * @author Micha
 */
public class ProtocolConfig {

    //Standard 101 Configs
    public byte casdusize=2;
    public byte ioasize  =2;
    public byte cotsize  =2;

    //Polling time and general interrogation time
    public int  gicycle = 1;

    //address of remote station
    public int  casdu = 5;

    //polling time in s
    public int polling=10;

    public ProtocolConfig(kXMLElement anode)
    {
       inputFromXML(anode) ;
    }

    private void inputFromXML(kXMLElement anode)
    {
        casdusize   = (byte)anode.getProperty("casdusize", 2);
        ioasize     = (byte)anode.getProperty("ioasize", 3);
        cotsize     = (byte)anode.getProperty("cotsize", 2);
        gicycle     = anode.getProperty("gicycle", 5);
        casdu       = anode.getProperty("caa", 5);
        polling     = anode.getProperty("polling",10);
    }
}
