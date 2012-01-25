/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tui.iec60870.common;

import java.util.Vector;

/**
 *
 * @author Micha
 */

public class UserData
{
	/**
	 * 'real ft1.2' user data field
	 */
	public Vector data;
	/**
	 * Remote address supplementary field. (not iec)
	 */
	public int address;
	/**
	 *
	 */
	public UserData()
	{
		data = new Vector();
	};
};
