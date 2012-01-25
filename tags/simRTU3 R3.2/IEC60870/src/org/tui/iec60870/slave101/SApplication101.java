/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tui.iec60870.slave101;

import org.tui.iec60870.IEC60870App;
import lib.interfaces.queue.QueueException;

//common imports
import org.tui.iec60870.common.ProtocolConfig;
import org.tui.iec60870.common.IEC608705Exception;
import org.tui.iec60870.common.UserData;
import org.tui.iec60870.common.Asdu;
import org.tui.iec60870.common.LayerThread;
import org.tui.iec60870.common.MapSingleValue;
import org.tui.iec60870.common.MapAsdu;
import org.tui.iec60870.common.DbLocator;
import org.tui.iec60870.common.Translator;
import org.tui.iec60870.common.LinearTranslator;
import org.tui.iec60870.common.AppService;


//common101 imports
import org.tui.iec60870.common101.AsduKey101;

//slave imports
import org.tui.iec60870.slave.SApplicationLayer;
import org.tui.iec60870.slave.SDataClass1AppSvc;

// Database Imports
import rt_database.DataElement;
import rt_database.Database;
import rt_database.DatabaseApp;
import rt_database.Record;


import lib.xml.kXMLElement;
import java.util.Enumeration;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.Vector;

/**
 *
 * @author Micha
 */
public class SApplication101 extends SApplicationLayer {

    protected static final String IOA = "ioa";
    protected static final String GENERALINTERRO = "GeneralInterrogation";
    protected static final String ASDU = "Asdu";
    protected static final String KEY = "Key";
    protected static final String ID = "id";
    protected static final String VSQ = "vsq";
    protected static final String COT = "cot";
    protected static final String CAA = "caa";
    protected final static String MAPVALUE = "MapValue";
    protected final static String DBREF = "db";
    protected final static String LABEL = "label";
    private ProtocolConfig protocol;
    private kXMLElement node;
    private AtomicBoolean initalized = new AtomicBoolean();
    private SAppManager101 mgr;

    public SApplication101(kXMLElement anode, LayerThread alower) {

        super("Slave Application 101", 1);

        this.initalized.set(false);

        //my lower layer is alower
        this.lower = alower;

        //lower Thread - i am the upper one for you
        this.lower.upper = this;

        //save config
        this.node = anode;

        //name
        this.name = node.getProperty("name", "unbekannt");

        protocol = new ProtocolConfig(anode);

        //the SAppManager builds my personal upper layer and sents me data if necessary
        mgr = new SAppManager101(this, protocol.casdu, protocol.polling);
        mgr.name = name;

        //**********************************************************************
        // Analyzing Configuration
        //**********************************************************************

        /*
         * example config is
         *
        <MappingList>
        <Mapping>
        <Key vsq="1" caa="513" id="13"/>
        <Value pie="0" ioa="8970" db="4001" pval="0"/>
        </Mapping>
        <Mapping>
        <Key vsq="1" caa="513" id="45"/>
        <Value pie="0" ioa="8970" db="5000" pval="0"/>
        </Mapping>
        </MappingList>

         */
        //1st of all we need a mapping list
        try {
            kXMLElement child = anode.getChild(0);
            if (child.getTagName().equals("MappingList")) {
                mgr.map = Mapping(child, mgr);
            }

        } catch (Exception e) {
            IEC60870App.err("Slave104 Konfiguration fehlt Tag MappingList...", "-", 1);
            //no service available;
            return;
        }

        manager.put(mgr.casdu, mgr);

        //set the ASDU Reader/Writer instances with default values due to 101 specification
        //cotsize cause of transmission size (how many bytes??)
        //casdusize common address size (how many bytes??)
        //ioasize information object address size (how many bytes??)

        this.reader = new SAsduReader101(protocol.cotsize, protocol.casdusize, protocol.ioasize);
        this.writer = new SAsduWriter101(protocol.cotsize, protocol.casdusize, protocol.ioasize);

    }

    protected SMapping101 Mapping(kXMLElement node, SAppManager101 manager) {

        SMapping101 map = new SMapping101(manager);

        for (Enumeration e = node.enumerateChildren(); e.hasMoreElements();) {
            kXMLElement child = (kXMLElement) e.nextElement();
            if (child.getTagName().equals("Mapping_Output")) {
               map=MapOutput(child, map);
            }
        }

        for (Enumeration e = node.enumerateChildren(); e.hasMoreElements();) {
            kXMLElement child = (kXMLElement) e.nextElement();
            if (child.getTagName().equals("Mapping_Input")) {
                map=MapInput(child, map);
            }
        }
        return map;
    }

    private SMapping101 MapOutput(kXMLElement node, SMapping101 map)
    {
        SMapAsdu101 asdu = inputAsdu(node);
        // general interrogation asdus.
        if (asdu.class1) {
            map.addEvent(new SDataClass1AppSvc(this.lower, asdu, protocol.casdu));
        }
      
        if (asdu.gi) // check no time tag asdus
        {
            map.addGi(new SDataClass1AppSvc(this.lower, asdu, protocol.casdu));
        }
        
        return map;
    }

