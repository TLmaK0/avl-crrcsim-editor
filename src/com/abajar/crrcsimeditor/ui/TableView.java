/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.abajar.crrcsimeditor.ui;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;
import javax.swing.JTable;

/**
 *
 * @author Hugo
 */
public class TableView extends JTable{
    private ArrayList<ChangeSelectionListener> changeSelectionListeners = new ArrayList<ChangeSelectionListener>();

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
