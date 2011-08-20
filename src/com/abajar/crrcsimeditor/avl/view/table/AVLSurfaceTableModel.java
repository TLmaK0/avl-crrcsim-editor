/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.abajar.crrcsimeditor.avl.view.table;

import com.abajar.crrcsimeditor.avl.geometry.Surface;
import javax.swing.table.TableModel;

/**
 *
 * @author hfreire
 */
class AVLSurfaceTableModel extends AVLTableModel  {

    final Surface surface;
    protected AVLSurfaceTableModel(Surface surface) {
        this.surface = surface;
    }

    @Override
    protected void updateAVL(TableModel tableModel) {
        this.surface.setName((String)tableModel.getValueAt(0, 0));
        this.surface.setNchord((Integer)tableModel.getValueAt(0, 1));
        this.surface.setCspace((Float)tableModel.getValueAt(0, 2));
        this.surface.setNspan((Integer)tableModel.getValueAt(0, 3));
        this.surface.setSspace((Float)tableModel.getValueAt(0, 4));
        this.surface.setYdupl((Float)tableModel.getValueAt(0, 5));
        this.surface.setdX((Float)tableModel.getValueAt(0, 6));
        this.surface.setdY((Float)tableModel.getValueAt(0, 7));
        this.surface.setdZ((Float)tableModel.getValueAt(0, 8));
        this.surface.setdAinc((Float)tableModel.getValueAt(0, 9));
    }

    @Override
    protected Object[][] getData() {
        return new Object[][]{{
            this.surface.getName(),
            this.surface.getNchord(),
            this.surface.getCspace(),
            this.surface.getNspan(),
            this.surface.getSspace(),
            this.surface.getYdupl(),
            this.surface.getdX(),
            this.surface.getdY(),
            this.surface.getdZ(),
            this.surface.getdAinc()
        }};
    }

    @Override
    protected Object[] getColumns() {
         return new Object[]{"surface name","Nchord","Cspace","Nspan","Sspace","Ydupl","Translate dX","Translate dY","Translate dZ","ANGLE dAinc"};
    }

    @Override
    public Class<?> getColumnClass(int i) {
        Class result = Float.class;
        if (i == 0) result = String.class;
        if (i == 1 || i == 3) result = Integer.class;
        return result;
    }
}