    private SMapping101 MapInput(kXMLElement node, SMapping101 map)
    {
        map.addWriter(inputWriter(node));
        return map;
    }

    protected MapAsdu inputWriter(kXMLElement node) {
        AsduKey101 key = null;
        MapSingleValue[] value = new MapSingleValue[node.countChildren() - 1];
        int i = 0;
        for (Enumeration e = node.enumerateChildren(); e.hasMoreElements();) {
            kXMLElement child = (kXMLElement) e.nextElement();
            String aname = child.getTagName();
            if (aname.equals(KEY)) {
                key = new AsduKey101(
                        (short) child.getProperty(ID, -1),
                        AsduKey101.NOTUSED,
                        (short) child.getProperty(CAA, -1));
            }
            if (aname.equals("Value")) {
                value[i++] = inputMapValue(child);
            }
        }
        return new MapAsdu(key, value);
    }

    private SMapAsdu101 inputAsdu(kXMLElement node) {

                AsduKey101 key = null;
		MapSingleValue[] value = new MapSingleValue[node.countChildren()-1];
		int i = 0;

                boolean class1=false;
                boolean class2=false;
                boolean gi=false;
                boolean ci=false;

		for(Enumeration e = node.enumerateChildren();e.hasMoreElements();)
		{
			kXMLElement child = (kXMLElement)e.nextElement();
			String aname = child.getTagName();
			if (aname.equals(KEY))
			{
				key = new AsduKey101(
					(short)child.getProperty(ID,-1),
					AsduKey101.NOTUSED,
					child.getProperty(CAA,-1)
				);
                            class1 = child.getProperty("class1", "true", "false", false);
                            class2 = child.getProperty("class2", "true", "false", false);
                            gi = child.getProperty("gi", "true", "false", false);
                            ci = child.getProperty("ci", "true", "false", false);
                        }
			else if (aname.equals("Value"))
                        {
				value[i++] = inputMapValue(child);
                        }
		}
                return new SMapAsdu101(
			key,
			value,
                        class1,
                        class2,
                        gi,
                        ci
		);
    }

    protected MapSingleValue inputMapValue(kXMLElement node) {

        int ioa = node.getProperty(IOA, -1);

        DbLocator db = new DbLocator(
                node.getProperty(DBREF, -1),
                node.getProperty(LABEL, "unknown"));

        Translator translator = inputTranslator(node);

        return new MapSingleValue(
                db,
                ioa,
                translator);
    }

    protected Translator inputTranslator(kXMLElement node) {

        Translator trans = null;
        String translator = node.getProperty("translation", "linear");
        if (translator.equals("linear")) {
            //y=ax+b
            double a = node.getFloatProperty("a", (float) 1);
            double b = node.getFloatProperty("b", (float) 0);
            trans = new LinearTranslator(a, b);
        }
        return trans;
    }

    @Override
    protected void upperEvent(Object object) throws IEC608705Exception {
        //create ASDU101 object sets
        Object data = null;
        Vector tmp1 = null;
        short[] tmp2 = null;

        if (object != null) {
            if (object instanceof AppService)
            {
                data = writer.build((AppService) object);
            }
            else
            {
                data = object;
            }
        }
        if (data != null) {
            int count = 0;
            if (data instanceof Vector) {
                tmp1 = (Vector) data;
                for (int ii = 0; ii < tmp1.size(); ii++) {
                    tmp2 = (short[]) tmp1.get(ii);
                    this.write(tmp2);
                    count++;
                }
            } else {
                write(data);
            }
        }
    }

