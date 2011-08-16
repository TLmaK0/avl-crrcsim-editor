/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.abajar.crrcsimeditor.avl.view.table;

import com.abajar.crrcsimeditor.avl.mass.Mass;
import javax.swing.table.TableModel;

/**
 *
 * @author hfreire
 */
public class AVLMassTableModel extends AVLTableModel {

    private final Mass mass;
    
    public AVLMassTableModel(Mass mass){
        this.mass = mass;
    }
    
    @Override
    protected void updateAVL(TableModel tableModel) {
        this.mass.setName((String)tableModel.getValueAt(0, 0));
        this.mass.getXyz()[0]=(Float)tableModel.getValueAt(0, 1);
        this.mass.getXyz()[1]=(Float)tableModel.getValueAt(0, 2);
        this.mass.getXyz()[2]=(Float)tableModel.getValueAt(0, 3);
        this.mass.getIxxyyzzxz()[0]=(Float)tableModel.getValueAt(0,4);
        this.mass.getIxxyyzzxz()[1]=(Float)tableModel.getValueAt(0,5);
        this.mass.getIxxyyzzxz()[2]=(Float)tableModel.getValueAt(0,6);
        this.mass.getIxxyyzzxz()[3]=(Float)tableModel.getValueAt(0,7);
    }

    @Override
    protected Object[][] getData() {
        return new Object[][]{{
          this.mass.getName(),
          this.mass.getXyz()[0],
          this.mass.getXyz()[1],
          this.mass.getXyz()[2],
          this.mass.getIxxyyzzxz()[0],
          this.mass.getIxxyyzzxz()[1],
          this.mass.getIxxyyzzxz()[2],
          this.mass.getIxxyyzzxz()[3]
        }};
    }

    @Override
    public Class<?> getColumnClass(int i) {
        Class result = Float.class;
        if (i == 0) result = String.class;
        return result;
    }

    @Override
    protected Object[] getColumns() {
        return new Object[]{
            "name", "x", "y", "z","xx","yy","zz","xz"
        };
    }
}