/*
 * Copyright (C) 2015  Hugo Freire Gil
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 */
package com.abajar.crrcsimeditor.ui;

import java.awt.Component;
import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;

/**
 *
 * @author Hugo
 */
public abstract class CellEditor extends DefaultCellEditor{


    public CellEditor(final JTextField textField){
        super(textField);
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        final JTextField ec = (JTextField) editorComponent;
        if (value != null) ec.setText(value.toString());
        if (isSelected) {
            ec.selectAll();
        }
        return editorComponent;
    }
}