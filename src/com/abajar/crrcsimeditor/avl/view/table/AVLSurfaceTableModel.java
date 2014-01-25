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

    @Override
    public String[] getColumnsHelp() {
        return new String[]{
            "Surface name, ex. Wing",

            "number of chordwise horseshoe vortices placed on the surface" + LINE_SEPARATOR
            + "8 is a good number, more vortices more acurate but more calculation time. http://en.wikipedia.org/wiki/Horseshoe_vortex",

            "chordwise vortex spacing parameter" + LINE_SEPARATOR
            + "3.0        equal         |   |   |   |   |   |   |   |   |" + LINE_SEPARATOR
            + "2.0        sine          || |  |   |    |    |     |     |" + LINE_SEPARATOR
            + "1.0        cosine        ||  |    |      |      |    |  ||" + LINE_SEPARATOR
            + "0.0        equal         |   |   |   |   |   |   |   |   |" + LINE_SEPARATOR
            + "-1.0        cosine        ||  |    |      |      |    |  ||" + LINE_SEPARATOR
            + "-2.0       -sine          |     |     |    |    |   |  | ||" + LINE_SEPARATOR
            + "-3.0        equal         |   |   |   |   |   |   |   |   |" + LINE_SEPARATOR
            + "The most efficient distribution (best accuracy for a given number of " + LINE_SEPARATOR
            + "vortices) is usually the cosine (1.0) chordwise and spanwise",

            "number of spanwise horseshoe vortices placed on the surface" + LINE_SEPARATOR
            + "8 is a good number, more vortices more acurate but more calculation time. http://en.wikipedia.org/wiki/Horseshoe_vortex",

            "spanwise vortex spacing parameter" + LINE_SEPARATOR
            + "3.0        equal         |   |   |   |   |   |   |   |   |" + LINE_SEPARATOR
            + "2.0        sine          || |  |   |    |    |     |     |" + LINE_SEPARATOR
            + "1.0        cosine        ||  |    |      |      |    |  ||" + LINE_SEPARATOR
            + "0.0        equal         |   |   |   |   |   |   |   |   |" + LINE_SEPARATOR
            + "-1.0        cosine        ||  |    |      |      |    |  ||" + LINE_SEPARATOR
            + "-2.0       -sine          |     |     |    |    |   |  | ||" + LINE_SEPARATOR
            + "-3.0        equal         |   |   |   |   |   |   |   |   |" + LINE_SEPARATOR
            + "The most efficient distribution (best accuracy for a given number of " + LINE_SEPARATOR
            + "vortices) is usually the cosine (1.0) chordwise and spanwise",

            "Y position of X-Z plane about which the current surface is" + LINE_SEPARATOR
            + "reflected to make the duplicate geometric-image surface.",

            "offset added on to all X,Y,Z values in this surface",
            
            "offset added on to all X,Y,Z values in this surface",
            
            "offset added on to all X,Y,Z values in this surface",
            
            "allows convenient changing of the incidence angle" + LINE_SEPARATOR
            + "of the entire surface without the need to change the Ainc values" + LINE_SEPARATOR
            + "for all the defining sections.  The rotation is performed about" + LINE_SEPARATOR
            + "the spanwise axis projected onto the y-z plane"

        };
    }
}
