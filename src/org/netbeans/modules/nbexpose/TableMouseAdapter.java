/*
 * TableMouseAdapter.java
 *
 * Created on March 3, 2007, 6:36 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.netbeans.modules.nbexpose;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JMenuItem;

import javax.swing.JPopupMenu;
import javax.swing.JTable;

/**
 *
 * @author jbf
 */
public class TableMouseAdapter extends MouseAdapter {
    
    JPopupMenu menu;
    
    /** Creates a new instance of TableMouseAdapter */
    public TableMouseAdapter( ) {
        
    }
    
    JTable table;
    ExposeTableModel model;
    TableSorter sorter;
    
    public void closeSelected() {
        int[] rows = table.getSelectedRows();
        for (int i = 0; i < rows.length; i++) {
            rows[i] = sorter.modelIndex(rows[i]);
        }
        model.actionsProvider.close(rows);
    }
    
    public void selectSelected() {
        int[] rows = table.getSelectedRows();
        for (int i = 0; i < rows.length; i++) {
            rows[i] = sorter.modelIndex(rows[i]);
        }
        model.actionsProvider.select(rows);
    }        
    
    public void showSelected() {
        int[] rows = table.getSelectedRows();
        for (int i = 0; i < rows.length; i++) {
            rows[i] = sorter.modelIndex(rows[i]);
        }
        model.actionsProvider.show(rows);        
    }
    
    
    class ShowItemsAction extends AbstractAction {
        ShowItemsAction() { super(java.util.ResourceBundle.getBundle("org/netbeans/modules/nbexpose/Bundle").getString("showItems")); }
        public void actionPerformed( ActionEvent e ) {
            showSelected();
        }
    }
    
    class CloseItemsAction extends AbstractAction {
        CloseItemsAction() { 
            super(java.util.ResourceBundle.getBundle("org/netbeans/modules/nbexpose/Bundle").getString("closeItems")); 
        }
        public void actionPerformed( ActionEvent e ) {
            closeSelected();
        }
    }
    
    class SelectItemsAction extends AbstractAction {
        SelectItemsAction() { 
            super(java.util.ResourceBundle.getBundle("org/netbeans/modules/nbexpose/Bundle").getString("checkItems")); 
        }
        public void actionPerformed( ActionEvent e ) {
            selectSelected();
        }
    }

    private JPopupMenu getPopupMenu() {
        if ( menu==null ) {
            menu= new JPopupMenu();
            menu.add( new JMenuItem( new ShowItemsAction() ) );
            menu.add( new JMenuItem( new SelectItemsAction() ) );
            menu.add( new JMenuItem( new CloseItemsAction() ) );
        }
        return menu;
    }
    
    public void mousePressed(MouseEvent e) {
        JTable h = (JTable) e.getSource();
        
        table= h;
        sorter= (TableSorter)h.getModel();
        model= (ExposeTableModel) sorter.getTableModel();
        
        if ( e.getButton()==MouseEvent.BUTTON3 ) {
            int irow= h.rowAtPoint( e.getPoint() );
            
            if ( !h.isRowSelected(irow) ) {
                h.setRowSelectionInterval( irow, irow );
            }
            
            getPopupMenu().show(table,e.getX(),e.getY());
        }
        
    }
    
    
    public void mouseClicked(MouseEvent ev ) {
        if ( ev.getClickCount()==2 ) {
            JTable h = (JTable) ev.getSource();
            table= h;
            sorter= (TableSorter)h.getModel();
            model= (ExposeTableModel) sorter.getTableModel();
            int row= table.getSelectedRow();
            model.actionsProvider.show(new int[] { sorter.modelIndex(row) } );
        }
    }
    
    public void mouseReleased(MouseEvent e) {
        getPopupMenu().setVisible(false);
    }
    
}
