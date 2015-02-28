/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
