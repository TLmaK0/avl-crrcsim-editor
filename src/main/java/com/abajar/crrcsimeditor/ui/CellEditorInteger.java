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
import javax.swing.JTable;
import javax.swing.JTextField;

/**
 *
 * @author Hugo
 */
public class CellEditorInteger extends CellEditor{


    public CellEditorInteger(final JTextField textField){
        super(textField);
    }

    @Override
    public Object getCellEditorValue() {
        return Integer.parseInt((String)super.getCellEditorValue());
    }
}