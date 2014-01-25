/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.abajar.crrcsimeditor.avl.view.table;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author hfreire
 */
public abstract class AVLTableModel extends DefaultTableModel implements TableModelListener {
    protected final String LINE_SEPARATOR = System.getProperty("line.separator");

    protected AVLTableModel getInitializedTable(){
       this.setDataVector(getData(), getColumns());
       this.addTableModelListener(this);
       return this;
    }

    @Override
    public void tableChanged(TableModelEvent tme) {
        TableModel tableModel = (TableModel)tme.getSource();
        updateAVL(tableModel);
    }


    @Override
    public Object getValueAt(int i, int i1) {
        Object value =  super.getValueAt(i, i1);
        Object returnValue;
        if (value != null && value.getClass().equals(String.class)){
            Class valueClass = getColumnClass(i1);
            if (valueClass.equals(Float.class)) returnValue = Float.valueOf((String)value);
            else returnValue = value;
        } else{
             returnValue = value;
        }
        return returnValue;
    }
    
    protected abstract void updateAVL(TableModel tableModel);
    protected abstract Object[][] getData();
    protected abstract Object[] getColumns();
    public abstract String[] getColumnsHelp();
}
