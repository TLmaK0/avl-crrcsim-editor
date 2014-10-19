/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.abajar.crrcsimeditor.view.table.avl;

import com.abajar.crrcsimeditor.view.annotations.CRRCSimEditorField;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author hfreire
 */
public class CRRCSimTableModel extends DefaultTableModel implements TableModelListener {

    public CRRCSimTableModel getInitializedTable(){
       this.setDataVector(getData(), getColumns());
       this.addTableModelListener(this);
       return this;
    }

    @Override
    public void tableChanged(TableModelEvent tme) {
        TableModel tableModel = (TableModel)tme.getSource();
        updateObject(tableModel);
    }


    @Override
    public Object getValueAt(int i, int i1) {
        Object value =  super.getValueAt(i, i1);
        Object returnValue;
        if (value != null && value.getClass().equals(String.class)){
            Class valueClass = getColumnClass(i1);
            if (valueClass.equals(Float.class)) returnValue = Float.valueOf((String)value);
            else returnValue = value;
        } else{
             returnValue = value;
        }
        return returnValue;
    }
    
    final Object model;

    public CRRCSimTableModel(Object model) {
        this.model = model;
    }

    protected CRRCSimTableModel(){
        this.model = null;
    }

    protected void updateObject(TableModel tableModel) {
        int fields = 0;
        for(Field field : this.model.getClass().getDeclaredFields()){
            if (field.isAnnotationPresent(CRRCSimEditorField.class)){
                try {
                    field.setAccessible(true);
                    field.set(this.model, tableModel.getValueAt(0, fields));
                    fields++;
                } catch (IllegalArgumentException ex) {
                    Logger.getLogger(CRRCSimTableModel.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(CRRCSimTableModel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    protected Object[][] getData() {
        ArrayList result = new ArrayList();
        for(Field field : this.model.getClass().getDeclaredFields()){
            if (field.isAnnotationPresent(CRRCSimEditorField.class)){
                try {
                    field.setAccessible(true);
                    result.add(field.get(this.model));
                } catch (IllegalArgumentException ex) {
                    Logger.getLogger(CRRCSimTableModel.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(CRRCSimTableModel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return new Object[][]{result.toArray()};
    }

    protected Object[] getColumns() {
        ArrayList result = new ArrayList();
        for(Field field : this.model.getClass().getDeclaredFields()){
            if (field.isAnnotationPresent(CRRCSimEditorField.class)){
                CRRCSimEditorField crrcsimFieldAnnotation = field.getAnnotation(CRRCSimEditorField.class);
                result.add(crrcsimFieldAnnotation.text());
            }
        }
        return result.toArray();
    }

    @Override
    public Class<?> getColumnClass(int i) {
        ArrayList<Class<?>> result = new ArrayList<Class<?>>();
        for(Field field : this.model.getClass().getDeclaredFields()){
            if (field.isAnnotationPresent(CRRCSimEditorField.class)){
                Class<?> classType = field.getType();
                if (classType == int.class) classType = Integer.class;
                else if (classType == float.class) classType = Float.class;
                else if (classType == boolean.class) classType = Boolean.class;
                result.add(classType);
            }
        }
        return result.get(i);
    }

    public String[] getColumnsHelp() {
        ArrayList<String> result = new ArrayList<String>();
        for(Field field : this.model.getClass().getDeclaredFields()){
            if (field.isAnnotationPresent(CRRCSimEditorField.class)){
                CRRCSimEditorField crrcsimFieldAnnotation = field.getAnnotation(CRRCSimEditorField.class);
                result.add(crrcsimFieldAnnotation.help());
            }
        }
        return result.toArray(new String[result.size()]);
    }
}
