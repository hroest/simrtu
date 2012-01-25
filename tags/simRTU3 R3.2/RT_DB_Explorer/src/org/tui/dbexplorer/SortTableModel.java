/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tui.dbexplorer;

import javax.swing.table.DefaultTableModel;
import java.util.HashMap;
/**
 *
 * @author Michael
 */
public class SortTableModel extends DefaultTableModel {

    public HashMap<Integer, Class> ColumnClasses = null;

    @Override
    public Class<?> getColumnClass(int columnIndex) {

        if (this.ColumnClasses != null)
        {
            if (this.ColumnClasses.containsKey(columnIndex))
            {
                return this.ColumnClasses.get(columnIndex);
            }
            else
            {
                return super.getColumnClass(columnIndex);
            }
        }
        else
        {
            return super.getColumnClass(columnIndex);
        }
    }

    public void clear_Class_list()
    {
        this.ColumnClasses.clear();
    }

    public void set_column_Class_list(int index, Class cclass)
    {
        if (this.ColumnClasses == null)
        {
            this.ColumnClasses = new HashMap();
        }

        if (this.ColumnClasses.containsKey(index))
        {
            this.ColumnClasses.remove(index);
        }
        this.ColumnClasses.put(index, cclass);
    }
}
