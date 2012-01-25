/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tui.iec60870.slave.services;

import org.tui.iec60870.slave.SAppManager;

import org.tui.iec60870.common.AppService;
import org.tui.iec60870.common.MapAsdu;


/**
 * <p>
 * This class defines an acknowledgement of general command service asdu.
 * <p>
 * Slave application has to answer such an asdu or a NACK one when receiving
 * a general command from master station.
 *
 * @author	lionnel cauvy
 * @version	1.0
 * @date 19.II.03
 */
public class SDataAppSvc extends AppService {

    /**
	 * <p>
	 * Instanciates a new asdu service to close general interrogation
	 * procedure.
	 *
	 * @param ack	Positive or negative acknoweldgement state.
	 * @param caa	Common address of asdu.
	 * @param ft	Function type.
	 * @param in	Information number.
	 * @param dpi	Double point information. 
	 */		


    public int dclass = 1; //can be 1 or 2
    public boolean gi = false;
    public short qoi = 0;


    public SDataAppSvc(SAppManager manager, MapAsdu asdu)
	{
		super(
			asdu,
			// related link service
			manager.lower,
			// no database notification
			null,
			// execution context
			manager.casdu
		);
	}
}
