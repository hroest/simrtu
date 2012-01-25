/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package APP_SET_DEFAULTS;

import java.io.*;
import java.util.Date;

import org.tui.messagelist.MessageList;

/**
 *
 * @author Administrator
 */
public class outputs {

    public static boolean Error = true;
    public static boolean Message = true;
    public static boolean Logging = true;
    
    
    private static PrintStream err_p;

    
    private static PrintStream msg_p;

    
    private static PrintStream log_p;

    static {
        
        MessageList.MSG.Event("simRTU3 gestartet", "-", "-", MessageList.prio_low);
        
        try {
            //err_out = new FileOutputStream("F date [" + date.getDate() + "-" + (date.getMonth() + 1) + "-" + (date.getYear()+1900) + "] time [" + date.getHours() + "-" + date.getMinutes() + "].csv");
            //err_out = new FileOutputStream("F.csv", true);
            //err_p = new PrintStream(err_out);

            //msg_out = new FileOutputStream("M date [" + date.getDate() + "-" + (date.getMonth() + 1) + "-" + (date.getYear()+1900) + "] time [" + date.getHours() + "-" + date.getMinutes() + "].csv");
            //msg_out = new FileOutputStream("M.csv", true);
            //msg_p = new PrintStream(msg_out);

            //log_out = new FileOutputStream("L date [" + date.getDate() + "-" + (date.getMonth() + 1) + "-" + (date.getYear()+1900) + "] time [" + date.getHours() + "-" + date.getMinutes() + "].csv");
            //log_out = new FileOutputStream("L.csv", true);
            //log_p = new PrintStream(log_out);

        } 
        catch (Exception e)
        {
          
        }
    }

    private static void log_write(String description, String module, String instance, String priority)
    {
        long timeStamp = System.currentTimeMillis();
        String time = new Date(timeStamp).toString();
        String message;
        message = time + "," + module + "," + instance + "," + description + "," + priority;
        log_p.println(message);
    }



    private static void msg_write(String description, String module, String instance, String priority)
    {
        long timeStamp = System.currentTimeMillis();
        String time = new Date(timeStamp).toString();
        String message;
        message = time + "," + module + "," + instance + "," + description + "," + priority;
        msg_p.println(message);
    }
    
    private static void err_write(String description, String module, String instance, String priority)
    {
        long timeStamp = System.currentTimeMillis();
        String time = new Date(timeStamp).toString();
        String message;
        message = time + "," + module + "," + instance + "," + description + "," + priority;
        err_p.println(message);
    }

    public static void log_low_msg(String description, String module, String instance)
    {
      
        MessageList.LOG.Event(description, module, instance, MessageList.prio_low);
        //log_write(description, module, instance, MessageList.prio_low);
    }

    public static void log_high_msg(String description, String module, String instance)
    {
        MessageList.LOG.Event(description, module, instance, MessageList.prio_high);
        //log_write(description, module, instance, MessageList.prio_high);
    }

    public static void log_critical_msg(String description, String module, String instance)
    {
        MessageList.LOG.Event(description, module, instance, MessageList.prio_critic);
        //log_write(description, module, instance, MessageList.prio_critic);
    }

    public static void err_low_msg(String description, String module, String instance)
    {
        MessageList.ERR.Event(description, module, instance, MessageList.prio_low);
        //err_write(description, module, instance, MessageList.prio_low);
    }

    public static void err_high_msg(String description, String module, String instance)
    {
        MessageList.ERR.Event(description, module, instance, MessageList.prio_high);
        //err_write(description, module, instance, MessageList.prio_high);
    }

    public static void err_critical_msg(String description, String module, String instance)
    {
        MessageList.ERR.Event(description, module, instance, MessageList.prio_critic);
        //err_write(description, module, instance, MessageList.prio_critic);
    }

    public static void msg_low_msg(String description, String module, String instance)
    {
        MessageList.MSG.Event(description, module, instance, MessageList.prio_low);
        //msg_write(description, module, instance, MessageList.prio_low);
    }

    public static void msg_high_msg(String description, String module, String instance)
    {
        MessageList.MSG.Event(description, module, instance, MessageList.prio_high);
        //msg_write(description, module, instance, MessageList.prio_high);
    }

    public static void msg_critical_msg(String description, String module, String instance)
    {
        MessageList.MSG.Event(description, module, instance, MessageList.prio_critic);
        //msg_write(description, module, instance, MessageList.prio_critic);
    }
}
