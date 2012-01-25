/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tui.messagelist;

import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.table.DefaultTableModel;
import java.util.Date;

/**
 *
 * @author mikra
 */

public class msgcontrl {

    private int max_elements=1100;

    private JTable table;
    private DefaultTableModel model;

    public msgcontrl()
    {
        model = new DefaultTableModel();
        model.addColumn("Uhrzeit");
        model.addColumn("Modul");
        model.addColumn("Instanz");
        model.addColumn("Beschreibung");
        model.addColumn("Priorität");
    }

    public void set_table(JTable link2)
    {
        this.table = link2;

    
        /**set table layout**/
        table.setModel(model);

        table_renderer renderer = new table_renderer();
        table.setDefaultRenderer(Object.class, renderer);

        TableColumn column = null;

        for (int ii =0; ii < table.getColumnCount(); ii++)
        {

            column = table.getColumnModel().getColumn(ii);
            if (ii==0)
            {
                int fix_width = 170;
                column.setMaxWidth(fix_width);
                column.setMinWidth(fix_width);
            }

            if (ii==1)
            {
                int fix_width = 100;
                column.setMaxWidth(fix_width);
                column.setMinWidth(fix_width);
            }

            if (ii==2)
            {
                int fix_width = 140;
                column.setMaxWidth(fix_width);
                column.setMinWidth(fix_width);
            }


            if (ii==4)
            {
                int fix_width = 80;
                column.setMaxWidth(fix_width);
                column.setMinWidth(fix_width);
            }

        }
    }
    public synchronized void Event(String decription, String modul, String instance, String priority)

    {
        boolean eventlog = false;
        while (eventlog == false) {
            try {

                long timeStamp = System.currentTimeMillis();
                String time = new Date(timeStamp).toString();

                while (model.getRowCount() > this.max_elements-100) {
                    model.removeRow(0);
                }

                model.addRow(new Object[]{time, modul, instance, decription, priority});
                eventlog = true;
            } catch (Exception e) {
                eventlog = false;
            }
    }
    }

    public synchronized void clear()
    {
        try
        {
            while (model.getRowCount() > 0) {
                    model.removeRow(0);
                }
        }
        catch(Exception e)
        {
          
        }
    }
}
