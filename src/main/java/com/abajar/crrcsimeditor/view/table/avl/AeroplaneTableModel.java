/*
 * Copyright (C) 2015  Hugo Freire Gil
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 */

package com.abajar.crrcsimeditor.view.table.avl;

import com.abajar.crrcsimeditor.crrcsim.CRRCSim;
import com.abajar.crrcsimeditor.crrcsim.CRRCSim.Description;
import javax.swing.table.TableModel;

/**
 *
 * @author Hugo
 */
public class AeroplaneTableModel extends CRRCSimTableModel{
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