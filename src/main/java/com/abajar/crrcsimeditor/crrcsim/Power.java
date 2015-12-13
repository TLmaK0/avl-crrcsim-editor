/*
 * Copyright (C) 2015  Hugo Freire Gil
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
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
import javax.xml.bind.annotation.XmlRootElement;

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
    @CRRCSimEditorNode(name="batteries")
    @XmlElement(name="battery")
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