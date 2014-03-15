/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.abajar.crrcsimeditor.avl.view.table;

import com.abajar.crrcsimeditor.avl.geometry.Section;
import javax.swing.table.TableModel;

/**
 *
 * @author hfreire
 */
class AVLSectionTableModel extends AVLTableModel{

    final Section section;

    protected AVLSectionTableModel(Section section) {
        this.section = section;
    }

    @Override
    protected void updateAVL(TableModel tableModel) {
        this.section.setXle((Float)tableModel.getValueAt(0,0));
        this.section.setYle((Float)tableModel.getValueAt(0,1));
        this.section.setZle((Float)tableModel.getValueAt(0,2));
        this.section.setChord((Float)tableModel.getValueAt(0,3));
        this.section.setAinc((Float)tableModel.getValueAt(0,4));
        this.section.setNspan((Integer)tableModel.getValueAt(0,5));
        this.section.setSspace((Float)tableModel.getValueAt(0,6));
        this.section.setNACA((String)tableModel.getValueAt(0,7));
        this.section.setAFILE((String)tableModel.getValueAt(0,8));
        this.section.setX1((Float)tableModel.getValueAt(0,9));
        this.section.setX2((Float)tableModel.getValueAt(0,10));
    }

    @Override
    protected Object[][] getData() {
        return new Object[][]{{
          this.section.getXle(),
          this.section.getYle(),
          this.section.getZle(),
          this.section.getChord(),
          this.section.getAinc(),
          this.section.getNspan(),
          this.section.getSspace(),
          this.section.getNACA(),
          this.section.getAFILE(),
          this.section.getX1(),
          this.section.getX2()
        }};
    }

    @Override
    protected Object[] getColumns() {
        return new Object[]{"Xle","Yle","Zle","Chord","Ainc","Nspan","Sspace","NACA","AFILE","X1","X2"};
    }

    @Override
    public Class<?> getColumnClass(int i) {
        Class result = Float.class;
        if (i == 7 || i == 8) result = String.class;
        if (i == 5) result = Integer.class;
        return result;
    }

    @Override
    public String[] getColumnsHelp() {
        return new String[]{
            "airfoil's leading edge X location",
            "airfoil's leading edge Y location",
            "airfoil's leading edge Z location",
            "the airfoil's chord  (trailing edge is at Xle+Chord,Yle,Zle)",

            "incidence angle, taken as a rotation (+ by RH rule) about" + LINE_SEPARATOR
            + "the surface's spanwise axis projected onto the Y-Z plane",
            
            "number of spanwise vortices until the next section [ optional ]",

            "controls the spanwise spacing of the vortices      [ optional ]",
            
            "sets the camber line to the NACA 4-digit shape specified",

            "XFoil filename",

            "If present, the optional X1 X2 parameters indicate that only the" + LINE_SEPARATOR
            + "x/c range X1..X2 from the coordinates is to be assigned to the surface." + LINE_SEPARATOR
            + "If the surface is a 20%-chord flap, for example, then X1 X2" + LINE_SEPARATOR
            + "would be 0.80 1.00.  This allows the camber shape to be easily" + LINE_SEPARATOR
            + "assigned to any number of surfaces in piecewise manner.",
            
            "If present, the optional X1 X2 parameters indicate that only the" + LINE_SEPARATOR
            + "x/c range X1..X2 from the coordinates is to be assigned to the surface." + LINE_SEPARATOR
            + "If the surface is a 20%-chord flap, for example, then X1 X2" + LINE_SEPARATOR
            + "would be 0.80 1.00.  This allows the camber shape to be easily" + LINE_SEPARATOR
            + "assigned to any number of surfaces in piecewise manner."

        };
    }

}
