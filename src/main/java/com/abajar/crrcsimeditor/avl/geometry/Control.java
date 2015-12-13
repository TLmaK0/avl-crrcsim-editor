/*
 * Copyright (C) 2015  Hugo Freire Gil
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 */

package com.abajar.crrcsimeditor.avl.geometry;

import com.abajar.crrcsimeditor.view.annotations.CRRCSimEditorField;
import com.abajar.crrcsimeditor.view.avl.SelectorMutableTreeNode.ENABLE_BUTTONS;
import com.abajar.crrcsimeditor.view.annotations.CRRCSimEditor;
import javax.xml.bind.annotation.XmlElementWrapper;
import com.abajar.crrcsimeditor.avl.mass.MassObject;
import com.abajar.crrcsimeditor.avl.AVLSerializable;
import java.io.OutputStream;
import java.io.PrintStream;
import static com.abajar.crrcsimeditor.avl.AVLGeometry.formatFloat;
import java.util.ArrayList;
import com.abajar.crrcsimeditor.avl.mass.Mass;

/**
 *
 * @author hfreire
 */
@CRRCSimEditor(buttons={ENABLE_BUTTONS.DELETE})
public class Control extends MassObject implements AVLSerializable {
    static final long serialVersionUID = -2732944334457467609L;

    public static final int AILERON = 0;
    public static final int ELEVATOR = 1;
    public static final int RUDDER = 2;

    @CRRCSimEditorField(text="name",
        help="name of control variable"
    )
    private String name = "new control";

    @CRRCSimEditorField(text="gain",
        help="control deflection gain, units:  degrees deflection / control variable\r\n"
            + "Maximun degrees deflection"
    )
    private float gain;

    @CRRCSimEditorField(text="Xhinge",
        help="x/c location of hinge.\r\n"
            + "If positive, control surface extent is Xhinge..1  (TE surface)\r\n"
            + "If negative, control surface extent is 0..-Xhinge (LE surface)\r\n"
            + "0.65 means that the hinge is at 65% of the stabilizer's chord,\r\n"
            + " so the fixed part of the tail is 65% of the chor"
    )
    private float Xhinge;

    @CRRCSimEditorField(text="Xhvec",
        help="vector giving hinge axis about which surface rotates \r\n"
            + "deflection is rotation about hinge by righthand rule\r\n"
            + "1 puts the hinge along the X axis (0 in otheers)"
    )
    private float Xhvec;

    @CRRCSimEditorField(text="Yhvec",
        help="vector giving hinge axis about which surface rotates \r\n"
            + "deflection is rotation about hinge by righthand rule\r\n"
            + "1 puts the hinge along the Y axis (0 in otheers)"
    )
    private float Yhvec;

    @CRRCSimEditorField(text="Zhvec",
        help="vector giving hinge axis about which surface rotates \r\n"
            + "deflection is rotation about hinge by righthand rule\r\n"
            + "1 puts the hinge along the Z axis (0 in otheers)"
    )
    private float Zhvec;

    @CRRCSimEditorField(text="SgnDup",
        help="sign of deflection for duplicated surface\r\n"
            + "An elevator would have SgnDup = +1\r\n"
            + "An aileron  would have SgnDup = -1"
    )
    private float SgnDup;

    @CRRCSimEditorField(text="type of control",
        help="type of control:\r\n"
            + "0 -> aileron\r\n"
            + "1 -> elevator\r\n"
            + "2 -> rudder"
    )
    private int type;

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the gain
     */
    public float getGain() {
        return gain;
    }

    /**
     * @param gain the gain to set
     */
    public void setGain(float gain) {
        this.gain = gain;
    }

    /**
     * @return the Xhinge
     */
    public float getXhinge() {
        return Xhinge;
    }

    /**
     * @param Xhinge the Xhinge to set
     */
    public void setXhinge(float Xhinge) {
        this.Xhinge = Xhinge;
    }

    /**
     * @return the SgnDup
     */
    public float getSgnDup() {
        return SgnDup;
    }

    /**
     * @param SgnDup the SgnDup to set
     */
    public void setSgnDup(float SgnDup) {
        this.SgnDup = SgnDup;
    }

    @Override
    public void writeAVLData(OutputStream out) {
        PrintStream ps = new PrintStream(out);
        ps.print("CONTROL\n");                                                          //        CONTROL                              | (keyword)
        ps.printf(locale, "#name,     gain,     Xhinge,   XYZhvec,  SgnDup\n%1$-10s " + formatFloat(6, 2)  + "\n",
                this.getName(), this.getGain(), this.getXhinge(),
                this.getXhvec(), this.getYhvec(),
                this.getZhvec(), this.getSgnDup()) ;                                                                   //elevator  1.0  0.6   0. 1. 0.   1.0  | name, gain,  Xhinge,  XYZhvec,  SgnDup
    }

    @Override
    public String toString() {
        return this.getName();
    }

    /**
     * @return the Xhvec
     */
    public float getXhvec() {
        return Xhvec;
    }

    /**
     * @param Xhvec the Xhvec to set
     */
    public void setXhvec(float Xhvec) {
        this.Xhvec = Xhvec;
    }

    /**
     * @return the Yhvec
     */
    public float getYhvec() {
        return Yhvec;
    }

    /**
     * @param Yhvec the Yhvec to set
     */
    public void setYhvec(float Yhvec) {
        this.Yhvec = Yhvec;
    }

    /**
     * @return the Zhvec
     */
    public float getZhvec() {
        return Zhvec;
    }

    /**
     * @param Zhvec the Zhvec to set
     */
    public void setZhvec(float Zhvec) {
        this.Zhvec = Zhvec;
    }

    /**
     * @return the type
     */
    public int getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(int type) {
        this.type = type;
    }

    public ArrayList<Mass> getMassesRecursive() {
        return getMasses();
    }
}