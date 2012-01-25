/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tui.messagelist;

import java.awt.Color;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.Component;
import javax.swing.JTable;

/**
 *
 * @author Michael Kratz
 */
public class table_renderer implements TableCellRenderer
{
    public static final DefaultTableCellRenderer DEFAULT_RENDERER = new DefaultTableCellRenderer();

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {
        Component renderer = DEFAULT_RENDERER.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        try
        {

        float[] hsb = new float[3];

        String val = (String)table.getValueAt(row, 4);

        if (val.equals(MessageList.prio_low))
        {
            hsb = Color.RGBtoHSB(60, 180, 0, hsb);
            renderer.setForeground(Color.getHSBColor(hsb[0], hsb[1], hsb[2]));
        }

        if (val.equals(MessageList.prio_high))
        {
            hsb = Color.RGBtoHSB(240, 95, 1, hsb);
            renderer.setForeground(Color.getHSBColor(hsb[0], hsb[1], hsb[2]));
        }

        if (val.equals(MessageList.prio_critic))
        {
           hsb = Color.RGBtoHSB(230, 0, 0, hsb);
            renderer.setForeground(Color.getHSBColor(hsb[0], hsb[1], hsb[2]));
        }

        val = (String)table.getValueAt(row, 3);

        if (val.contains("Empfang") || val.contains("empfang"))
        {
            hsb = Color.RGBtoHSB(6, 70, 95, hsb);
            renderer.setForeground(Color.getHSBColor(hsb[0], hsb[1], hsb[2]));
        }

        hsb = Color.RGBtoHSB(238, 250, 238, hsb);
        renderer.setBackground(Color.getHSBColor(hsb[0], hsb[1], hsb[2]));
        }
        catch(Exception e)
        {
            // what should I do??
        }

        return renderer;
    }
}
