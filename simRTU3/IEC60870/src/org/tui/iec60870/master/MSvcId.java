/*
 * Class    com.itlity.protocol.iec608705.master.MSvcId;
 * File     MSvcId.java
 * Author   lionnel cauvy
 */

package org.tui.iec60870.master;



/**
 * <p>
 * This class defines an identifier for an application procedure.
 * <p>
 * AppService is identified by a type and a priority.
 *
 * @author lionnel cauvy
 * @version 1.0
 * @date 01.II.03
 */
public class MSvcId
{
	/**
	 * Associated application procedure type.
	 */
	public byte type;
	/**
	 * AppService priority.
	 */
	public int priority;
	/**
	 * <p>
	 * Allocates a procedure identifier.
	 *
	 * @param type		AppService type.
	 * @param priority	AppService priority.
	 */
	public MSvcId(byte type, int priority)
	{
		this.type = type;
		this.priority = priority;
	}
}
