/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tui.iec60870.slave;

import org.tui.iec60870.IEC60870App;
import org.tui.iec60870.common.AppManager;
import org.tui.iec60870.common.AppService;
import org.tui.iec60870.common.Asdu;
import org.tui.iec60870.common.IEC608705Exception;
import org.tui.iec60870.common.PeriodicService;

import java.util.Enumeration;
import java.util.Vector;
/**
 * <p>
 * General contract is to define manager object for application
 * layer. An application manager interfaces Mapping and therefore
 * database with application layer.
 * <p>
 * One one hand, an aplication manager is in charge of handling
 * application procedure. It's the command role of such an object.
 * Notice that these mechanisms, the manager must drive, come from
 * database notification via application mapping. Or it can also be
 * internal procedures the manager launches by its own.
 * <p>
 * One the other hand, SAppManager must be able to write some protocol
 * data to the database.
 * <p>
 * Notice that for a slave application, there wiil be only one unique
 * instance of this manager. Whereas for a master application, there
 * will be as many SAppManager instances as there will be remote stations.
 *
 * @author lionnel cauvy
 * @version 1.0
 * @date 01.II.03
 */
public abstract class SAppManager extends AppManager
{
	public abstract void start();

	/**
	 * Event triggering by mapping.
	 *
	 * @param proc	The application procedure.
	 */
	public void event(AppService proc)
	{
		layer.receive(proc);
	}

        @Override
	public void triggerEvent(AppService svc)
	{
		measurands();
	}

	/**
	 * Method to build all measure's asdus.
	 */
	public void measurands()
	{
		for(Enumeration e = ((SMapping)map).enumerateMeasures();e.hasMoreElements();)
		{
			AppService proc = (AppService)e.nextElement();
			((SMapping)map).resolveMapping(proc);
			layer.receive(proc);
		}
	}
	/**
	 * Method to build all gi's asdus.
	 */
	public int gis(int flag)
	{
		int elm = 0;

                //alle GA-relevanten Typen werden einzeln nacheinander abgearbeitet
                for (Enumeration e = ((SMapping) map).enumerateGis(); e.hasMoreElements();) {
                AppService proc = (AppService) e.nextElement();
                //heraussuchen aller Werte
                try {
                    elm = elm + ((SMapping) map).resolveMapping(proc);
                    //setzen der Übertragungsursache
                    
                    if (flag==1)
                    {
                        proc.cot = (short) 20;
                    }
                    else
                    {
                        proc.cot = (short) 5;
                    }
                    layer.receive(proc);
                } catch (Exception error) {
                    IEC60870App.err("Fehler beim Mapping in SAppManager" + error, "-", 1);
                }
            }

                return elm;
	}
	/**
	 * Evaluation asdu method. According to this decoding, slave application
	 * will be able to react to the asdus sent by remote master.
	 *
	 * @param asdu	The asdu sent by remote master station.
	 */
	public abstract void evaluate(Asdu asdu) throws IEC608705Exception;
	/**
	 * Allocates an application layer manager according to local station
	 * context for a slave application and in regards to a single remote
	 * slave station for a master application.
	 *
	 * @param layer		Application layer reference.
	 * @param context	Execution context reference.
	 */
	public SAppManager(SApplicationLayer layer, int casdu, int polling)
	{
		super(layer,casdu);
		this.layer=layer;
		periodic = new Vector(1);
		
                //register PeriodicService -> see triggerevent in this class
                periodic.addElement(new PeriodicService(1000*polling,null,this));
	}
}