/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tui.iec60870.slave101;

import org.tui.iec60870.IEC60870App;

import org.tui.iec60870.slave.SAppManager;
import org.tui.iec60870.slave.SMapping;
import org.tui.iec60870.slave.SApplicationLayer;

import org.tui.iec60870.common101.Interoperability101;
import org.tui.iec60870.slave101.services.SGIAppSvc101;
import org.tui.iec60870.slave101.services.SEIAppSvc101;
import org.tui.iec60870.slave101.services.SDCAppSvc101;
import org.tui.iec60870.slave101.services.SSCAppSvc101;

import org.tui.iec60870.common.Asdu;
import org.tui.iec60870.common.IEC608705Exception;
import org.tui.iec60870.common.PeriodicService;


/**
 *
 * @author Micha
 */
public class SAppManager101 extends SAppManager {

    private SGIAppSvc101 giConf;
    private SGIAppSvc101 giTerm;
    private SEIAppSvc101 endOfInit;

    /**
     * Start the slave application manager.
     */
    @Override
    public void start() {
        layer.receive(endOfInit);
        for (int i = 0; i < periodic.size(); i++) {
            ((PeriodicService) periodic.elementAt(i)).resume();
        }
    }

    @Override
    public void evaluate(Asdu asdu) throws IEC608705Exception {
        evaluate(asdu, 0);
    }

    //@todo GO HERE FOR TO MODIFY EVALUATION
    public void evaluate(Asdu asdu, int flag) throws IEC608705Exception {

        switch (asdu.id) {
            //general Interrogation
            case Interoperability101.ID_C_IC_NA_1:
                // confirmation
                IEC60870App.msg("GA Abfrage.", this.layer.toString(), 1);
                giConf.casdu = asdu.address;
                // send confirmation
                layer.receive(giConf);
                // durchführung
                int elm = gis(flag);
                giTerm.casdu = asdu.address;
                // sende GA fertig
                layer.receive(giTerm);
                IEC60870App.msg("GA Bearbeitung fertig. " + Integer.toString(elm) + " Elemente verarbeitet. CASDU: " + giTerm.casdu + ".", this.layer.toString(), 1);
                break;
            //double command
            case Interoperability101.ID_C_DC_NA_1:

                //confirm
                SDCAppSvc101 dcConfca = new SDCAppSvc101(this, casdu, Interoperability101.COT_ACTIVATIONCONF, asdu);
                layer.receive(dcConfca);
                
                // to write to the data element
                boolean res = ((SMapping)this.map).write(asdu, Interoperability101.COT_ACTIVATIONCONF);
                if (res==false)
                {
                    IEC60870App.err("Befehl nicht durchführbar.", name, 3);
                    break;
                }

                try
                {
                    Thread.sleep(500);
                }
                catch(Exception e)
                {
                    
                }

                // terminate
                //((SMapping)this.map).write(asdu, Interoperability101.COT_ACTIVATION_TERMINATION);
                SDCAppSvc101 dcConfct = new SDCAppSvc101(this, casdu, Interoperability101.COT_ACTIVATION_TERMINATION, asdu);
                layer.receive(dcConfct);
                break;
            
            // single command
            case Interoperability101.ID_C_SC_NA_1:

                //confirm
                SSCAppSvc101 scConfca = new SSCAppSvc101(this, casdu, Interoperability101.COT_ACTIVATIONCONF, asdu);
                layer.receive(scConfca);

                // to write to the data element
                boolean ressc = ((SMapping)this.map).write(asdu, Interoperability101.COT_ACTIVATIONCONF);
                if (ressc==false)
                {
                    IEC60870App.err("Befehl nicht durchführbar.", name, 3);
                    break;
                }

                try
                {
                    Thread.sleep(500);
                }
                catch(Exception e)
                {

                }

                // terminate
                //((SMapping)this.map).write(asdu, Interoperability101.COT_ACTIVATION_TERMINATION);
                SSCAppSvc101 scConfct = new SSCAppSvc101(this, casdu, Interoperability101.COT_ACTIVATION_TERMINATION, asdu);
                layer.receive(scConfct);
                break;
        }
    }

    /**
     * Allocates an application layer manager according to local station
     * context for this slave application.
     *
     * @param layer		Application layer reference.
     * @param context	Execution context.
     * @param polling	Measure periodicity in seconds.
     */
    public SAppManager101(SApplicationLayer layer, int casdu, int polling) {
        super(layer, casdu, polling);
        //define Service controlling asdus
        giConf = new SGIAppSvc101(this, casdu, Interoperability101.COT_ACTIVATIONCONF);
        giTerm = new SGIAppSvc101(this, casdu, Interoperability101.COT_ACTIVATION_TERMINATION);
        endOfInit = new SEIAppSvc101(this, casdu);
        giConf.gi = true;
        giConf.qoi = 0;
    }
}
