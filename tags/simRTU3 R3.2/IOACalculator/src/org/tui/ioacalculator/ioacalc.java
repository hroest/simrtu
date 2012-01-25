/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tui.ioacalculator;

import org.openide.cookies.EditCookie;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CookieAction;

public final class ioacalc extends CookieAction {

    protected void performAction(Node[] activatedNodes) {
        EditCookie editCookie = activatedNodes[0].getLookup().lookup(EditCookie.class);

        ioacalcTopComponent.findInstance().setVisible(true);
        
    }

    protected int mode() {
        return CookieAction.MODE_EXACTLY_ONE;
    }

    public String getName() {
        return NbBundle.getMessage(ioacalc.class, "CTL_ioacalc");
    }

    protected Class[] cookieClasses() {
        return new Class[]{EditCookie.class};
    }

    @Override
    protected void initialize() {
        super.initialize();
        // see org.openide.util.actions.SystemAction.iconResource() Javadoc for more details
        putValue("noIconInMenu", Boolean.TRUE);
    }

    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }
}

