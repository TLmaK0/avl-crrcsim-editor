/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
        ec.setText(value.toString());
        if (isSelected) {
            ec.selectAll();
        }
        return editorComponent;
    }
}
