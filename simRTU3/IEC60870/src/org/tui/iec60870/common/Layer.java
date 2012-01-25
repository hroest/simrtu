/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tui.iec60870.common;

/**
 * <p>
 * This class defines properties of a generic layer for this implementation
 * of protocols based on iec 608705 standard.
 *
 * @author lionnel cauvy
 * @version 1.0
 * @date 01.II.03
 */

public abstract class Layer
{
	/**
	 * Lower layer reference.
	 */
	public Layer lower;
	/**
	 * Upper layer reference.
	 */
	public Layer upper;

	/**
	 * Called by upper layer to transmit data to this layer.
	 *
	 * @param object	the data received from upper layer.
	 */
	public abstract void receive(Object object);

	/**
	 * Called by lower layer to transmit data to this layer.
	 *
	 * @param object	the data readed from lower layer
	 */
	public abstract void read(Object object);

	/**
	 * Layer uses this call to transmit data to lower layer.
	 *
	 * @param object	the data to write to lower layer.
	 */
	public abstract void write(Object object);

        public abstract void send(Object object);

	protected abstract void launch();

        public abstract void init_from_lower();
    
        public abstract void init_from_upper();

        public abstract void comm_error(boolean val);

	/**
	 * Layer uses this call to transmit data to upper layer.
	 *
	 * @param object	the data to send to upper layer.
	 */
	

	/**
	 * Lower layer calls this method to indicate the whole application
	 * must die. For example, it can follow on from physical layer transmission
	 * error.
	 */
	public abstract void lowerKill();

	/**
	 * Kill all what is lower. Probably called by upper.
	 */
	public abstract void kill();


};