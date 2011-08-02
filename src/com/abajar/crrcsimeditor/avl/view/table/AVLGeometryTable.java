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
public class AVLGeometryTable extends DefaultTableModel implements TableModelListener {
    private final AVLGeometry aVLGeometry;
    public AVLGeometryTable(AVLGeometry aVLGeometry){
        this.aVLGeometry = aVLGeometry;
        this.initModel();
    }

    private void initModel(){
        Object[][] data = new Object[][] {{
              aVLGeometry.getMach()
                        , aVLGeometry.getSCBref()[0]
                        ,aVLGeometry.getSCBref()[1]
                        ,aVLGeometry.getSCBref()[2]
                        ,aVLGeometry.getXYZref()[0]
                        ,aVLGeometry.getXYZref()[1]
                        ,aVLGeometry.getXYZref()[2]
                        ,aVLGeometry.getCDp()
       }};
       this.setDataVector(data,new Object[] {"Mach","SCBref S","SCBref C","SCBref B","XYZref X","XYZref Y","XYZref Z","CDp"});
       this.addTableModelListener(this);
    }

    @Override
    public void tableChanged(TableModelEvent tme) {
        TableModel tableModel = (TableModel)tme.getSource();
        aVLGeometry.setMach( (Float) tableModel.getValueAt(0,0));

        aVLGeometry.getSCBref()[0] = (Float) tableModel.getValueAt(0, 1);
        aVLGeometry.getSCBref()[1]=  (Float) tableModel.getValueAt(0, 2);
        aVLGeometry.getSCBref()[2]= (Float) tableModel.getValueAt(0, 3);

        aVLGeometry.getXYZref()[0]= (Float) tableModel.getValueAt(0, 4);
        aVLGeometry.getXYZref()[1]= (Float) tableModel.getValueAt(0, 5);
        aVLGeometry.getXYZref()[2]= (Float) tableModel.getValueAt(0, 6);

        aVLGeometry.setCDp( (Float) tableModel.getValueAt(0, 7));
    }

    @Override
    public Class<?> getColumnClass(int i) {
        return Float.class;
    }

    @Override
    public Object getValueAt(int i, int i1) {
        Object value =  super.getValueAt(i, i1);

        float returnValue;
        if (value.getClass().equals(String.class)){
            returnValue = Float.valueOf((String)value);
        } else{
             returnValue = (Float)value;
        }
        return returnValue;
    }



    private float getFloatAt(int row, int column, TableModel tableModel){
        Object value = tableModel.getValueAt(row, column);

        return (Float)value;
    }
}
