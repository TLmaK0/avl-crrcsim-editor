/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.abajar.crrcsimeditor.avl.view.table;

import com.abajar.crrcsimeditor.avl.AVLGeometry;
import com.abajar.crrcsimeditor.avl.geometry.Control;
import com.abajar.crrcsimeditor.avl.geometry.Section;
import com.abajar.crrcsimeditor.avl.geometry.Surface;
import com.abajar.crrcsimeditor.avl.mass.Mass;
import javax.swing.table.TableModel;

/**
 *
 * @author hfreire
 */
public class AVLModelTableFactory{

    private AVLModelTableFactory(){};

    public static AVLTableModel createTableModel(Object userObject) {
        AVLTableModel tableModel = null;
        Class aClass = userObject.getClass();
        if (aClass.equals(AVLGeometry.class)) tableModel=new AVLGeometryTableModel((AVLGeometry)userObject).getInitializedTable();
        if (aClass.equals(Surface.class)) tableModel=new AVLSurfaceTableModel((Surface)userObject).getInitializedTable();
        if (aClass.equals(Section.class)) tableModel=new AVLSectionTableModel((Section)userObject).getInitializedTable();
        if (aClass.equals(Control.class)) tableModel=new AVLControlTableModel((Control)userObject).getInitializedTable();
        if (aClass.equals(Mass.class)) tableModel=new AVLMassTableModel((Mass)userObject).getInitializedTable();

        return tableModel;
    }


}
