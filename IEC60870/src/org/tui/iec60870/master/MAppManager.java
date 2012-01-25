/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tui.iec60870.master;

import org.tui.iec60870.IEC60870App;
import org.tui.iec60870.common.AppManager;
import org.tui.iec60870.common.AppService;
import org.tui.iec60870.common.PeriodicService;
import org.tui.iec60870.common.Asdu;
import lib.util.UnicPrioQueue;
import lib.interfaces.queue.QueueException;

/**
 * <p>
 * General contract is to define manager object for application
 * layer. An application manager interfaces Mapping and therefore
 * database with application layer.
 * <p>
 * One one hand, an aplication manager is in charge of handling
 * application procedures and services. It's the command role of
 * such an object. Notice that these mechanisms manager must drive
 * come from database notification via application mapping.
 * Or it can also be internal procedures the manager launches by its
 * own. In order to handle notifications, this class implements the
 * MappingReader interface to enable the receive of procedures
 * via the mapEvent method. The listener is recorded during iniatial
 * step of Configuration.
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
public class MAppManager extends AppManager {

    /**
     * Array of initial procedures to execute on start/restart.
     */
    public MAppService initial[];
    /**
     * A fifo queue whith unique priority key. Priority are related
     * to the procedures and are defined in CodeProperties interface.
     * Each procedure from mapping or internal ones will be stored
     * into this fifo before to be launch.
     */
    public UnicPrioQueue procedures;
   
    /**
     * Flag to indicate LAZY/WORK state of this manager.
     */
    public boolean state;

    /**
     * <p>
     * Add a new application procedure to fifo.
     *
     * @param proc	The application procedure to add.
     */
    public void push(MAppService proc) {
        //started by trigger event
        //prio=2;
        procedures.enqueue(proc, 2);
        manage();
    }

    /**
     * <p>
     * Start/restart this manager.
     */
    public void start() {

        // first, copy initial procedures in procedures queue
        //prio=1
        for (int i = 0; i < initial.length; i++) {
            procedures.enqueue(initial[i], 1);
        }
        // then, just manage....

        manage();
    }

    public void reinit()
    {
        start();
    }

    public void evaluate(Asdu asdu)
    {
        map.write(asdu);
    }

    public void manage() {

        try
        {
            current = (MAppService) procedures.dequeue();
        }
        catch(QueueException e)
        {
            IEC60870App.err("Prozedur nicht gefunden", "MAppManager", 3);
        }

        while (current != null) {

            if (current != null) {
                //write asdu to application layer // example 101 layer
                layer.receive(current);
            }
            try
            {
            current = (MAppService) procedures.dequeue();
            }
            catch(QueueException e)
            {
                IEC60870App.err("Prozedur nicht gefunden", "MAppmanager", 3);
            }
        }
        //start periodic service
        if (!initialized) {
            initialized = true;
            for (int i = 0; i < periodic.size(); i++) {
                ((PeriodicService) periodic.elementAt(i)).resume();
            }
        }
    }

    /**
     * <p>
     * This event is raised by all running periodic tasks. In this case,
     * it may be polling data class 2 procedure, synchronization one, or
     * at last, general interrogation one. An identifier is returned to
     * allow to differenciate these cyclic procedures.
     * <p>
     * When application layer is so triggered, we must alert an application manager
     * to execute the procedure. One by one, according to value of count, each manager
     * will be solicitated.
     *
     * @param svc	Application service.
     * @param count	Triggering counter.( Which remote is concerned.)
     */
    @Override
    public void triggerEvent(AppService svc) {
        push((MAppService) svc);
    }

    /**
     * <p>
     * Allocates a new manager for application layer according to a single
     * remote secondary/slave station.
     *
     * @param layer		Application layer reference.
     * @param context	Execution context.
     * @param nbToPoll	Maximum number of asdus to poll during a polling cycle.
     */
    public MAppManager(MApplicationLayer layer, int casdu) {
        super(layer, casdu);
        this.layer = layer;
        procedures = new UnicPrioQueue(100);
    }
}
