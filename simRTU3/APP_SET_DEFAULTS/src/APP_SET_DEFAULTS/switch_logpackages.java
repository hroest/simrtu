/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package APP_SET_DEFAULTS;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public final class switch_logpackages implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        if (settings.PACKAGE)
        {
            settings.PACKAGE=false;
        }
        else
        {
            settings.PACKAGE=true;
        }
    }
}
