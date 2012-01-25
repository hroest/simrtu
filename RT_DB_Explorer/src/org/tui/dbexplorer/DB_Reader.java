/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tui.dbexplorer;

import rt_database.DataEventListener;
import rt_database.DataElement;
import rt_database.Database;
import rt_database.DatabaseApp;
import rt_database.Record;

import java.util.Date;
import java.util.Enumeration;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.table.TableRowSorter;
import javax.swing.table.TableModel;
import javax.swing.SwingWorker;

import APP_SET_DEFAULTS.outputs;

/**
 *  builds the DataEventListener -> a single Thread running in the background,
 *  is linked as a Listener to each Element in the Database and called, if a
 *  value in these elements has changed -> the update procedures for the explorer
 *  application is used here
 *
 * @author Michael Kratz
 **/
public class DB_Reader implements DataEventListener {

    private final static Database DB = DatabaseApp.ACTIVE;
    private final static DB_Reader INST = new DB_Reader();
    private static JTable table;
    boolean STOP = false;
    private static boolean running = false;
    private static int treshold = 15*60*1000;

    public static DB_Reader getInstance() {
        return INST;
    }

    public void set_table(JTable atable) {

        SortTableModel model = new SortTableModel();

        model.addColumn("Datenbank ID");
        model.addColumn("Beschreibung");
        model.addColumn("Typ");
        model.addColumn("Wert");
        model.addColumn("letztes Update");
        model.addColumn("Qualität");
        model.addColumn("Status");
        model.set_column_Class_list(0, Integer.class);
        table = atable;

        /**set table layout**/
        table.setModel(model);

        table_renderer renderer = new table_renderer();

        table.setDefaultRenderer(Object.class, renderer);
        table.setDefaultRenderer(Integer.class, renderer);
        reg_DB_listeners();

        /**set table row sorter**/
        RowSorter<TableModel> sorter =
        new TableRowSorter<TableModel>(model);
        table.setRowSorter(sorter);
        

    }

   public static void log(String description, String instance)
    {
        if (outputs.Logging)
        {
            outputs.log_low_msg(description, "Echtzeitdatenbankexplorer", instance);
        }
    }

    public static void err(String description, String instance, int priority)
    {
       switch (priority)
       {
           case 1:
           {
              outputs.err_low_msg(description, "Echtzeitdatenbankexplorer", instance);
           }
           break;
           case 2:
           {
               outputs.err_high_msg(description, "Echtzeitdatenbankexplorer", instance);
           }
           break;
           case 3:
           {
               outputs.err_critical_msg(description, "Echtzeitdatenbankexplorer", instance);
           }
           break;
       }
    }

    public static void msg(String description, String instance, int priority)
    {
        switch (priority)
        {
           case 1:
           {
              outputs.msg_low_msg(description, "Echtzeitdatenbankexplorer", instance);
           }
           break;
           case 2:
           {
               outputs.msg_high_msg(description, "Echtzeitdatenbankexplorer", instance);
           }
           break;
           case 3:
           {
               outputs.msg_critical_msg(description, "Echtzeitdatenbankexplorer", instance);
           }
           break;
       }
    }



    public void external_update()
    {
        dataEventPerformed(null);
    }

    @Override
    public synchronized void dataEventPerformed(DataElement source) {
        // if application has not started, don't send anything !!!
        if ((table == null) || (DB_Reader.running)) {
            return;
        }
      
        // it doesn't matter which elements changed -> every element is read
        // and an update table sent to Explorer

        DB_Reader.running = true;

        try {
            SwingWorker<SortTableModel, Void> worker =
                    new SwingWorker<SortTableModel, Void>() {

                        @Override
                        public SortTableModel doInBackground() {
                            return update();
                        }

                        @Override
                        public void done() {
                            try {
                                table.setModel(get());
                                //TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>();
                                //table.setRowSorter(sorter);
                                get().fireTableDataChanged();
                            } catch (Exception e) {
                                err("Fehler" + e, "", 3);
                            }
                            DB_Reader.running = false;
                        }
                    };
            worker.execute();
            notify();
        }
        catch(Exception e)
        {
            DB_Reader.running = false;
        }
    }

