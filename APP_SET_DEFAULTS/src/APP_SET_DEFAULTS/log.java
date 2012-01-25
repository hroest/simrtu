/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package APP_SET_DEFAULTS;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public final class log implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        if (outputs.Logging)
        {
            outputs.Logging=false;
        }
        else
        {
            outputs.Logging=true;
        }

    }
}
