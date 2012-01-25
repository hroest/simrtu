/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package lib.interfaces.application;

/**
 *
 * @author mikra
 */
public interface Application {

        //numbers of maximum allowed sessions
        public int numberOfSessionsAllowed();

        //creator of a new Session
        public int openNewSession();

        //current number of Sessions
        public int numberOfCurrentSessions();

        //close all sessions
        public void deleteAllSessions();

        //return a selected session
        public AppSession getSession(int session);

}
