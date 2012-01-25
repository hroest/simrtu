/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tui.iec60870;

//** Standard Imports for writing simRTUv2 Applications **//
import lib.interfaces.application.Configurable;
import lib.xml.kXMLElement;
import org.tui.iec60870.master104.Master104Application;
import org.tui.iec60870.slave104.Slave104Application;
import rt_database.DatabaseApp;
import java.util.Enumeration;

/**
 *
 * @author Micha
 */
public class IEC60870 implements Configurable {

    //**Standard Application Metadata**//
    private static final String XML_Configuration = "IEC60870";
    private static final String XML_Language = "en";

    //** Meta information about the configuration file **//
    private String language = "en";
    private String author = "Michael Kratz";
    private String date = "01.04.2009";
    private String version = "1.0";
    private String fileId = "UNKNOWN";

    @Override
    public void inputFromXML(kXMLElement xml) throws IllegalArgumentException {

                while (DatabaseApp.ACTIVE==null)
                {
                    Thread.yield();
                }
                // Laden der Konfiguration und Meldung
                IEC60870App.msg("Datenbank verfügbar. Warte auf Initialisierungsende.","-",1);
                while (!DatabaseApp.ACTIVE.init_ready)
                {
                    Thread.yield();
                }
                IEC60870App.msg("Datenbank verfügbar. Registriere Modul IEC60870.","-",1);


        try {
            if (xml.getTagName().equals(XML_Configuration)) {
                //** Standard Application Metadata **//
                language = xml.getProperty("language", "en");
                author = xml.getProperty("author", "UNKNOWN");
                date = xml.getProperty("date", "UNKNOWN");
                version = xml.getProperty("version", "UNKNOWN");
                fileId = xml.getProperty("fileId", "UNKNOWN");

                for (Enumeration e = xml.enumerateChildren(); e.hasMoreElements();) {
                    kXMLElement child = (kXMLElement) e.nextElement();
                    
                    //starting Master104Application
                    if (child.getTagName().equals(Master104Application.XML_Configuration))
                    {
                        Master104Application.getInstance().inputFromXML(child);
                    }

                    //starting Slave104Application
                    if (child.getTagName().equals(Slave104Application.XML_Configuration))
                    {
                        Slave104Application.getInstance().inputFromXML(child);
                    }
                }
            }
        } catch (Exception e) {
            // If there is any problem, let's discard the whole thing and warn the
            // upper level with an exception
            e.printStackTrace();
            throw new IllegalArgumentException(e.toString());
        }
    }

    @Override
    public kXMLElement outputToXML() {
        kXMLElement root = new kXMLElement();
        root.setTagName(XML_Configuration);
        root.addProperty(XML_Language, language);
        root.addProperty("author", author);
        root.addProperty("date", date);
        root.addProperty("version", version);
        root.addProperty("fileId", fileId);
        kXMLElement child = Master104Application.getInstance().outputToXML();
        root.addChild(child);
        return root;
    }

    public void restart_app()
    {
         Master104Application.getInstance().restart_app();
         Slave104Application.getInstance().restart_app();
    }

 
}

