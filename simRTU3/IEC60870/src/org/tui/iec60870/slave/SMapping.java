/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tui.iec60870.slave;

import org.tui.iec60870.common.*;
import org.tui.iec60870.common101.Interoperability101;
import rt_database.DataElement;
import rt_database.Record;
import java.util.Vector;
import java.util.Enumeration;
import java.util.Hashtable;
import org.tui.iec60870.IEC60870App;

/**
 * <p>
 * This class defines a mapping for slave application.
 * <p>
 * This inheriting class specializes Mapping object adding:
 * <p>
 * - measure asdus to poll periodically.
 * <p>
 * - general interrogation asdus to return on related master
 * request.
 * <p>
 * - event asdus to enable triggering of spontaneous events from
 * database notification.
 *
 * @author lionnel cauvy
 * @version 1.0
 * @date 01.II.03
 */
public abstract class SMapping extends Mapping {

    /**
     * Measure vector.
     */
    private Vector measure;
    /**
     * General interrogation vector.
     */
    private Vector gi;
    /**
     * Table to map database reference to spontaneous events.
     */
    private Hashtable Map_DbRef_Event;

    /**
     * This event is raised on database notification.
     *
     * @param source	The element from database that has changed.
     */
    @Override
    public void dataEventPerformed(DataElement source) {
        // reject if invalid.
        if (!source.lastRecord().isValid()) {
            return;
        }
        // spontaneous event application service
        AppService svc = (AppService) Map_DbRef_Event.get(new Integer(source.databaseID));
        resolveMapping(svc);
        ((SAppManager) manager).event(svc);
    }

    /**
     * Allows to get data in luciol central database for a special
     * application procedure from its defined mapping.
     *
     * @param proc	The procedure to complete with data elements from luciol database.
     */
    public int resolveMapping(AppService proc) {
        //set link to proc.asdu.value
        int elm = 0;
        MapSingleValue[] value = proc.asdu.value;
        for (int ii = 0; ii < proc.asdu.value.length; ii++) {
            value[ii].mapped = read(value[ii].db.index);
            if (value[ii].translator != null) {
                value[ii].mapped = value[ii].translator.inverseapply(value[ii].mapped);
            }
            elm++;
        }
        return elm;
    }


    /**
     * Read data value at a database index and return it as an Object.
     *
     * @param index	The database area to read
     *
     * @return read value as a java.lang.Object
     */
    public Object read(int index) {
        DataElement element = getDataElement(index);
        if (element == null) {
            return null;
        }
        return element.lastRecord();
    }

    /**
     * Add an application procedure, and by the way, an asdu, to
     * answer on master general interrogation request.
     *
     * @param proc	The procedure to store.
     */
    public void addGi(AppService asdu) {
        gi.addElement(asdu);
    }

    /**
     * Add an application procedure, and by the way, an asdu, to
     * return on master data class 2 polling procedure request.
     *
     * @param proc	The procedure to record.
     */
    public void addMeasure(AppService asdu) {
        measure.addElement(asdu);
    }

    /**
     * Get accessor on number of different asdus that are 'measurand' ones.
     *
     * @return number of different measures performed by this application.
     */
    public int measureSize() {
        return measure.size();
    }

    /**
     * Add a 'spontaneous event' asdu to map. Same db index reference
     * can trigger several different events. So events are
     *
     * @param event	The event to map to database.
     */
    public void addEvent(AppService event) {
        if (event.asdu.value.length > 0) {
            for (int ii = 0; ii < event.asdu.value.length; ii++) {
                //register all value mappings (value!! is type of MapSingleValue)
                int db_id = event.asdu.value[ii].db.index;
                event.cot = 3;
                recordEventListener(db_id);
                try {
                    Map_DbRef_Event.put(db_id, sub_event(event, ii));
                } catch (Exception e) {
                }

            }
        }

    }

    /**
     * Get accessor on all general interrogation asdus recorded
     * in this mapping.
     *
     * @return an enumeration of asdus.
     */
    public Enumeration enumerateGis() {
        return gi.elements();
    }

    /**
     * Get accessor on all measure asdus recorded
     * in this mapping.
     *
     * @return an enumeration of asdus.
     */
    public Enumeration enumerateMeasures() {
        return measure.elements();
    }

    /**
     * Allocates a mapping object for slave application.
     *
     * @param manager	Application layer manager.
     */
    protected SMapping(SAppManager manager) {
        super(manager);
        Map_DbRef_Event = new Hashtable();
        gi = new Vector();
        measure = new Vector();
    }

    public AppService sub_event(AppService event, int index) {
        AppService svc = new AppService(event);
        svc.asdu.value = new MapSingleValue[1];
        svc.asdu.value[0] = event.asdu.value[index];
        return svc;
    }

    public boolean write(Object object, int Interop) {
        boolean result = false;
        Asdu asdu = (Asdu) object;
        AsduKey key = instanciateKey(object);
        MapAsdu writer = null;
        DbLocator db = null;
        int ioa = -1;
        Translator trans = null;
        Object toWrite = null;
        DataElement dbelement = null;
        Record record = null;

        if (Map_Key_Writer.size() == 0) {
            return false;
        }

        //@todo - write a better mapping procedure to support more than one io's

        writer = (MapAsdu) Map_Key_Writer.get(key);

        if (writer != null) {
            if (!asdu.sq && asdu.nio == 1) {
                //only one value
                try {
                    InformationObject elt = null;
                    for (int i = 0; i < writer.value.length; i++) {
                        ioa = writer.value[i].ioa;
                        db = writer.value[i].db;
                        trans = writer.value[i].translator;
                        elt = (InformationObject) asdu.io.get(0);

                        //find correct element
                        if (ioa != elt.address) {
                            elt = null;
                        } else {
                            break;
                        }

                    }

                    if (elt == null) {
                        return false;
                    }

                    toWrite = asdu.value(ioa, 0); //0 for the element itself //depends whicht format the element structure has, time or not and so on

                    if (trans != null) {
                        toWrite = trans.apply(toWrite);
                    }

                    dbelement = getDataElement(db.index);
                    result = true;

                    record = dbelement.instanciateRecord();

                    record.setValue(toWrite);
                    record.initial = false;
                    switch(Interop)
                    {
                        case(Interoperability101.COT_ACTIVATION):
                        case(Interoperability101.COT_ACTIVATIONCONF):
                            record.setRemoteEvent();
                        break;

                        case(Interoperability101.COT_ACTIVATION_TERMINATION):
                            record.setTerm();
                        break;
                    }
                    dbelement.writeNewRecord(record);

                } catch (Exception e) {
                    return false;
                }
            }
        }
        else
        {
            IEC60870App.err("Mapping nicht gefunden.", "Typinformation: " + key.id + " CAA: " + key.caa, 3);
            return false;
        }
        return result;
    }

}
