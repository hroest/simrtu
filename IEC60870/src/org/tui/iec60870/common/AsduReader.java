/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tui.iec60870.common;

/**
 *
 * @author Micha
 */
public interface AsduReader {

    /**
	 * variable structure qualifier construction MASK_NB
	 * mask to evaluate NB in structure qualifier field
	 */
	public static final byte	MASK_NB	= 0x007F;

	/**
	 * variable structure qualifier construction MASK_SQ
	 * mask to evaluate SQ in structure qualifier field
	 */
	public static final short	MASK_SQ	= 0x0080;

	/**
	 * variable structure qualifier construction MASK_NB
	 * SQ position in structure qualifier field
	 */
	public static final byte	POS_SQ	= 0x07;
	/**
	 * Main method to construct an Asdu.
	 *
	 * @param object	the object to turn into asdu.
	 *
	 * @return the built asdu.
	 */

    public abstract Asdu build(Object object) throws IEC608705Exception;



}
