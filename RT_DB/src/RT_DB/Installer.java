/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package RT_DB;

import org.openide.modules.ModuleInstall;
import rt_database.DatabaseApp;
import lib.xml.kXMLElement;


/**
 * Manages a module's lifecycle. Remember that an installer is optional and
 * often not needed at all.
 */
public class Installer extends ModuleInstall {

    @Override
    public void restored() {
        //** starts app **//

        //read config
        read_app();
        DatabaseApp.getInstance().start();
        DatabaseApp.ready=true;
    }

    private void save_app() {

        DatabaseApp.msg("Speichere Konfiguration", "Installer", 1);
       
        try
        {
            kXMLElement xml = DatabaseApp.ACTIVE.outputToXML();
            Config.save(xml);
        }
        catch(Exception e)
        {
           DatabaseApp.err("Kann Konfigurationsdatei nicht laden", "Installer", 3);
        }
    }

    private void read_app() {

            DatabaseApp.msg("Lese Konfiguration", "Installer", 1);

            kXMLElement xml;

            try
            {
                xml = Config.read();
                DatabaseApp.getInstance().activeConfiguration().inputFromXML(xml);
            }
            catch(Exception e)
            {
                DatabaseApp.msg("Erzeuge Standardatenbank", "Installer", 2);
                xml = new kXMLElement();
                xml.setTagName("Database");
                xml.addProperty("fileId", "simRTU default Database");
                xml.addProperty("version", "1.0");
                xml.addProperty("date", "01.01.2010");
                kXMLElement xml_di = new kXMLElement();
                xml_di.parseString("\"<DigitalInput logic=\"1\" desc=\"DigitalInput\" id=\"1\"/>\"");
                xml.addChild(xml_di);
                kXMLElement xml_do = new kXMLElement();
                xml_do.parseString("\"<DigitalOutput logic=\"1\" desc=\"DigitalOutput\" id=\"2\"/>\"");
                xml.addChild(xml_do);
                kXMLElement xml_ai = new kXMLElement();
                xml_ai.parseString("\"<AnalogOutput desc=\"AnalogInput\" id=\"3\"/>\"");
                xml.addChild(xml_ai);
                kXMLElement xml_ao = new kXMLElement();
                xml_ao.parseString("\"<AnalogInput desc=\"AnalogOutput\" id=\"4\"/>\"");
                xml.addChild(xml_ao);

                DatabaseApp.getInstance().activeConfiguration().inputFromXML(xml);
                
                DatabaseApp.msg("Speichere Konfiguration", "Installer", 1);
                save_app();
            }
            
    }
}
