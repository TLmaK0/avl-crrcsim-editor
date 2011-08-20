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
            ,aVLGeometry.getiYsym()
            ,aVLGeometry.getiZsym()
            ,aVLGeometry.getZsym()
            , aVLGeometry.getSref()
            ,aVLGeometry.getCref()
            ,aVLGeometry.getBref()
            ,aVLGeometry.getXref()
            ,aVLGeometry.getYref()
            ,aVLGeometry.getZref()
            ,aVLGeometry.getCDp()
       }};
    }

    @Override
    public void updateAVL(TableModel tableModel){
        aVLGeometry.setName( (String) tableModel.getValueAt(0,0));
        aVLGeometry.setMach( (Float) tableModel.getValueAt(0,1));

        aVLGeometry.setiYsym((Integer) tableModel.getValueAt(0, 2));
        aVLGeometry.setiZsym((Integer) tableModel.getValueAt(0, 3));
        aVLGeometry.setZsym( (Float) tableModel.getValueAt(0, 4));

        aVLGeometry.setSref((Float) tableModel.getValueAt(0, 5));
        aVLGeometry.setCref((Float) tableModel.getValueAt(0, 6));
        aVLGeometry.setBref((Float) tableModel.getValueAt(0, 7));

        aVLGeometry.setXref((Float) tableModel.getValueAt(0, 8));
        aVLGeometry.setYref((Float) tableModel.getValueAt(0, 9));
        aVLGeometry.setZref((Float) tableModel.getValueAt(0, 10));

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
