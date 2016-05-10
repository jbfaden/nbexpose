/*
 * ExposeTableModel.java
 *
 * Created on March 2, 2007, 4:37 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package org.netbeans.modules.nbexpose;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.table.AbstractTableModel;
import org.netbeans.api.project.Project;
import org.openide.filesystems.FileObject;
import org.openide.windows.Mode;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 *
 * @author jbf
 */
public class ExposeTableModel extends AbstractTableModel {

    TopComponent[] nodes;
    TopComponent myTc;
    public static final String PROP_READ_FRESHNESS = "read_freshness";
    public static final String PROP_SELECTED = "expose_selected";
    PropertyChangeListener propListener = new PropertyChangeListener() {
        public void propertyChange(PropertyChangeEvent e) {
            if (e.getPropertyName().equals("opened") && myTc.isOpened()) {
                update();
            } else if (e.getPropertyName().equals("activated") && myTc.isOpened()) {
                TopComponent tc = TopComponent.getRegistry().getActivated();
                tc.putClientProperty(PROP_READ_FRESHNESS, new Date(System.currentTimeMillis()));
                fireTableDataChanged();
            }
        }
    };

    /** Creates a new instance of ExposeTableModel */
    public ExposeTableModel(TopComponent tc) {
        myTc = tc;
        TopComponent.getRegistry().addPropertyChangeListener(propListener);
    }

    public void update() {
        Mode editorMode = WindowManager.getDefault().findMode("editor");
        if (editorMode != null) {
            nodes = editorMode.getTopComponents();
        }
        ArrayList nodeList = new ArrayList();
        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i].isOpened()) {
                nodeList.add(nodes[i]);
            }
        }
        nodes = (TopComponent[]) nodeList.toArray(new TopComponent[nodeList.size()]);
        /*Set set= TopComponent.getRegistry().getOpened();
        Set keep= new HashSet();
        for ( Iterator i= set.iterator(); i.hasNext(); ) {
        TopComponent tc= (TopComponent) i.next();
        //if ( tc.getDisplayName()!=null ) keep.add(tc);
        keep.add(tc);
        }
        nodes= (TopComponent[]) keep.toArray(new TopComponent[keep.size()]);*/
        fireTableDataChanged();
    }

    public int getRowCount() {
        if (nodes == null) {
            return 0;
        } else {
            return nodes.length;
        }
    }

    public int getColumnCount() {
        return 6;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            Boolean b = (Boolean) nodes[rowIndex].getClientProperty(PROP_SELECTED);
            if (b == null) {
                b = Boolean.FALSE;
            }
            return b;
        } else if (columnIndex == 1) {
            nodes[rowIndex].getClientProperty(this)
            return nodes[rowIndex];
        } else if (columnIndex == 2) {
            return getNodeType(nodes[rowIndex]);
        } else if (columnIndex == 3) {
            Project p = Util.getProjectFor(nodes[rowIndex]);
            if (p == null) {
                return "";
            } else {
                return p.getProjectDirectory().getName();
            }
        } else if (columnIndex == 4) {
            return getNodeFreshness(nodes[rowIndex]);
        } else if (columnIndex == 5) {
            return nodes[rowIndex].getClientProperty(PROP_READ_FRESHNESS);
        } else {

            throw new IllegalArgumentException("bad columnindex");
        }
    }

    private String getNodeType(TopComponent tc) {
        FileObject fo = Util.getPrimaryFileFor(tc);
        if (fo == null) {
            if (tc.getName() != null && tc.getName().contains("[ Diff ]")) {
                return "diff";
            } else {
                return "";
            }
        } else {
            return "file " + fo.getMIMEType();
        }
    }

    public String getColumnName(int column) {
        if (column == 0) {
            return "";
        } else if (column == 1) {
            return java.util.ResourceBundle.getBundle("org/netbeans/modules/nbexpose/Bundle").getString("Name");
        } else if (column == 2) {
            return java.util.ResourceBundle.getBundle("org/netbeans/modules/nbexpose/Bundle").getString("Type");
        } else if (column == 3) {
            return java.util.ResourceBundle.getBundle("org/netbeans/modules/nbexpose/Bundle").getString("Project");
        } else if (column == 4) {
            return java.util.ResourceBundle.getBundle("org/netbeans/modules/nbexpose/Bundle").getString("Modified");
        } else if (column == 5) {
            return java.util.ResourceBundle.getBundle("org/netbeans/modules/nbexpose/Bundle").getString("Exposed");
        }

        String retValue;

        retValue = super.getColumnName(column);
        return retValue;
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex==0;
    }

    public void setValueAt( Object aValue, int rowIndex, int columnIndex ) {
        if ( columnIndex==0 ) {
            super.setValueAt( aValue, rowIndex, columnIndex );
            TopComponent tc= nodes[rowIndex];
            tc.putClientProperty( PROP_SELECTED, (Boolean)aValue );
            
        } else {
            throw new IllegalArgumentException("column not editable");
        }
    }
    
    
    private Date getNodeFreshness(TopComponent tc) {
        FileObject fo = Util.getPrimaryFileFor(tc);
        if (fo == null) {
            return new Date(0);
        } else {
            return fo.lastModified();
        }
    }

    protected class ActionsProvider {

        private TopComponent[] copyTopComponents(TopComponent[] nodes) {
            TopComponent[] myTopComponents = new TopComponent[nodes.length];
            for (int i = 0; i < nodes.length; i++) {
                myTopComponents[i] = nodes[i];
            }
            return myTopComponents;
        }

        void show(int[] rows) {
            TopComponent[] myTopComponents = copyTopComponents(nodes);
            for (int i = 0; i < rows.length; i++) {
                myTopComponents[rows[i]].requestActive();
            }
        }

        void close(int[] rows) {
            TopComponent[] myTopComponents = copyTopComponents(nodes);
            for (int i = 0; i < rows.length; i++) {
                TopComponent tc = myTopComponents[rows[i]];
                tc.close();
            }
        }

        void select(int[] rows) {
            for (int i = 0; i < rows.length; i++) {
                ( (TopComponent) nodes[rows[i]] ) .putClientProperty( PROP_SELECTED, Boolean.TRUE );
            }
            fireTableDataChanged();
        }
        
        void closeSelected( ) {
            TopComponent[] myTopComponents = copyTopComponents(nodes);
            for (int i = 0; i < myTopComponents.length; i++) {
                if ( Boolean.TRUE.equals(( (TopComponent) myTopComponents[i] ).getClientProperty( PROP_SELECTED ) ) ) {
                    myTopComponents[i].close();
                }
            }
            fireTableDataChanged();            
        }
        
        void dontCloseSelected( ) {
            TopComponent[] myTopComponents = copyTopComponents(nodes);
            for (int i = 0; i < myTopComponents.length; i++) {
                if ( ! Boolean.TRUE.equals(( (TopComponent) myTopComponents[i] ).getClientProperty( PROP_SELECTED ) ) ) {
                    myTopComponents[i].close();
                }
            }
            fireTableDataChanged();            
        }
        
    }
    protected ActionsProvider actionsProvider = new ActionsProvider();

    public Class getColumnClass(int columnIndex) {
        Class retValue;
        retValue = super.getColumnClass(columnIndex);
        if (columnIndex == 0) {
            retValue = Boolean.class;
        } else if (columnIndex == 1) {
            retValue = TopComponent.class;
        } else if (columnIndex == 4) {
            retValue = Date.class;
        } else if (columnIndex == 5) {
            retValue = Date.class;
        }
        return retValue;
    }
}
