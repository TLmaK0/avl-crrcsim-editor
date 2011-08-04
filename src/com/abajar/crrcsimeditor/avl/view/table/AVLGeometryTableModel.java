/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.abajar.crrcsimeditor.avl.view.table;

import com.abajar.crrcsimeditor.avl.AVLGeometry;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author hfreire
 */
public class AVLGeometryTableModel extends AVLTableModel implements TableModelListener {
    private final AVLGeometry aVLGeometry;
    protected AVLGeometryTableModel(AVLGeometry aVLGeometry){
        this.aVLGeometry = aVLGeometry;
    }

    @Override
    protected Object[] getColumns() {
        return new Object[] {"Name","Mach","iYsym","iZsym","Zsym","Sref","Cref","Bref","Xref","Yref","Zref","CDp"};
    }

    @Override
    protected Object[][] getData() {
        return new Object[][] {{
             aVLGeometry.getName()
            ,aVLGeometry.getMach()
            ,aVLGeometry.getiYiZsym()[0]
            ,aVLGeometry.getiYiZsym()[1]
            ,aVLGeometry.getZsym()
            , aVLGeometry.getSCBref()[0]
            ,aVLGeometry.getSCBref()[1]
            ,aVLGeometry.getSCBref()[2]
            ,aVLGeometry.getXYZref()[0]
            ,aVLGeometry.getXYZref()[1]
            ,aVLGeometry.getXYZref()[2]
            ,aVLGeometry.getCDp()
       }};
    }

    @Override
    public void updateAVL(TableModel tableModel){
        aVLGeometry.setName( (String) tableModel.getValueAt(0,0));
        aVLGeometry.setMach( (Float) tableModel.getValueAt(0,1));

        aVLGeometry.getiYiZsym()[0]= (Integer) tableModel.getValueAt(0, 2);
        aVLGeometry.getiYiZsym()[1]= (Integer) tableModel.getValueAt(0, 3);
        aVLGeometry.setZsym( (Float) tableModel.getValueAt(0, 4));

        aVLGeometry.getSCBref()[0] = (Float) tableModel.getValueAt(0, 5);
        aVLGeometry.getSCBref()[1]=  (Float) tableModel.getValueAt(0, 6);
        aVLGeometry.getSCBref()[2]= (Float) tableModel.getValueAt(0, 7);

        aVLGeometry.getXYZref()[0]= (Float) tableModel.getValueAt(0, 8);
        aVLGeometry.getXYZref()[1]= (Float) tableModel.getValueAt(0, 9);
        aVLGeometry.getXYZref()[2]= (Float) tableModel.getValueAt(0, 10);

        aVLGeometry.setCDp( (Float) tableModel.getValueAt(0, 11));

    }

    @Override
    public Class<?> getColumnClass(int i) {
        Class result = Float.class;
        if (i == 0) result = String.class;
        if (i == 2 || i == 3) result = Integer.class;
        return result;
    }

}
