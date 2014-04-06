/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.abajar.crrcsimeditor.avl.view.table;

import com.abajar.crrcsimeditor.crrcsim.CRRCSim.Change;
import java.util.Date;
import javax.swing.table.TableModel;

/**
 *
 * @author Hugo
 */
class ChangeTableModel  extends CRRCSimTableModel{
    final Change change;

    public ChangeTableModel(Change change) {
        this.change = change;
    }

    @Override
    protected void updateObject(TableModel tableModel) {
        this.change.setDate(new Date());
        this.change.setAuthor((String)tableModel.getValueAt(0, 0));
        this.change.setEn((String)tableModel.getValueAt(0, 1));
    }

    @Override
    protected Object[][] getData() {
        return new Object[][]{{
           this.change.getAuthor(),
           this.change.getEn()
        }};
    }

    @Override
    protected Object[] getColumns() {
        return new Object[]{"Author","Description"};
    }

    @Override
    public String[] getColumnsHelp() {
        return new String[]{
            "Author of the changes",
            "Description of the change"
                };
    }

}
