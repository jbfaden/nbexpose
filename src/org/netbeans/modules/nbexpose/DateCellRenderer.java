/*
 * IconTableCellRenderer.java
 *
 * Created on March 6, 2007, 2:50 PM
 *
 *
 */

package org.netbeans.modules.nbexpose;

import java.awt.Component;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author Jeremy
 */
public class DateCellRenderer implements TableCellRenderer {
    
    DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
    NumberFormat nf = new DecimalFormat( "0.0" );
    DateFormat df= DateFormat.getDateInstance( );
    
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel result = (JLabel)tcr.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, column );

        if ( value==null ) {
            result.setText(java.util.ResourceBundle.getBundle("org/netbeans/modules/nbexpose/Bundle").getString("never"));
        } else if ( !(value instanceof Date ) ) {
            result.setText( String.valueOf(value) );
        } else {
            Date tc= (Date)value;
            if ( tc.getTime()==0 ) {
                result.setText("");
            } else {
                Date now= new Date();
                double secsElapsed= ( now.getTime() - tc.getTime() ) / 1000.;
                
                String human;

                if ( secsElapsed < 5. ) {
                    human=java.util.ResourceBundle.getBundle("org/netbeans/modules/nbexpose/Bundle").getString("just_now");
                } else if ( secsElapsed < 60. ) {
                    human= java.util.ResourceBundle.getBundle("org/netbeans/modules/nbexpose/Bundle").getString("moments_ago");
                } else if ( secsElapsed < 60*60. ) {
                    human= nf.format( secsElapsed/60 ) + " minutes ago";
                } else if ( secsElapsed < 24*3600 ) {
                    human= nf.format( secsElapsed/3600 ) + " hours ago";
                } else if ( secsElapsed < 20*86400 ) {
                    human= nf.format( secsElapsed/86400 ) + " days ago";
                } else {
                    human= df.format( tc );
                }
                result.setText( human );
            }
        }
        return result;
        
    }
    
    
}