    public void reg_DB_listeners() {

        if (table != null) {
            Enumeration enumElements = DB.elements();
            DataElement element;
            while (enumElements.hasMoreElements()) {
                element = (DataElement) enumElements.nextElement();
                try {
                    element.removeDataEventListener(this);
                } catch (Exception e) {
                }
                element.addDataEventListener(this);
            }
        }
    }
    
    public void unreg_DB_listeners() {

        if (table != null) {
            Enumeration enumElements = DB.elements();
            DataElement element;
            while (enumElements.hasMoreElements()) {
                element = (DataElement) enumElements.nextElement();
                try {
                    element.removeDataEventListener(this);
                } catch (Exception e) {
                }
            }
        }
    }



    public void stop_DB_Reader() {
        this.STOP = true;
    }

    public SortTableModel update() {

        DataElement element;
        Enumeration enumElements = DatabaseApp.ACTIVE.elements();
        SortTableModel model = new SortTableModel();
        model.addColumn("Datenbank ID");
        model.addColumn("Beschreibung");
        model.addColumn("Typ");
        model.addColumn("Wert");
        model.addColumn("letztes Update");
        model.addColumn("Qualität");
        model.addColumn("Status");
        model.set_column_Class_list(0, Integer.class);

        //add Columns
        while (enumElements.hasMoreElements()) {
            element = (DataElement) enumElements.nextElement();

            Record rec = element.lastRecord();

            int id = element.databaseID;
            String typ  = null;
            String val  = null;
            String upd  = null;
            String dec  = null;
            String qual = null;
            String flag = null;


            if (rec.getClass().equals(rt_database.DigitalRecord.class)) {
                typ = "Digital";
            }

            if (rec.getClass().equals(rt_database.AnalogRecord.class)) {
                typ = "Analog";

            }

            if (rec.getClass().equals(rt_database.IntegerRecord.class)) {
                typ = "Integer";
            }

            if (rec.getClass().equals(rt_database.FloatRecord.class)) {
                typ = "Float";
            }

            val = rec.stringValue();
            boolean timeout = false;

            dec = element.description;

            if (treshold < (System.currentTimeMillis() - rec.getTimeStamp()))
            {
                timeout = true;
            }

            upd = new Date(rec.getTimeStamp()).toString();

            switch (rec.quality) {
                case Record.Q_NOT_REACHABLE:
                    qual = "Station nicht erreichbar";
                case Record.Q_TIMEOUT:
                    qual = "Timeout";
                    break;
                case Record.Q_VALID:
                    qual = "gut";
                    break;
                case Record.Q_VALID_COMM:
                    qual = "gut | Kommunikation geprüft";
                    break;
                case Record.Q_WAIT_FOR_CONNECT:
                    qual = "Warte auf Verbindung";
                    break;
                case Record.Q_NOT_REFRESHED:
                    qual = "nicht erneuert";
                    break;
                case Record.Q_COMM_FAIL:
                    qual = "Kommunikation ausgefallen";
                    break;
                case Record.Q_INITIAL:
                    qual = "Initialwert";
                    break;
                case Record.Q_COUNTER:
                    qual = "Interner Zählwert";
                    break;
                default:
                    qual = "unbekannt";
                    break;
            }

            if (rec.remote_event || rec.termination)
            {
                flag = "Fernwirkbefehl";
            }
            else if( rec.spontaneous)
            {
                flag = "Spontan";
            }

            model.addRow(new Object[]{id, dec, typ, val, upd, qual, flag});
        }
        return model;
    }

    @Override
    public String toString()
    {
        return "Database Explorer";
    }

}
