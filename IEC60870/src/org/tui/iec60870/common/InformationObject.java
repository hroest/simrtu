/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tui.iec60870.common;

import java.util.Vector;

/**
 * Information object is user data contained by a frame.
 *
 * @author lionnel cauvy
 * @version 1.0
 * @date 01.II.03
 */

public class InformationObject
{
	/**
	 * Information element sequence.
	 */
	public Vector ie; //idea - reduce dimension - not as vector - higher transforming procedures for sq apps

	/**
	 * Object address.
	 */
	public int address;

	/**
	 * Add an element to information elements hold by this
	 * information object.
	 *
	 * @param element	The information element to add.
	 */
	public void setElement(InformationElement element)
	{
		ie.addElement(element);
	}

        public void setAddress(int ioa)
        {
                this.address = ioa;
        }

	/**
	 * Allocate an information object.
	 */
	public InformationObject()
	{
		ie = new Vector();
	}

}