    @Override
    protected void lowerEvent(Object object) throws IEC608705Exception {
        {
            short[] raw_asdu = (short[]) object;

            //Anlayse bzgl. GA und Confirmations
            UserData data = null;
            Asdu asdu = null;
            try {
                data = analyze_asdu(raw_asdu);
                asdu = reader.build(data);
            } catch (Exception e) {

                String msg = "";
                if (raw_asdu != null) {
                    for (int ii = 0; ii < raw_asdu.length; ii++) {
                        msg = msg + "[" + Short.toString(raw_asdu[ii]) + "] ";
                    }
                } else {
                    msg = "null";
                }
                IEC60870App.err("Fehlerhafte Asdu, verwerfe Paket der Länge " + raw_asdu.length + " ..." + msg, name,1);
                return;
            }

            // Wenn ASDU==null, dann ist sie nicht ausgwertet worden -> Fehler.
            if (asdu == null) {
                if (raw_asdu != null) {
                    String msg = "";
                    for (int ii = 0; ii < raw_asdu.length; ii++) {
                        msg = msg + " " + Short.toString(raw_asdu[ii]);
                    }
                    IEC60870App.err("Asdu nicht auswertbar durch Asdureader: " + msg, name, 1);
                }
                return;
            }

            SAppManager101 mngr = null;
            //should we be interested to analyse the data??

            // if asdu == 65535, then global procedure call

            if (manager.containsKey(new Integer(asdu.caa)) || asdu.caa==65535) {
                switch (asdu.cot) {
                    /*
                     * Übertragungsursachen, die eine Rückantwort verlangen
                     *
                     * 6 - Aktivierung, muss mit 7 beantwortet werden
                     * 8 - Abbruch der Aktivierung, muss mit 9 beantwortet werden
                     */
                    case 6:
                    case 8:
                        //Manager schreibt Rückantwort
                        if (asdu.caa != 65535) {
                            mngr = (SAppManager101) manager.get(new Integer(protocol.casdu));
                            mngr.evaluate(asdu);
                        } else {
                            for (Enumeration e = manager.elements(); e.hasMoreElements();) {
                                mngr = (SAppManager101) e.nextElement();
                                mngr.evaluate(asdu, 1);
                            }
                        }
                        break;
                    /*
                     * Übertragungsursachen, die lediglich bestätigt werden müssen
                     * 
                     * 3 - spontan
                     * 5 - abgefragt
                     * 
                     */
                    case 3:
                    case 5:
                    case 7:
                    case 9:
                    //case 10:
                    case 44:
                    case 45:
                    case 46:
                    case 47:
                        //ohne Modifikation zurückschreiben
                        write(object);
                        break;
                }
               
            } else {
                //Modifikation des Objektes cot=46 für unbekannte Stationsadresse
                modify_cot(raw_asdu, 46);
                write(raw_asdu);
            }
        }
    }

    private void modify_cot(short[] asdu, int cot)
    {
        byte szCot = protocol.cotsize;
        
        if (szCot==1)
        {
            asdu[2] = (short) ((cot & 0x00FF));
        }
        else
        {
            asdu[2] = (short) ((cot & 0x00FF));
            asdu[3] = (short) ((cot & 0xFF00) >> 8);
        }
    }

    private UserData analyze_asdu(short[] data) {

        UserData job = new UserData();
        int index_start = 1 + protocol.cotsize;

        job.address = AddressField((short) data[index_start + 1], (short) data[index_start + 2]);

        index_start = 0;

        for (int ii = 0; ii < data.length; ii++) {
            job.data.add((short) data[ii]);
        }

        return job;
    }

    private int AddressField(short octet1, short octet2) {
        return (octet2 << 8) | octet1;
    }

    @Override
    public void handle_events() throws IEC608705Exception{
        int count = 0;
        Object obj;
        while (!fromLower.isEmpty() && count < 16) {
            try
            {
                obj = fromLower.dequeue();
            }
            catch(QueueException e)
            {
                throw new IEC608705Exception(e.toString());
            }
            lowerEvent(obj);
            count++;
        }
        count = 0;
        while (!fromUpper.isEmpty() && (count < 16)) {
            try
            {
                obj = fromUpper.dequeue();
            }
            catch(QueueException e)
            {
                throw new IEC608705Exception(e.toString());
            }
            upperEvent(obj);
            count++;
        }
    }

    @Override
    public void run() {
            while (running)
            {
            try {
                {
                    if (!this.comm_error)
                    {
                        handle_events();
                    }
                    Thread.sleep(100);
                }
            } catch (Exception e) {
                IEC60870App.err("Fehler im Slave101 Layer (Prozedur SApplication101/run)" + e, name, 1);
            }
        }
    }

    @Override
    public void comm_error(boolean val) {
        try {
            if (val) {
                this.empty_Queues();
                this.connected.set(false);
                if (!this.initalized.get()) {
                    this.initalized.set(true);
                } else {
                    Database base = DatabaseApp.ACTIVE;
                    int error_db = node.getProperty("error_db", -1);
                    if (error_db != -1) {
                        DataElement element = base.getDataElement(error_db);
                        Record rec = element.lastRecord();
                        rec.setValue(rec.floatValue() + 1);
                        rec.quality=0;
                        rec.timeStamp = System.nanoTime();
                        element.writeNewRecord(rec);
                    }
                }
            }
            else
            {
                this.connected.set(true);
            }
            kXMLElement child1 = node.getChild(0).getChild(0);

            if (child1.getTagName().equals("Mapping_Input")) {

                for (Enumeration e = child1.enumerateChildren(); e.hasMoreElements();) {
                    kXMLElement child2 = (kXMLElement) e.nextElement();
                    String aname = child2.getTagName();
                    if (aname.equals("Value")) {
                        int db = child2.getProperty("db", -1);
                        if (db != -1) {
                            Database base = DatabaseApp.ACTIVE;
                            DataElement element = base.getDataElement(db);
                            if (element != null) {
                                Record rec = element.lastRecord();
                                if (val) {
                                    rec.quality = Record.Q_COMM_FAIL;
                                } else {
                                    rec.quality = Record.Q_NOT_REFRESHED;
                                }
                                
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
        }
    }
}
