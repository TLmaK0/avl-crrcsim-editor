/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.abajar.crrcsimeditor.avl.view.table;

import com.abajar.crrcsimeditor.avl.geometry.Control;
import javax.swing.table.TableModel;

/**
 *
 * @author hfreire
 */
class AVLControlTableModel extends AVLTableModel{

    final Control control;
    protected  AVLControlTableModel(Control control) {
        this.control = control;
    }

    @Override
    protected void updateAVL(TableModel tableModel) {
        this.control.setName((String)tableModel.getValueAt(0, 0));
        this.control.setGain((Float)tableModel.getValueAt(0, 1));
        this.control.setXhinge((Float)tableModel.getValueAt(0, 2));
        this.control.setXhvec(((Float)tableModel.getValueAt(0, 3)));
        this.control.setYhvec(((Float)tableModel.getValueAt(0, 4)));
        this.control.setZhvec(((Float)tableModel.getValueAt(0, 5)));
        this.control.setSgnDup((Float)tableModel.getValueAt(0, 6));
    }

    @Override
    protected Object[][] getData() {
        return new Object[][]{{
            this.control.getName(),
            this.control.getGain(),
            this.control.getXhinge(),
            this.control.getXhvec(),
            this.control.getYhvec(),
            this.control.getZhvec(),
            this.control.getSgnDup()
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
       return new Object[]{"name", "gain",  "Xhinge",  "Xhvec",  "Yhvec",  "Zhvec", "SgnDup"};
    }

}
