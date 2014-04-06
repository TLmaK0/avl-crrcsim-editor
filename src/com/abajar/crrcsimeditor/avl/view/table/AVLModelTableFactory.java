/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.abajar.crrcsimeditor.avl.view.table;

import com.abajar.crrcsimeditor.avl.AVL;
import com.abajar.crrcsimeditor.avl.AVLGeometry;
import com.abajar.crrcsimeditor.avl.geometry.Body;
import com.abajar.crrcsimeditor.avl.geometry.Control;
import com.abajar.crrcsimeditor.avl.geometry.Section;
import com.abajar.crrcsimeditor.avl.geometry.Surface;
import com.abajar.crrcsimeditor.avl.mass.Mass;
import com.abajar.crrcsimeditor.crrcsim.CRRCSim;
import com.abajar.crrcsimeditor.crrcsim.CRRCSim.Change;
import com.abajar.crrcsimeditor.crrcsim.CRRCSim.Description;
import javax.swing.table.TableModel;

/**
 *
 * @author hfreire
 */
public class AVLModelTableFactory{

    private AVLModelTableFactory(){};

    public static CRRCSimTableModel createTableModel(Object userObject) {
        CRRCSimTableModel tableModel = null;
        Class aClass = userObject.getClass();
        if (aClass.equals(AVLGeometry.class)) tableModel=new AVLGeometryTableModel((AVLGeometry)userObject).getInitializedTable();
        if (aClass.equals(Surface.class)) tableModel=new AVLSurfaceTableModel((Surface)userObject).getInitializedTable();
        if (aClass.equals(Section.class)) tableModel=new AVLSectionTableModel((Section)userObject).getInitializedTable();
        if (aClass.equals(Control.class)) tableModel=new AVLControlTableModel((Control)userObject).getInitializedTable();
        if (aClass.equals(Mass.class)) tableModel=new AVLMassTableModel((Mass)userObject).getInitializedTable();
        if (aClass.equals(Body.class)) tableModel=new AVLBodyTableModel((Body)userObject).getInitializedTable();
        if (aClass.equals(CRRCSim.class)) tableModel = new AeroplaneTableModel((CRRCSim)userObject).getInitializedTable();
        if (aClass.equals(Change.class)) tableModel = new ChangeTableModel((Change)userObject).getInitializedTable();
        if (aClass.equals(AVL.class)); //No table Model for avl
        if (tableModel == null) throw new IllegalArgumentException();
        return tableModel;
    }


}
