/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tui.iec60870;

/**
 *
 * @author Micha
 */

import APP_SET_DEFAULTS.*;

import lib.interfaces.application.Application;
import lib.interfaces.application.AppSession;
import lib.interfaces.application.Configurable;

public class IEC60870App implements Application, AppSession {

    /** Active Configuration App. */
    public static final IEC60870 ACTIVE = new IEC60870();
    /** The only instance of this class allowed. */
    private static final IEC60870App INST = new IEC60870App();

    public static void log(String description, String instance)
    {
        if (outputs.Logging)
        {
            outputs.log_low_msg(description, "IEC60870-5-104", instance);
        }
    }

    public static void err(String description, String instance, int priority)
    {
       switch (priority)
       {
           case 1:
           {
              outputs.err_low_msg(description, "IEC60870-5-104", instance);
              break;
           }
           case 2:
           {
               outputs.err_high_msg(description, "IEC60870-5-104", instance);
               break;
           }
           case 3:
           {
               outputs.err_critical_msg(description, "IEC60870-5-104", instance);
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
              outputs.msg_low_msg(description, "IEC60870-5-104", instance);
              break;
           }
           case 2:
           {
               outputs.msg_high_msg(description, "IEC60870-5-104", instance);
               break;
           }
           case 3:
           {
               outputs.msg_critical_msg(description, "IEC60870-5-104", instance);
               break;
           }
       }
    }

    public static IEC60870App getInstance() {
        return INST;
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

    @Override
    public void reset() {
    }
}
