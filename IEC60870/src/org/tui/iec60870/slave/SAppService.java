/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tui.iec60870.slave;

import org.tui.iec60870.common.AppService;
import org.tui.iec60870.common.DbLocator;
import org.tui.iec60870.common.MapAsdu;
import org.tui.iec60870.common.Layer;

/**
 *
 * @author Michael
 */
public class SAppService extends AppService { 
	
	public SAppService(MapAsdu asdu, Layer lower, DbLocator notifier, int casdu)
	{
		super(asdu, lower, notifier, casdu);
	}
        
}
