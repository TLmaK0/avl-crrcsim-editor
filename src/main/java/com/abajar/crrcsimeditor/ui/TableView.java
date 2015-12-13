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

         setDefaultRenderer(Float.class, new CellRenderFloat());

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