/*
 * Copyright (C) 2015  Hugo Freire Gil 
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 */
package com.abajar.crrcsimeditor.avl.mass;

import com.abajar.crrcsimeditor.view.annotations.CRRCSimEditorField;
import com.abajar.crrcsimeditor.view.avl.SelectorMutableTreeNode.ENABLE_BUTTONS;
import com.abajar.crrcsimeditor.view.annotations.CRRCSimEditor;
import java.util.Locale;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import static com.abajar.crrcsimeditor.avl.AVLGeometry.*;

/**
 *
 * @author hfreire
 */
@XmlRootElement
@CRRCSimEditor(buttons={ENABLE_BUTTONS.DELETE})
public class Mass implements Serializable{
    static final long serialVersionUID = 4976225526842074852L;

    protected static final Locale locale = new Locale("en");


    @CRRCSimEditorField(text="name",
        help="name. Mass objects must have absolute position."
    )
    private String name="new mass";

    @CRRCSimEditorField(text="mass",
        help="weight"
    )
    private float mass;

    @CRRCSimEditorField(text="x gravity center",
        help="x location of item's own CG"
    )
    private float x;

    @CRRCSimEditorField(text="y gravityc center",
        help="y location of item's own CG"
    )
    private float y;

    @CRRCSimEditorField(text="z gravity center",
        help="z location of item's own CG"
    )
    private float z;

    @CRRCSimEditorField(text="x length",
        help="object length over the x axis"
        + " or an inertia if you 'xyz are inertias' to 1.\n"
        + " Crrcsim editor will calculate inertia"
    )
    private float xLength;

    @CRRCSimEditorField(text="y length",
        help="object length over the y axis.\n"
        + " or an inertia if you 'xyz are inertias' to 1.\n"
        + " Crrcsim editor will calculate inertia"
    )
    private float yLength;

    @CRRCSimEditorField(text="z length",
        help="object length over the z axis\n"
        + " or an inertia if you 'xyz are inertias' to 1.\n"
        + " Crrcsim editor will calculate inertia"
    )
    private float zLength;

    @CRRCSimEditorField(text="xyz are inertias",
        help="Put here a 1 if you use inertias instead of length in x,y,z length\n"
    )
    private boolean inertia = false;


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

    @Override
    public String toString() {
        return "mass: " + this.name;
    }

    public void writeAVLMassData(OutputStream out){
        PrintStream ps = new PrintStream(out);
        ps.printf(locale, formatFloat(7)  + "     ! %8$s\n",
                this.getMass(), this.getX(), this.getY(), this.getZ(),
                this.getIxx(), this.getIyy(),this.getIzz(),
                this.getName());
    }

    /**
     * @return the x
     */
    public float getX() {
        return x;
    }

    /**
     * @param x the x to set
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     * @return the y
     */
    public float getY() {
        return y;
    }

    /**
     * @param y the y to set
     */
    public void setY(float y) {
        this.y = y;
    }

    /**
     * @return the z
     */
    public float getZ() {
        return z;
    }

    /**
     * @param z the z to set
     */
    public void setZ(float z) {
        this.z = z;
    }

    /**
     * @return the Ixx
     */
    public float getIxx() {
        return inertia ? getxLength() : getMass() * (float) (Math.pow(getyLength(), 2) + Math.pow(getzLength(), 2)) / 12;
    }

    /**
     * @return the Iyy
     */
    public float getIyy() {
        return inertia ? getyLength() : getMass() * (float) (Math.pow(getxLength(), 2) + Math.pow(getzLength(), 2)) / 12;
    }

    /**
     * @return the Izz
     */
    public float getIzz() {
        return inertia ? getzLength() : getMass() * (float) (Math.pow(getyLength(), 2) + Math.pow(getxLength(), 2)) / 12;
    }

    /**
     * @return the Ixz
     */
    public float getIxz(){
        //Symmetric objects over axis has product of inertia 0
        return 0;
    }

    /**
     * @return the mass
     */
    public float getMass() {
        return mass;
    }

    /**
     * @param mass the mass to set
     */
    public void setMass(float mass) {
        this.mass = mass;
    }

    /**
     * @return the xLength
     */
    public float getxLength() {
        return xLength;
    }

    /**
     * @param xLength the xLength to set
     */
    public void setxLength(float xLength) {
        this.xLength = xLength;
    }

    /**
     * @return the yLength
     */
    public float getyLength() {
        return yLength;
    }

    /**
     * @param yLength the yLength to set
     */
    public void setyLength(float yLength) {
        this.yLength = yLength;
    }

    /**
     * @return the zLength
     */
    public float getzLength() {
        return zLength;
    }

    /**
     * @param zLength the zLength to set
     */
    public void setzLength(float zLength) {
        this.zLength = zLength;
    }

    /**
     * @return the inertia
     */
    public boolean isInertia() {
        return inertia;
    }

    /**
     * @param inertia the inertia to set
     */
    public void setInertia(boolean inertia) {
        this.inertia = inertia;
    }

}