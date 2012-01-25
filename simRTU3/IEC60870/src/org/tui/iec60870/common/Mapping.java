/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tui.iec60870.common;

import org.tui.iec60870.IEC60870App;

import rt_database.Record;
import rt_database.Database;
import rt_database.DatabaseApp;
import rt_database.DataElement;
import rt_database.DataEventListener;

import java.util.Hashtable;
import java.util.Enumeration;

//@todo hier noch deutliche Anpassungen vornehmen für ASDUS unterschiedlicher Art...

/**
 *
 * @author Micha
 */
public abstract class Mapping implements DataEventListener {

    /**
     * <p>
     * Table mapping keys to asdus. This table will be filled
     * by asdus to write in database eg in case application
     * has decoded an asdu from a remote station.
     * <p>
     * Key:	AsduKey <br>
     * Value:	MapAsdu
     */

    protected Hashtable Map_Key_Writer;

    /**
     * Related application manager. Mapping implements DataEventListener
     * to be notified by luciol database. Then the mapped data
     * is passed to application manager.
     */
    public AppManager manager;

    /**
     * Map Asdu Key instanciation according to implementation of iec608705
     * currently in use.
     *
     * @param object	Can be a MapAsdu or a real Asdu object.
     *
     * @return the adequate writer map key.
     */
    protected abstract AsduKey instanciateKey(Object object);

    /**
     * Gets the 'dbref' data element located in luciol datatbase.
     *
     * @param dbref	a luciol database reference.
     *
     * @return the data element object requested or null.
     */
    protected DataElement getDataElement(int dbref) {
        Database db = DatabaseApp.ACTIVE;
        DataElement elt = null;
        if (db != null) {
            elt = db.getDataElement(dbref);
            if (elt==null)
            {
                 IEC60870App.err("Common/Mapping: Kann Datenelement mit Datenbankreferenz " + dbref + "nicht lesen.", "-", 1);
            }
        }
        return elt;
    }

    /**
     * Records the listener for all services & readers.
     *
     * @param dbref	luciol database reference
     */
    protected void recordEventListener(int db) {
        DataElement element = getDataElement(db);
        if (element != null) {
            element.addDataEventListener(this);
            //an data event was performed due to change of a record
            dataEventPerformed(element);
        }
    }

    /**
     * This call will perform an attempt to a value in luciol database
     * from a read 'decoded' asdu sent by a remote station.<br>
     * The mechanism is the following:<br>
     * - First, instanciation of a key according to standard implementation
     * and master/slave mode running application. Notice that this instanciation
     * is operated from an Asdu object.<br>
     * - Second, by evaluating the presence of the so-created key in the
     * related mapping between key and references, the write is allowed
     * or not.<br>
     * - Third and last, thanks to MapRwAsdu fields, we can retrieve all
     * necessary informations to locate the value to write a new record
     * in the correct database referenced area.
     *
     * @param object	an application service data unit reference
     *
     * @return a flag to indicate writing or not.
     */
    



    public boolean write(Object object) {
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
                    record.setRemoteEvent();

                    dbelement.writeNewRecord(record);

                } catch (Exception e) {
                    return false;
                }
            }
        }
        return result;
    }

    /**
     * <p>
     * Maps a key to a writer asdu.
     * Updates only Map_Key_Asdu.
     * Request listener is not recorded for writer.
     *
     * @param writer	a writer asdu.
     */
    public void addWriter(MapAsdu writer) {
        Map_Key_Writer.put(writer.key, writer);
    }

    /**
     * Get accessor on a single writer asdu from its key.
     *
     * @param key	A writer mapping key.
     *
     * @return the mapped writer asdu or null.
     */
    public MapAsdu getWriter(AsduKey key) {
        return (MapAsdu) Map_Key_Writer.get(key);
    }

    /**
     * Get accessor on all recorded writer asdus from this mapping.
     *
     * @return an enumeration writer asdus.
     */
    public Enumeration enumerateWriters() {
        return Map_Key_Writer.elements();
    }

    /**
     * Allocates a mapping object.
     *
     * @param manager	Application layer manager.
     */
    protected Mapping(AppManager manager) {
        Map_Key_Writer = new Hashtable();
        this.manager = manager;
    }
}
