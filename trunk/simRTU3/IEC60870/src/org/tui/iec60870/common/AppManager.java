/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tui.iec60870.common;

import java.util.Vector;

/**
 * <p>
 * General contract is to define manager object for application
 * layer. An application manager interfaces Mapping and therefore
 * database with application layer.
 * <p>
 * One one hand, an aplication manager is in charge of handling
 * application procedures and services. It's the command role of
 * such an object. Notice that these mechanisms, the manager must
 * drive, come from database notification via application mapping.
 * Or it can also be internal procedures the manager launches by its
 * own.
 * <p>
 * One the other hand, AppManager must be able to write some protocol
 * data to the database.
 * <p>
 * Notice that for a slave application, there wiil be only one unique
 * instance of this manager. Whereas for a master application, there
 * will be as many AppManager instances as there will be remote stations.
 *
 * @author lionnel cauvy
 * @version 1.0
 * @date 01.II.03
 */
public abstract class AppManager implements PeriodicEventListener
{
	/**
	 * Final static identifier for application manager in state OK.
	 */
	public static final boolean OK		= true;
	/**
	 * Final static identifier for application manager in state KO.
	 */
	public static final boolean KO		= false;
	/**
	 * Execution context reference.
	 */
	public int casdu=-1;
	/**
	 * Reference to a mapping object for write access. Therefore,
	 * manager is enabled to write data hold by an asdu into the
	 * database. As for the reader listener that enables reception
	 * of procedures, the listener in related Mapping that implements
	 * MappingWriter interface is recorded during initial Configuration
	 * phase.
	 */
	public Mapping map;
	
	/**
	 * Link manager reference.
	 */
	public Layer lower;
	/**
	 * Application layer reference.
	 */
	public ApplicationLayer layer;
	/**
	 * Result?
	 */
	public boolean result;
	/**
	 * Indicator of initialization.
	 */
	public boolean initialized = false;
	/**
	 * The current application procedure.
	 */
	public AppService current;
	/**
	 * A vector to store internal periodic tasks.
	 */
	public Vector periodic;

        public String name="";

	/**
	 * Allocates an application layer manager according to local station
	 * context for a slave application and in regards to a single remote
	 * slave station for a master application.
	 *
	 * @param layer		Application layer reference.
	 * @param context	Execution context.
	 */
	protected AppManager(ApplicationLayer layer, int casdu)
	{
		this.layer      = layer;
		this.casdu	= casdu;
                this.lower      = layer.lower;
	}
}
