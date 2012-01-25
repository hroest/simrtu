/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tui.iec60870.common104;

/**
 *
 * @author mikra
 */
public interface LnkFeatures {
        public byte Start = 0x68;
        public byte TESTFR_CON = (byte) 0x83;
        public byte TESTFR_ACT = 0x43;
        
        //not supportedsi
        public byte STOPDT_CON = 0x23;
        public byte STOPDT_ACT = 0x13;
        public byte STARTDT_CON = 0xB;
        public byte STARTDT_ACT = 0x7;

        public int sleep = 100;
}
