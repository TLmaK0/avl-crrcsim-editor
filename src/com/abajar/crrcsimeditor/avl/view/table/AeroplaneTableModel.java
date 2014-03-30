/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.abajar.crrcsimeditor.avl.view.table;

import com.abajar.crrcsimeditor.crrcsim.CRRCSim;
import com.abajar.crrcsimeditor.crrcsim.CRRCSim.Description;
import javax.swing.table.TableModel;

/**
 *
 * @author Hugo
 */
class AeroplaneTableModel extends CRRCSimTableModel{
    final CRRCSim crrcsim;

    public AeroplaneTableModel(CRRCSim crrcsim){
        this.crrcsim = crrcsim;
    }

    @Override
    protected void updateObject(TableModel tableModel) {
        this.crrcsim.getDescription().setEn((String)tableModel.getValueAt(0, 0));
    }

    @Override
    protected Object[][] getData() {
        return new Object[][]{{
            this.crrcsim.getDescription().getEn(),
        }};
    }

    @Override
    protected Object[] getColumns() {
         return new Object[]{"Description"};
    }

    @Override
    public Class<?> getColumnClass(int i) {
        return String.class;
    }

    @Override
    public String[] getColumnsHelp() {
        return new String[]{
            "Description of the airplane"
        };
    }
}
