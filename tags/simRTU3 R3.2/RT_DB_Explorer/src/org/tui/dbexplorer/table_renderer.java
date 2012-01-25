/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tui.dbexplorer;

import java.awt.Color;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.Component;
import javax.swing.JTable;


/**
 *
 * @author Micha
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

        String val = (String)table.getValueAt(row, 5);

        float[] hsb = new float[3];

        if (val.equals("gut") || val.equals("gut | Kommunikation geprüft"))
        {
            renderer.setForeground(Color.BLUE);
        }

        if (val.equals("Kommunikation ausgefallen") || val.equals("Warte auf Verbindung") || val.equals("Station nicht erreichbar"))
        {
            renderer.setForeground(Color.red);
        }

        if (val.equals("nicht erneuert"))
        {
            hsb = Color.RGBtoHSB(255, 155, 15, hsb);
            renderer.setForeground(Color.getHSBColor(hsb[0], hsb[1], hsb[2]));
        }

        if (val.equals("Initialwert"))
        {
            renderer.setForeground(Color.LIGHT_GRAY);
        }

        if (val.contains("Timeout"))
        {
            renderer.setForeground(Color.DARK_GRAY);
        }

        if (val.contains("Interner Zählwert"))
        {
            hsb = Color.RGBtoHSB(45, 90, 90, hsb);
            renderer.setForeground(Color.getHSBColor(hsb[0], hsb[1], hsb[2]));
        }


        hsb = Color.RGBtoHSB(238, 250, 238, hsb);
        renderer.setBackground(Color.getHSBColor(hsb[0], hsb[1], hsb[2]));
        }
        catch(Exception e)
        {
            // waht should I do??
        }


        return renderer;
    }
}
