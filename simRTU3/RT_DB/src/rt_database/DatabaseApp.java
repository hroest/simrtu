/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rt_database;

import lib.util.FifoQueue;
import lib.interfaces.application.Application;
import lib.interfaces.application.AppSession;
import lib.interfaces.application.Configurable;

import APP_SET_DEFAULTS.outputs;

//-----------------------------------------------------------------------------
// Class
//-----------------------------------------------------------------------------
/**
 * Entry point of the database application.
 *
 * @author <a href="mailto:alexis.bietti@sysaware.com">Alexis BIETTI</a>
 */
public class DatabaseApp implements Application, AppSession, Runnable,
        DatabaseFeatures {
    //---------------------------------------------------------------------------
    // Class Attributes
    //---------------------------------------------------------------------------

    public static void log(String description, String instance)
    {
        if (outputs.Logging)
        {
        outputs.log_low_msg(description, "Echtzeitdatenbank", instance);
        }
    }

    public static void err(String description, String instance, int priority)
    {
       switch (priority)
       {
           case 1:
           {
              outputs.err_low_msg(description, "Echtzeitdatenbank", instance);
              break;
           }
           case 2:
           {
               outputs.err_high_msg(description, "Echtzeitdatenbank", instance);
               break;
           }
           case 3:
           {
               outputs.err_critical_msg(description, "Echtzeitdatenbank", instance);
               break;
           }
       }
    }

    public static void msg(String description, String instance, int priority)
    {
        switch (priority)
        {
           case 1:
           {
              outputs.msg_low_msg(description, "Echtzeitdatenbank", instance);
              break;
           }
           case 2:
           {
               outputs.msg_high_msg(description, "Echtzeitdatenbank", instance);
               break;
           }
           case 3:
           {
               outputs.msg_critical_msg(description, "Echtzeitdatenbank", instance);
               break;
           }
       }
    }


    //public static final boolean DEBUG = true;
    /** Active database. */
    public static final Database ACTIVE = new Database();
    /** Passive database. */

    private static final DatabaseApp INST = new DatabaseApp();
    /** Queue of pending notifications. */
    protected static FifoQueue toNotify = new FifoQueue(100);
    
    public static boolean ready;

    //---------------------------------------------------------------------------
    // Constructor
    //---------------------------------------------------------------------------
    /**
     * Don't let anyone instanciate this class.
     */
    private DatabaseApp() {
    }

    /**
     * Print a debug message on System.out
     *
     * @param o
     */

    /**
     * DOCUMENT ME!
     */
    public void start() {
        Thread t = new Thread(this);
        t.start();
    }

    //---------------------------------------------------------------------------
    // Static methods
    //---------------------------------------------------------------------------
    /**
     * Return an instance of the database application.
     */
    public static DatabaseApp getInstance() {
        return INST;
    }

    //---------------------------------------------------------------------------
    // Interface Runnable
    //---------------------------------------------------------------------------
    /**
     * This thread monitors the {@link #toNotify} queue. New {@link
     * DataElement}s are appended to the queue when they are updated.
     */
    @Override
    public void run() {
        DatabaseApp.msg("Starte RT Database Service", "-",1);
        DataElement pending = null;
        while (true) {
            try {
                pending = (DataElement) toNotify.blockingDequeue();
                pending.notifyAllListeners();
            } catch (Exception e) {
                DatabaseApp.err("Fehler aufgetreten " + e, "DatabaseApp", 3);
            }
        }
    }

    /**
     * <p>
     * One session only.
     * </p>
     */
    @Override
    public int numberOfSessionsAllowed() {
        return 1;
    }

    /**
     * <p>
     * This method does nothing for us because the Database application is
     * lauched at starting and never stopped.
     * </p>
     */
    @Override
    public int openNewSession() {
        return -1;
    }

    /**
     * <p>
     * One session only.
     * </p>
     */
    @Override
    public int numberOfCurrentSessions() {
        return 1;
    }

    /**
     * <p>
     * This method does nothing for us because the Database application must
     * never be stopped.
     * </p>
     */
    @Override
    public void deleteAllSessions() {
    }

    /**
     * <p>
     * Return ourselves as we are also the AppSession.
     * </p>
     */
    @Override
    public AppSession getSession(int session) {
        if (session == 0) {
            return this;
        }

        return null;
    }

    //---------------------------------------------------------------------------
    // Interface AppSession
    //---------------------------------------------------------------------------
    /**
     * <p>
     * Get the application of this session.
     * </p>
     */
    public Application getApplication() {
        // We are our own Application
        return this;
    }

    /**
     * <p>
     * Return the active configuration of this application.
     * </p>
     */
    @Override
    public Configurable activeConfiguration() {
        return ACTIVE;
    }

    /**
     * <p>
     * Return the passive configuration of this application.
     * </p>
     */
    
 
    @Override
    public void reset()
    {

    }
}
