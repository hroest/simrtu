/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tui.iec60870.common;

/**
 * <p>
 * Wait() and notify() operations in a sequence can be processed
 * with a Lock object.
 *
 * @author <a href="mailto:alexis.clerc@sysaware.com">Alexis CLERC
 *         &lt;alexis.clerc@sysaware.com&gt;</a>
 */
public class Lock {
    //=============================================================================
    // Attributes
    //=============================================================================

    /**
     * Lock state : LOCKED or UNLOCKED
     */
    private boolean state;
    public boolean timeout;

    //=============================================================================
    // Constructor
    //=============================================================================
    /**
     * Build a Lock initialized as LOCKED (default)
     */
    public Lock() {
        state = false;
        timeout = false;
    }

    /**
     * Build a Lock
     *
     * @param s  Lock initialization : LOCKED or UNLOCKED
     */
    public Lock(boolean s) {
        state = s;
    }

    //=============================================================================
    // Methods
    //=============================================================================
    /**
     * Verify if this object is locked
     *
     * @return the state of this Lock object
     */
    public boolean isLocked() {
        return state;
    }

    /**
     * Lock this object
     * Waiting() functions on this object are blocking now
     */
    public synchronized void lock() {
        state = true;
        this.timeout = false;
    }

    /**
     * Unlock this object
     * All waiting() functions on this object are unlocked
     */
    public synchronized void unlock() {
        state = false;
        notifyAll();
        Thread.yield();
        this.timeout = false;
    }

    /**
     * Wait until this object is unlocked or timeout has exceeded
     *
     * @param timeout timeout in millisecond to wait
     *
     * @return <tt>true</tt> if object has been unlocked
     *         <tt>false</tt> if timeout has exceeded
     */
    public synchronized boolean waiting(long timeout) {
        this.timeout = false;
        if (state == true) {
            try {
                Thread.yield();
                wait(timeout);
                this.timeout = true;
            } catch (InterruptedException e) {
            }
        }
        return !state;
    }

    /**
     * Wait until this object is unlocked
     */
    public synchronized void waiting() {
        if (state == true) {
            try {
                Thread.yield();
                wait();
            } catch (InterruptedException e) {
            }
        }
    }
}
