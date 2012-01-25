/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tui.iec60870.common;

/**
 *
 * @author mikra
 */
public abstract class ApplicationLayer extends LayerThread {

    /**
	 * Asdu writer reference. A factory to code asdu to send.
	 */
	public AsduWriter writer;
	/**
	 * Asdu reader reference. A factory to decode received asdu.
	 */
	public AsduReader reader;

    /**
	 * Event raised by upper layer. Application layer is requested
	 * for a service. It must inform remote image, then build the
	 * related asdu and last request lower link layer to work.
	 *
	 * @param object	data from upper layer.
	 */
   
	/**
	 * <p>
	 * Allocates an application layer. Such an object handles asdus. Read from lower
	 * link layer and to write to this lower layer.
	 */
	protected ApplicationLayer(String AppName)
	{
		super(AppName);
	}
}
