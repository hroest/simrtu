/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tui.iec60870.master101;



import org.tui.iec60870.master101.services.MCmdAppSvc101;
import org.tui.iec60870.master101.services.MGIAppSvc101;
import org.tui.iec60870.IEC60870App;

import org.tui.iec60870.master.MApplicationLayer;
import org.tui.iec60870.master.MAppManager;
import org.tui.iec60870.master.MAppService;

import org.tui.iec60870.common.ProtocolConfig;
import org.tui.iec60870.common.AppService;
import org.tui.iec60870.common.PeriodicService;
import org.tui.iec60870.common.MapSingleValue;
import org.tui.iec60870.common.MapAsdu;
import org.tui.iec60870.common.DbLocator;
import org.tui.iec60870.common.Translator;
import org.tui.iec60870.common.LinearTranslator;
import org.tui.iec60870.common.IEC608705Exception;
import org.tui.iec60870.common.UserData;
import org.tui.iec60870.common.Asdu;
import org.tui.iec60870.common.LayerThread;

import org.tui.iec60870.common101.AsduKey101;

import rt_database.DataElement;
import rt_database.Database;
import rt_database.DatabaseApp;
import rt_database.Record;

import lib.xml.kXMLElement;

import java.util.Vector;
import java.util.Enumeration;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author mikra
 */
public class MApplication101 extends MApplicationLayer {

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
    protected ProtocolConfig protocol;
    private kXMLElement node;
    private AtomicBoolean initalized = new AtomicBoolean();
    private MAppManager101 mgr;
    public long runs;
    
    public MApplication101(kXMLElement anode, LayerThread alower) {

        super("Master Application 101", 1);

        this.instance = "Master101 Layer";

        this.initalized.set(false);

        //my lower layer is alower
        this.lower = alower;
        
        //lower Thread - i am the upper one for you
        this.lower.upper = this;

        //save config
        this.node = anode;

        //name
        this.name = node.getProperty("name","unbekannt");


        //**********************************************************************
        // Analyzing Configuration
        //**********************************************************************

        /* example (important are name, gicylce, error_db, caa)
         *
         * error_db link to rt database for counting errors
         * gicycle general interrogation cycle
         * caa/casdu common address
         *
         * take a look into class MProtoolConfig for more tags you can put there
         *
         * <Master104Session port="2404" name="teststation" gicycle="1" error_db="4002" caa="513" ip="127.0.0.1">
         * <MappingList>
         * *
         * </MappingList>
         * </Master104Session>
         */

        protocol = new ProtocolConfig(anode);

        //the MAppManager builds my personal upper layer and sents me data if necessary
        mgr = new MAppManager101(this, protocol.casdu);

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
        try
        {
            kXMLElement child = anode.getChild(0);
            if (child.getTagName().equals("MappingList")) {
            mgr.map = Mapping(child, mgr);
        }

        }
        catch(Exception e)
        {
            IEC60870App.err("Master104 Konfiguration fehlt Tag MappingList...", name, 1);
            //no service available;
            return;
        }
        
        //creates a Service --> take care to tell the app which AppManager listens to him
        mgr.periodic = new Vector();
        if (protocol.gicycle > 0) {
            mgr.periodic.addElement(new PeriodicService(protocol.gicycle * 1000, new MGIAppSvc101(mgr, null, protocol.casdu, (short) 0), mgr));
        }

        //also for intial services
        mgr.initial = new MAppService[1];
        mgr.initial[0] = new MGIAppSvc101(mgr, null, protocol.casdu, (short) 0);

        //put the Appmanager to the Hashtable where all Managers are summarized --> this time only implemented for one Manager
        manager.put(mgr.casdu, mgr);

        //set the ASDU Reader/Writer instances with default values due to 101 specification
        //cotsize cause of transmission size (how many bytes??)
        //casdusize common address size (how many bytes??)
        //ioasize information object address size (how many bytes??)
        this.reader = new MAsduReader101(protocol.cotsize, protocol.casdusize, protocol.ioasize);
        this.writer = new MAsduWriter101(protocol.cotsize, protocol.casdusize, protocol.ioasize);

    }

    private MMapping101 Mapping(kXMLElement node, MAppManager manager) {

        /*
         * Analyzes Mapping things
         *
         * example
         *
         * <Mapping>
         * <Key vsq="1" caa="513" id="13"/>
	 * <Value pie="0" ioa="8970" db="4001" pval="0"/>
	 * </Mapping>
         */

        MMapping101 map = new MMapping101(manager);

        /*
         * generate Mapping for values which come from remote station
         */
        
        for (Enumeration e = node.enumerateChildren(); e.hasMoreElements();) {
            kXMLElement child = (kXMLElement) e.nextElement();
            if (child.getTagName().equals("Mapping_Input")) {
                //add for each Mapping a inputWriter
                map.addWriter(inputWriter(child));
            }
        }

        /*
         * generate Mapping values which should be sent to remote station
         */
         for (Enumeration e = node.enumerateChildren(); e.hasMoreElements();) {
            kXMLElement child = (kXMLElement) e.nextElement();
            if (child.getTagName().equals("Mapping_Output")) {
                MAppService service = addProcedure(child, manager);
                if (service != null)
                map.addProcedure(service);
            }
        }
        return map;
    }

