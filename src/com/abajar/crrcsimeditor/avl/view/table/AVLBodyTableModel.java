/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.abajar.crrcsimeditor.avl.view.table;

import com.abajar.crrcsimeditor.avl.geometry.Body;
import javax.swing.table.TableModel;

/**
 *
 * @author hfreire
 */
class AVLBodyTableModel extends CRRCSimTableModel  {

    final Body body;
    protected AVLBodyTableModel(Body body) {
        this.body = body;
    }

    @Override
    protected void updateObject(TableModel tableModel) {
        this.body.setName((String)tableModel.getValueAt(0, 0));
        this.body.setNbody((Integer)tableModel.getValueAt(0, 1));
        this.body.setBspace((Float)tableModel.getValueAt(0, 2));
        this.body.setYdupl((Float)tableModel.getValueAt(0, 3));
        this.body.setdX((Float)tableModel.getValueAt(0, 4));
        this.body.setdY((Float)tableModel.getValueAt(0, 5));
        this.body.setdZ((Float)tableModel.getValueAt(0, 6));
        this.body.setBFILE((String)tableModel.getValueAt(0, 7));
    }

    @Override
    protected Object[][] getData() {
        return new Object[][]{{
            this.body.getName(),
            this.body.getNbody(),
            this.body.getBspace(),
            this.body.getYdupl(),
            this.body.getdX(),
            this.body.getdY(),
            this.body.getdZ(),
            this.body.getBFILE()
        }};
    }

    @Override
    protected Object[] getColumns() {
         return new Object[]{"Body name","Nbody","Bspace","Ydupl","Translate dX","Translate dY","Translate dZ","BFILE"};
    }

    @Override
    public Class<?> getColumnClass(int i) {
        Class result = Float.class;
        if (i == 0 || i == 7) result = String.class;
        if (i == 1) result = Integer.class;
        return result;
    }

    @Override
    public String[] getColumnsHelp() {
        return new String[]{
            "Body name",

            "number of source-line nodes",

            "lengthwise node spacing parameter" + LINE_SEPARATOR
            + "3.0        equal         |   |   |   |   |   |   |   |   |" + LINE_SEPARATOR
            + "2.0        sine          || |  |   |    |    |     |     |" + LINE_SEPARATOR
            + "1.0        cosine        ||  |    |      |      |    |  ||" + LINE_SEPARATOR
            + "0.0        equal         |   |   |   |   |   |   |   |   |" + LINE_SEPARATOR
            + "-1.0        cosine        ||  |    |      |      |    |  ||" + LINE_SEPARATOR
            + "-2.0       -sine          |     |     |    |    |   |  | ||" + LINE_SEPARATOR
            + "-3.0        equal         |   |   |   |   |   |   |   |   |" + LINE_SEPARATOR
            + "The most efficient distribution (best accuracy for a given number of " + LINE_SEPARATOR
            + "vortices) is usually the cosine (1.0)",

             "Y position of X-Z plane about which the current surface is" + LINE_SEPARATOR
            + "reflected to make the duplicate geometric-image surface.",

            "offset added on to all X value in this body",
            
            "offset added on to all Y values in this body",
            
            "offset added on to all Z values in this body",
            
            "the shape of the body as an 'airfoil' file" + LINE_SEPARATOR
            + "which gives the top or side view of the body, which is" + LINE_SEPARATOR
            + "assumed to have a round cross-section"

        };
    }
}
