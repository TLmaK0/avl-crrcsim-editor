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
public class AVLGeometryTableModel extends CRRCSimTableModel implements TableModelListener {
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
    public void updateObject(TableModel tableModel){
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

    @Override
    public String[] getColumnsHelp() {
        return new String[] {
            "Project name",
            
            "default freestream Mach number for Prandtl-Glauert correction. http://en.wikipedia.org/wiki/Mach_number. 0 should be ok for RC airplanes",

            "Allow you to draw only a part of the airplane" + LINE_SEPARATOR
            + "and left AVL to mirror about Y axis" + LINE_SEPARATOR
            + "1 case is symmetric about Y=0, (X-Z plane is a solid wall)" + LINE_SEPARATOR
            + "-1  case is antisymmetric about Y=0, (X-Z plane is at const. Cp)" + LINE_SEPARATOR
            + "0  no Y-symmetry is assumed" + LINE_SEPARATOR
            + "use 0 by default and draw all elements",
            
            "Allow you to draw only a part of the airplane"  + LINE_SEPARATOR
            + "and left AVL to mirror about Z axis"
            + "1  case is symmetric about Z=Zsym, (X-Y plane is a solid wall)" + LINE_SEPARATOR
            + "-1  case is antisymmetric about Z=Zsym, (X-Y plane is at const. Cp)" + LINE_SEPARATOR
            + "0  no Z-symmetry is assumed (Zsym ignored)" + LINE_SEPARATOR
            + "use 0 by default and draw all elements",

            "Zsym",
            
            "reference area used to define all coefficients (CL, CD, Cm, etc)" + LINE_SEPARATOR
            + "the area of the wing in square units",

            "reference chord used to define pitching moment (Cm)." + LINE_SEPARATOR
            + "the chord of the wing http://en.wikipedia.org/wiki/Chord_(aircraft)",
            
            "reference span  used to define roll,yaw moments" + LINE_SEPARATOR
            + "the wing span http://en.wikipedia.org/wiki/Wingspan",
            
            "default location about which moments and rotation rates are defined" + LINE_SEPARATOR
            + "Center of Gravity X axis position",
            
            "default location about which moments and rotation rates are defined" + LINE_SEPARATOR
            + "Center of Gravity Y axis position",

            "default location about which moments and rotation rates are defined" + LINE_SEPARATOR
            + "Center of Gravity Z axis position",

            "default profile drag coefficient added to geometry, applied at XYZref" + LINE_SEPARATOR
            + "http://en.wikipedia.org/wiki/Drag_coefficient" + LINE_SEPARATOR
            + "0.020 seems to be a common default value"};
    }

}
