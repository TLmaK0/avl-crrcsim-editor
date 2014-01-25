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

    @Override
    public String[] getColumnsHelp() {
        return new String[]{
            "name of control variable",

            "control deflection gain, units:  degrees deflection / control variable" + LINE_SEPARATOR
            + "Maximun degrees deflection",

            "x/c location of hinge." + LINE_SEPARATOR
            + "If positive, control surface extent is Xhinge..1  (TE surface)" + LINE_SEPARATOR
            + "If negative, control surface extent is 0..-Xhinge (LE surface)" + LINE_SEPARATOR
            + "0.65 means that the hinge is at 65% of the stabilizer's chord," + LINE_SEPARATOR
            + " so the fixed part of the tail is 65% of the chor",

            "vector giving hinge axis about which surface rotates " + LINE_SEPARATOR
            + "deflection is rotation about hinge by righthand rule" + LINE_SEPARATOR
            + "1 puts the hinge along the X axis (0 in otheers)",

            "vector giving hinge axis about which surface rotates " + LINE_SEPARATOR
            + "deflection is rotation about hinge by righthand rule" + LINE_SEPARATOR
            + "1 puts the hinge along the Y axis (0 in otheers)",

            "vector giving hinge axis about which surface rotates " + LINE_SEPARATOR
            + "deflection is rotation about hinge by righthand rule" + LINE_SEPARATOR
            + "1 puts the hinge along the Z axis (0 in otheers)",

            "sign of deflection for duplicated surface" + LINE_SEPARATOR
            + "An elevator would have SgnDup = +1" + LINE_SEPARATOR
            + "An aileron  would have SgnDup = -1"}
        ;
    }

}
