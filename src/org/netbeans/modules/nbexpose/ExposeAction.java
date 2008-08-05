package org.netbeans.modules.nbexpose;

import java.awt.Image;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;
import org.openide.windows.TopComponent;

/**
 * Action which shows Expose component.
 */
public class ExposeAction extends AbstractAction {
    
    public ExposeAction() {
        super(NbBundle.getMessage(ExposeAction.class, "CTL_ExposeAction"));
        Image image= Utilities.loadImage("/org/netbeans/modules/nbexpose/icon16.png", true); 
        ImageIcon icon= new ImageIcon( image );
        putValue( SMALL_ICON, icon );
    }
    
    public void actionPerformed(ActionEvent evt) {
        TopComponent win = ExposeTopComponent.findInstance();
        win.open();
        win.requestActive();
    }
    
}
