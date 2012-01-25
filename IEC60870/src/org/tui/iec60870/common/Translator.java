/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tui.iec60870.common;

/**
 * This interface defines generic translator objects between database
 * & an iec application. Once a precise value hold by an asdu is
 * located, an iec application can read from the database the specified
 * value. This is the read mode. Or, an iec application can write
 * a value to the database. This is the write mode. But there will
 * be probably have some conversion to make between the value it reads
 * and the value it will have to use, or the value it get and that it will
 * have to write.<br>
 * Translator objects can apply a translation operation before value exchange.
 *
 * @author lionnel cauvy
 * @version 1.0
 * @date 01.II.03
 */
public interface Translator
{
	/**
	 * Applies a translation on an input object.
	 *
	 * @param object	input data.
	 *
	 * @return the translated value as an object or the input object if no
	 * adequate translation has been found.
	 */
	public abstract Object apply(Object object);
        public abstract Object inverseapply(Object object);

}
