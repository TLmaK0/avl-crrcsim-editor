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
        return new Object[] {"Mach","iYsym","iZsym","Zsym","Sref","Cref","Bref","Xref","Yref","Zref","CDp"};
    }

    @Override
    protected Object[][] getData() {
        return new Object[][] {{
              aVLGeometry.getMach()
                        ,aVLGeometry.getiYiZZsym()[0]
                        ,aVLGeometry.getiYiZZsym()[1]
                        ,aVLGeometry.getiYiZZsym()[2]
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
        aVLGeometry.setMach( (Float) tableModel.getValueAt(0,0));

        aVLGeometry.getiYiZZsym()[0]= (Float) tableModel.getValueAt(0, 1);
        aVLGeometry.getiYiZZsym()[1]= (Float) tableModel.getValueAt(0, 2);
        aVLGeometry.getiYiZZsym()[2]= (Float) tableModel.getValueAt(0, 3);

        aVLGeometry.getSCBref()[0] = (Float) tableModel.getValueAt(0, 4);
        aVLGeometry.getSCBref()[1]=  (Float) tableModel.getValueAt(0, 5);
        aVLGeometry.getSCBref()[2]= (Float) tableModel.getValueAt(0, 6);

        aVLGeometry.getXYZref()[0]= (Float) tableModel.getValueAt(0, 7);
        aVLGeometry.getXYZref()[1]= (Float) tableModel.getValueAt(0, 8);
        aVLGeometry.getXYZref()[2]= (Float) tableModel.getValueAt(0, 9);

        aVLGeometry.setCDp( (Float) tableModel.getValueAt(0, 10));

    }

    @Override
    public Class<?> getColumnClass(int i) {
        return Float.class;
    }

}
