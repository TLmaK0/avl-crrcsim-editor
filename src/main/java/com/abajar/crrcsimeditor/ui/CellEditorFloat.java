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
public class CellEditorFloat extends CellEditor{


    public CellEditorFloat(final JTextField textField){
        super(textField);
    }

    @Override
    public Object getCellEditorValue() {
        return Float.parseFloat((String)super.getCellEditorValue());
    }
}
