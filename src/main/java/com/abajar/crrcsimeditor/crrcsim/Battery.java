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
@CRRCSimEditor(buttons={ENABLE_BUTTONS.ADD_SHAFT})
public class Battery implements Serializable {
    static final long serialVersionUID = -2002346996816014100L;

    public static final String U_0_REL ="1;1.05;0.95;0.90;0.85;0.70;0.60;";
    static float DEFAULT_C=1;
    
    @Override
    public String toString() {
        return "Battery";
    }

    @CRRCSimEditorField(text="Throttle_min",
        help="Lower limit for throttle input. Set to >0 if you want a behaviour of"+
            "a piston engine: once started, it keeps running with at least that throttle."+
            "Set to zero otherwise."+
            "This is an attribute of Battery (instead of engine) because of the brake in shaft."
    )
    private int throttle_min;

    @CRRCSimEditorField(text="C",
        help="Capacity at full charge in As"
    )
    private float C;

    @CRRCSimEditorField(text="R_I",
        help="Internal resistance in Ohm"
    )
    private int R_I;

    @CRRCSimEditorField(text="U_off",
        help="Voltage below which all the connected engines are turned off [V]"
    )
    private float U_off;

    @CRRCSimEditorField(text="U_0",
        help="Voltage at full charge [V]"
    )
    private float U_0;

    @CRRCSimEditorField(text="U_0rel",
        help="Semicolon separated values representing the proportinal voltage returned by the batery over the time. Ex. 1;1.05;0.95;0.90;0.85;0.70;0.60;\n"+
        "In this example, voltage at full charge is 1.05 * U_0"
    )
    private String U_0rel = U_0_REL;

    private ArrayList<Shaft> shafts = new ArrayList<Shaft>();
    public Battery() {
    }

    /**
     * @return the throttle_min
     */
    @XmlAttribute(name="throttle_min")
    public int getThrottle_min() {
        return throttle_min;
    }

    /**
     * @param throttle_min the throttle_min to set
     */
    public void setThrottle_min(int throttle_min) {
        this.throttle_min = throttle_min;
    }

    /**
     * @return the shafts
     */
    @CRRCSimEditorNode(name="Shafts")
    @XmlElement(name="shaft")
    public ArrayList<Shaft> getShafts() {
        return shafts;
    }

    /**
     * @param shafts the shafts to set
     */
    public void setShafts(ArrayList<Shaft> shafts) {
        this.shafts = shafts;
    }

    /**
     * @return the C
     */
    @XmlAttribute(name = "C")
    public float getC() {
        return C;
    }

    /**
     * @param C the C to set
     */
    public void setC(float C) {
        this.C = C;
    }

    /**
     * @return the R_I
     */
    @XmlAttribute(name="R_I")
    public int getR_I() {
        return R_I;
    }

    /**
     * @param R_I the R_I to set
     */
    public void setR_I(int R_I) {
        this.R_I = R_I;
    }

    /**
     * @return the U_off
     */
    @XmlAttribute(name="U_off")
    public float getU_off() {
        return U_off;
    }

    /**
     * @param U_off the U_off to set
     */
    public void setU_off(float U_off) {
        this.U_off = U_off;
    }

    /**
     * @return the U_0
     */
    @XmlAttribute(name="U_0")
    public float getU_0() {
        return U_0;
    }

    /**
     * @param U_0 the U_0 to set
     */
    public void setU_0(float U_0) {
        this.U_0 = U_0;
    }

    /**
     * @return the U_0rel
     */
    @XmlElement(name = "U_0rel")
    public String getU_0rel() {
        return U_0rel;
    }

    /**
     * @param U_0rel the U_0rel to set
     */
    public void setU_0rel(String U_0rel) {
        this.U_0rel = U_0rel;
    }

    public Object createShaft() {
        Shaft shaft = new Shaft();
        this.getShafts().add(shaft);
        return shaft;
    }
}
