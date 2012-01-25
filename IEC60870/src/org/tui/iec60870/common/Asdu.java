/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tui.iec60870.common;

import java.util.Vector;


/**
 * <p>
 * This class defines generic application service data unit for
 * all iec applications.
 * <p>
 * An asdu is identified by:
 * <p>
 * - an identification type - id
 * <p>
 * - a variable structure qualifier - vsq
 * <p>
 * - a cause of transmission - cot
 * <p>
 * - a common address - caa
 * <p>
 * - a set of information objects, each one being a set of
 * information elements.
 *
 * @author lionnel cauvy
 * @version 1.0
 * @date 01.II.03
 */

public abstract class Asdu
{
	/**
	 * Remote station address (not iec). This field provides necessary information
	 * to launch read asdu evaluation for the correct remote station.
	 */
	public int address;
	/**
	 * Asdu type identification.
	 */
	public short id;
	/**
	 * Asdu variable structure qualifier.
	 */
	public short vsq;
	/**
	 * Asdu number of information objects.
	 */
	public short nio;
	/**
	 * Single/sequence flag.
	 */
	public boolean sq;
	/**
	 * Cause of transmission.
	 */
	public short cot;
	/**
	 * Common address of asdu _ 1 or 2 octets
	 */
	public int caa;
	/**
	 * Information object container.
	 */
	public Vector io;
	/**
	 * Add an information object to this asdu.
	 *
	 * @param object	the io to add.
	 */
	public void add(InformationObject object)
	{
		io.addElement(object);
	}
	/**
	 * Get accessor on a single value hold by asdu from its index positions.
	 *
	 * @param pio	Position of an io into sequence of information objects. From zero value.
	 * @param pie	Position of an ie into sequence of information elements. From zero value.
	 * @param pval	Position of value into sequence of values. From zero value.
	 *
	 * @return the value as an Object. It can be a Byte, a Short, a Math32FP, a Float....
	 */
	public abstract Object value(int ioa, int index) throws IEC608705Exception;

	/**
	 * Allocates a new Application LnkService Data Unit object.
	 */
	public Asdu()
	{
		io = new Vector();
	}
}
