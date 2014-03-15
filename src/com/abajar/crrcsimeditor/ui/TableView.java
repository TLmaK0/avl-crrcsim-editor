/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.abajar.crrcsimeditor.ui;

import java.awt.Component;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;
import java.util.EventObject;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;
import javax.swing.text.JTextComponent;

/**
 *
 * @author Hugo
 */
public class TableView extends JTable{
    private ArrayList<ChangeSelectionListener> changeSelectionListeners = new ArrayList<ChangeSelectionListener>();

    public TableView() {
         setDefaultEditor(Integer.class, new CellEditorInteger(new JTextField()));
         setDefaultEditor(String.class, new CellEditorString(new JTextField()));
         setDefaultEditor(Float.class, new CellEditorFloat(new JTextField()));
    }



    @Override
    public void changeSelection(int rowIndex, int columnIndex, boolean toggle, boolean extend) {
        super.changeSelection(rowIndex, columnIndex, toggle, extend);
        notifyChangeSelection(rowIndex, columnIndex);
    }

    public void addChangeSelectionListener(ChangeSelectionListener changeSelectionListener){
        changeSelectionListeners.add(changeSelectionListener);
    }

    private void notifyChangeSelection(int rowIndex, int columnIndex) {
        for(ChangeSelectionListener changeSelectionListener: changeSelectionListeners){
            changeSelectionListener.notify(rowIndex, columnIndex);
        }
    }
}