    protected MAppService addProcedure(kXMLElement node, MAppManager manager)
    {
        MCmdAppSvc101 cmd = null;
        AsduKey101 key = null;
        DbLocator db = null;
        int ioa = -1;
        for (Enumeration e = node.enumerateChildren(); e.hasMoreElements();) {
            kXMLElement child = (kXMLElement) e.nextElement();
            String aname = child.getTagName();
            if (aname.equals(KEY)) {
                key = new AsduKey101(
                        (short) child.getProperty(ID, -1),
                        (short) 6,
                        (short) child.getProperty(CAA, -1));
            }

            if (aname.equals("Value")) {

                db = new DbLocator(
                        child.getProperty(DBREF, -1),
                        child.getProperty(LABEL, "unknown"));
                ioa = child.getProperty(IOA, -1);
            }
        }

        if ((manager != null) && (db != null) & (key != null) & ioa != -1) {
            cmd = new MCmdAppSvc101(manager, db, ioa, key, key);
        }
        return cmd;
    }

    /**
     * Instanciate a Writer and its key from XML configuration.
     *
     * @param node	a kXMLElement Writer.
     *
     * @return an object array with Writer and related WriterKey.
     */
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
        
        Translator trans=null;
        String translator = node.getProperty("translation", "linear");
        if (translator.equals("linear")) {
            //y=ax+b
            double a = node.getFloatProperty("a", (float)1);
            double b = node.getFloatProperty("b", (float)0);
            trans = new LinearTranslator(a, b);
        }
        return trans;
    }

    @Override
    protected void upperEvent(Object object) throws IEC608705Exception {
        //create ASDU101
        Object data = writer.build((AppService) object);
        if (data != null) {
            write(data);
        }
    }

    @Override
    protected void lowerEvent(Object object) throws IEC608705Exception {
        short[] raw_asdu = (short[]) object;

        //Anlayse bzgl. GA und Confirmations
        UserData data = null;
        Asdu asdu = null;
        try {
            data = analyze_asdu(raw_asdu);
            asdu = reader.build(data);
        } catch (Exception e) {

            String msg = "";
            if (raw_asdu !=null)
            {
            for (int ii = 0; ii < raw_asdu.length; ii++) {
                msg = msg + "[" + Short.toString(raw_asdu[ii]) + "] ";
            }
            }
            else
            {
                msg="null";
            }
            IEC60870App.err("Fehlerhafte Asdu, verwerfe Paket der Länge " + raw_asdu.length + " ..." + msg, name, 1);
            return;
        }

        if (asdu==null)
        {
            if (raw_asdu!=null)
            {
                String msg="";
                for (int ii=0; ii< raw_asdu.length;ii++)
                {
                    msg=msg + " " + Short.toString(raw_asdu[ii]);
                }
                IEC60870App.err("Asdu nicht auswertbar durch reader...:" + msg,name,1);
            }
        return;
        }

        MAppManager mngr = null;
        if (manager.containsKey(new Integer(protocol.casdu))) {
            switch (asdu.cot) {
                case 1:
                case 2:
                case 3:
                case 5:
                    write(object);
                    break;
                case 20:
                    IEC60870App.log("Station " + name + " meldet " + "Generalabfrage Objekt empfangen. Typ: " + asdu.id, name);
                    write(object);
                    break;

                //diese sind nur bestätigend...
                case 7:
                    IEC60870App.log("Station " + name + " meldet " + " Bestätigung Anfrage Generalabfrage", name);
                    write(object);
                    return;
                case 9:
                    write(object);
                    return;
                case 10:
                    IEC60870App.log("Station " + name + " meldet " +  " Beendigung Generalabfrage", name);
                    write(object);
                    return;
                case 44:
                case 45:
                case 46:
                case 47:
                    write(object);
                    return;
            }
            mngr = (MAppManager) manager.get(new Integer(protocol.casdu));
            mngr.evaluate(asdu);
        } else {
            IEC60870App.err("Station " + name + " meldet " + "Adresse " + data.address + " nicht definiert.", name,1);
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
    public synchronized void init_from_lower() {
        MAppManager mngr = null;
        if (manager.containsKey(new Integer(protocol.casdu))) {
            mngr = (MAppManager) manager.get(new Integer(protocol.casdu));
            mngr.reinit();
        }
        notifyAll();
    }

    @Override
    public void init_from_upper() {
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
                        rec.quality = Record.Q_COUNTER;
                        rec.timeStamp = System.currentTimeMillis();
                        element.writeNewRecord(rec);
                    }
                }
            }
            else
            {
                this.connected.set(true);
            }
            
            for (Enumeration ee = node.getChild(0).enumerateChildren(); ee.hasMoreElements();)
            {
            kXMLElement child1 = (kXMLElement)ee.nextElement();

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
                                element.writeNewRecord(rec);
                            }
                        }
                    }
                }
            }
            }
        } catch (Exception e) {
        }
    }

    public void comm_test() {
        try {


            for (Enumeration ee = node.getChild(0).enumerateChildren(); ee.hasMoreElements();)
            {
            kXMLElement child1 = (kXMLElement)ee.nextElement();
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
                                switch (rec.quality) {
                                    case (Record.Q_VALID):
                                        rec.quality = Record.Q_VALID_COMM;
                                        break;
                                }
                                element.writeNewRecord(rec);
                            }
                        }
                    }
                }
            }
            }
        } catch (Exception e) {
        }
    }

    public void wait_for_connect() {
        this.connected.set(false);
        try {
            for (Enumeration ee = node.getChild(0).enumerateChildren(); ee.hasMoreElements();)
            {
            kXMLElement child1 = (kXMLElement)ee.nextElement();
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
                                rec.quality = Record.Q_WAIT_FOR_CONNECT;
                            }
                        }
                    }
                }
            }
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void run() {
            while (running) {
            try {
                if (runs == Long.MAX_VALUE) {
                    runs = 0;
                } else {
                    runs++;
                }

                {
                    handle_events();
                    Thread.sleep(100);
                }
            } catch (Exception e) {
                IEC60870App.err("Fehler im Master101 Layer (Prozedur MApplication101/run)" + e, name, 1);
            }
        }
    }


}


