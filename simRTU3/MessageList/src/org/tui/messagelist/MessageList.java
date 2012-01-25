/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tui.messagelist;

import javax.swing.JTable;

/**
 *
 * @author mikra
 */
public class MessageList {

    public static final msgcontrl MSG = new msgcontrl();
    public static final msgcontrl ERR = new msgcontrl();
    public static final msgcontrl LOG = new msgcontrl();

    public static final String prio_low     = "niedrig";
    public static final String prio_high    = "hoch";
    public static final String prio_critic  = "kritisch";
    
    public static void set_table_MSG(JTable link2)
    {
        MSG.set_table(link2);
    }

    public static void set_table_ERR(JTable link2)
    {
        ERR.set_table(link2);
    }

    public static void set_table_LOG(JTable link2)
    {
        LOG.set_table(link2);
    }

    public static void clear_table_LOG()
    {
        LOG.clear();
    }

    public static void clear_table_ERR()
    {
        ERR.clear();
    }

    public static void clear_table_MSG()
    {
        MSG.clear();
    }

}
