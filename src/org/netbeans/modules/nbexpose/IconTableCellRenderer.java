/*
 * IconTableCellRenderer.java
 *
 * Created on March 6, 2007, 2:50 PM
 *
 *
 */

package org.netbeans.modules.nbexpose;

import java.awt.Component;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import org.openide.windows.TopComponent;

/**
 *
 * @author Jeremy
 */
public class IconTableCellRenderer implements TableCellRenderer {
    
    DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
    
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel result = (JLabel)tcr.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, column );

        if ( !(value instanceof TopComponent ) ) {
            result.setText( String.valueOf(value) );
            result.setIcon(null);
        } else {
            TopComponent tc= (TopComponent)value;
            Image i= tc.getIcon();
            if ( i!=null ) result.setIcon( new ImageIcon( tc.getIcon() ) ); else result.setIcon(null);
            result.setText( Util.getLabelFor(tc) );
        }
        return result;
        
    }
    
    
}
