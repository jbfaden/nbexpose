/*
 * Util.java
 *
 * Created on March 3, 2007, 10:23 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.netbeans.modules.nbexpose;

import org.netbeans.api.project.FileOwnerQuery;
import org.netbeans.api.project.Project;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataNode;
import org.openide.loaders.DataObject;
import org.openide.nodes.Node;
import org.openide.windows.TopComponent;


/**
 *
 * @author jbf
 */
public class Util {
    
    /** Creates a new instance of Util */
    public Util() {
    }
    
    protected static Node getPrimaryNode( TopComponent tc ) {
        Node[] nn= tc.getActivatedNodes();
        if ( nn==null ) return null;
        if ( nn.length>0 ) {
            Node n= nn[0];
            return n;
        } else {
            return null;
        }
    }
    
    static FileObject getFileObject( Node n ) {
        try {
            if ( n instanceof DataNode ) {
                DataNode dn= (DataNode)n;
                DataObject o;
                FileObject fo;
                o= dn.getDataObject();
                if ( o==null ) return null; else fo= o.getPrimaryFile();
                return fo;
            } else {
                return null;
            }
        } catch ( NullPointerException ex ) {
            return null;
        }
    }
    
    static FileObject getPrimaryFileFor( TopComponent tc ) {
        Node[] nodes = tc.getActivatedNodes();
        if (nodes == null) {
            return null;
        }
        DataObject obj = null;
        for (int i=0; i < nodes.length; i++) {
            obj = (DataObject) nodes[i].getCookie(DataObject.class);
            if (obj != null) {
                FileObject file = obj.getPrimaryFile();
                if (file != null) {
                    return file;
                }
            }
        }
        return null;
        
    }
    
    static String getLabelFor( TopComponent tc ) {
        String dname= tc.getDisplayName();
        if ( dname!=null ) {
            return dname;
        } else {
            return tc.getName();
        }
    }
    
    /**
     * taken from WhichProjectAction of whichproject, thanks.
     */
    static Project getProjectFor( TopComponent tc ) {
        
        Node[] nodes = tc.getActivatedNodes();
        if (nodes == null) {
            return null;
        }
        DataObject obj = null;
        for (int i=0; i < nodes.length; i++) {
            obj = (DataObject) nodes[i].getCookie(DataObject.class);
            if (obj != null) {
                FileObject file = obj.getPrimaryFile();
                if (obj != null) {
                    Project p = FileOwnerQuery.getOwner(file);
                    if (p != null) {
                        return p;
                    }
                }
            }
        }
        return null;
    }
}
