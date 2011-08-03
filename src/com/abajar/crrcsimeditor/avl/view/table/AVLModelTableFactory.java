/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.abajar.crrcsimeditor.avl.view.table;

import com.abajar.crrcsimeditor.avl.AVLGeometry;
import com.abajar.crrcsimeditor.avl.geometry.Control;
import com.abajar.crrcsimeditor.avl.geometry.Section;
import com.abajar.crrcsimeditor.avl.geometry.Surface;
import javax.swing.table.TableModel;

/**
 *
 * @author hfreire
 */
public class AVLModelTableFactory{

    private AVLModelTableFactory(){};

    public static TableModel createTableModel(Object userObject) {
        TableModel tableModel = null;
        Class aClass = userObject.getClass();
        if (aClass.equals(AVLGeometry.class)) tableModel=new AVLGeometryTableModel((AVLGeometry)userObject).getInitializedTable();
        if (aClass.equals(Surface.class)) tableModel=new AVLSurfaceTableModel((Surface)userObject).getInitializedTable();
        if (aClass.equals(Section.class)) tableModel=new AVLSectionTableModel((Section)userObject).getInitializedTable();
        if (aClass.equals(Control.class)) tableModel=new AVLControlTableModel((Control)userObject).getInitializedTable();
        
        return tableModel;
    }


}
