/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.abajar.crrcsimeditor.crrcsim;

import com.abajar.crrcsimeditor.view.annotations.CRRCSimEditor;
import com.abajar.crrcsimeditor.view.annotations.CRRCSimEditorField;
import com.abajar.crrcsimeditor.view.annotations.CRRCSimEditorNode;
import com.abajar.crrcsimeditor.view.avl.SelectorMutableTreeNode.ENABLE_BUTTONS;
import java.io.Serializable;
import java.util.ArrayList;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author Hugo
 */
@CRRCSimEditor(buttons={ENABLE_BUTTONS.ADD_BATTERY})
public class Power implements Serializable {

    public Battery createBattery() {
        Battery battery = new Battery();
        this.getBateries().add(battery);
        return battery;
    }

    /**
     * @return the bateries
     */
    @CRRCSimEditorNode
    @XmlElement
    public ArrayList<Battery> getBateries() {
        return bateries;
    }

    /**
     * @param bateries the bateries to set
     */
    public void setBateries(ArrayList<Battery> bateries) {
        this.bateries = bateries;
    }

    

    
    private ArrayList<Battery> bateries = new ArrayList<Battery>();
    public Power() {
    }

    @Override
    public String toString() {
        return "Power";
    }

    

    
}
