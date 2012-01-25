/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package iec60870;

import org.openide.modules.ModuleInstall;
import lib.xml.kXMLElement;
import org.tui.iec60870.IEC60870App;


/**
 * Manages a module's lifecycle. Remember that an installer is optional and
 * often not needed at all.
 */
public class Installer extends ModuleInstall {

    @Override
    public void restored() {
        if (read_app()) {
           IEC60870App.ACTIVE.restart_app();
       }
    }

    @Override
    public void close() {
        
    }

    private boolean read_app() {

            IEC60870App.msg("Lese IEC60870 Konfigruation.", "Installer", 1);

            kXMLElement xml;

            try
            {
                xml = Config.read();
            }
            catch(Exception e)
            {
                IEC60870App.err("IEC60870 Fehler beim Lesen der Konfiguration. Services werden nicht gestartet: " + e, "Installer", 3);
                return false;
            }

            try
            {
                IEC60870App.ACTIVE.inputFromXML(xml);
            }
            catch(Exception e)
            {
                IEC60870App.err("Konfiguration IEC60870 fehlerhaft. Services werden nicht gestartet: " + e, "Installer", 3);
                return false;
            }

            return true;
    }
}
