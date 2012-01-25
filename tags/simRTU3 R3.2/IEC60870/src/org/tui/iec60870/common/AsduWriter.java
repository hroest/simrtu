/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tui.iec60870.common;

/**
 *
 * @author Micha
 */
public interface AsduWriter {

    public abstract Object build(AppService svc) throws IEC608705Exception;
    
}
