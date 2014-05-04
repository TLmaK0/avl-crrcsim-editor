/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.abajar.crrcsimeditor.view.table.avl;

import com.abajar.crrcsimeditor.avl.mass.Mass;
import javax.swing.table.TableModel;

/**
 *
 * @author hfreire
 */
public class AVLMassTableModel extends CRRCSimTableModel {

    private final Mass mass;
    
    public AVLMassTableModel(Mass mass){
        this.mass = mass;
    }
    
    @Override
    protected void updateObject(TableModel tableModel) {
        this.mass.setName((String)tableModel.getValueAt(0, 0));
        this.mass.setMass((Float)tableModel.getValueAt(0, 1));
        this.mass.setX((Float)tableModel.getValueAt(0, 2));
        this.mass.setY((Float)tableModel.getValueAt(0, 3));
        this.mass.setZ((Float)tableModel.getValueAt(0, 4));
        this.mass.setxLength((Float)tableModel.getValueAt(0,5));
        this.mass.setyLength((Float)tableModel.getValueAt(0,6));
        this.mass.setzLength((Float)tableModel.getValueAt(0,7));
    }

    @Override
    protected Object[][] getData() {
        return new Object[][]{{
          this.mass.getName(),
          this.mass.getMass(),
          this.mass.getX(),
          this.mass.getY(),
          this.mass.getZ(),
          this.mass.getxLength(),
          this.mass.getyLength(),
          this.mass.getzLength(),
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
            "name", "mass", "x gravity center", "y gravityc center", "z gravity center", "x length", "y length", "z length"
        };
    }

    @Override
    public String[] getColumnsHelp() {
        return new String[]{
            "name. Mass objects must have absolute position.",
            
            "weight",

            "x location of item's own CG",
            
            "y location of item's own CG",

            "z location of item's own CG",
            
            "object longitude over the x axis",
            
            "object longitude over the y axis",
            
            "object longitude over the z axis",
        };
    }
}
