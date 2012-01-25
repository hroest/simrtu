/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tui.iec60870.master;

import org.tui.iec60870.common.Mapping;
import rt_database.DataElement;
import java.util.Hashtable;
import java.util.Enumeration;

/**
 * <p>
 * This class defines basic Mapping implementation for iec package.<br>
 * The mapping is divided in two. One mapping for values an application
 * is expected to write; and another one for an application axpected to
 * read values from database OR to be notified by this database.<br>
 * <p>
 * It describes read and write methods to database, record listeners
 * methods on database.
 * <p>
 * Is also present a way to instanciate Keys according to the iec standard
 * implementation in use.
 * <p>
 * REQUEST LISTENER REGISTRATION IS NOT YET EFFECTIVE. TO SET UP ONLY
 * FOR WRITER.
 *
 * @author lionnel cauvy
 * @version 1.0
 * @date 01.II.03
 */

public abstract class MMapping extends Mapping
{
	/**
	 * Table to map database reference to LnkService.<br>
	 * Key:		Integer(index)<br>
	 * Value:	LnkService
	 */
	private Hashtable Map_DbRef_AppService;

        @Override
	public void dataEventPerformed(DataElement source)
	{
		source.addDataEventListener(this);

                // reject event with false quality flag.
		if(!source.lastRecord().isValid())
			return;
		
                // we want to write the command value
                
                MAppService proc = (MAppService)Map_DbRef_AppService.get(new Integer(source.databaseID));
                
                ((MAppManager)manager).push(proc);
                
	}

	/**
	 * <p>
	 * Maps a database reference index to a service asdu.<br>
	 * Updates Map_DbRef_AppService.<br>
	 * Records event listener in luciol database.
	 *
	 * @param proc	an application procedure to map.
	 */
	public void addProcedure(MAppService proc)
	{
		Map_DbRef_AppService.put(new Integer(proc.notifier.index),proc);
		recordEventListener(proc.notifier.index);
	}

	/**
	 * Get accessor on a single procedure from its database reference.
	 *
	 * @param dbref	Database reference.
	 *
	 * @return the mapped procedure or null.
	 */
	public MAppService getProcedure(int dbref)
	{
		Integer kdb = new Integer(dbref);
		return (MAppService)Map_DbRef_AppService.get(kdb);
	}

	/**
	 * Get accessor on all recorded procedures from this mapping.
	 *
	 * @return an enumeration procedures.
	 */
	public Enumeration enumerateProcedures()
	{
		return Map_DbRef_AppService.elements();
	}

	/**
	 * <p>
	 * Allocates a mapping for master applications.
	 *
	 * @param manager	Master application layer manager reference.
	 */
	protected MMapping(MAppManager manager)
	{
		super(manager);
		Map_DbRef_AppService	= new Hashtable();
	}
}
