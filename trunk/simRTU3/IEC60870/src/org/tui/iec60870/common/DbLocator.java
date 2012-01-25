/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tui.iec60870.common;

/**
 * <p>
 * To exchange value with luciol database, an iec application needs
 * to know where to read/write.<br>
 * This class defines properties to point on a database area. In fact, this
 * is just an integer that indexes a database element.
 *
 * @author lionnel cauvy
 * @version 1.0
 * @date 01.II.03
 */
public class DbLocator
{
	/**
	 * Database reference.
	 */
	public int index;
	/**
	 * Optional comment.
	 */
	public String label;
	/**
	 * <p>
	 * Instanciates a database locator with an index pointer and a label.
	 *
	 * @param index	Database index reference.
	 * @param label	Optional comment for this database index.
	 */
	public DbLocator(int index, String label)
	{
		this.index = index;
		this.label = label;
	}
}
