/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package APP_SET_DEFAULTS;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public final class switch_debug implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        if (settings.DEBUG)
        {
            settings.DEBUG=false;
        }
        else
        {
            settings.DEBUG=true;
        }
    }
}


