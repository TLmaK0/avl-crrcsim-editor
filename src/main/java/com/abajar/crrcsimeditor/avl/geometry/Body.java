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
import com.abajar.crrcsimeditor.avl.AVLSerializable;
import com.abajar.crrcsimeditor.avl.mass.MassObject;
import java.io.OutputStream;
import java.io.PrintStream;
import static com.abajar.crrcsimeditor.avl.AVLGeometry.formatFloat;
import static com.abajar.crrcsimeditor.avl.AVLGeometry.formatInteger;
import java.util.ArrayList;
import com.abajar.crrcsimeditor.avl.mass.Mass;

/**
 *
 * @author hfreire
 */
@CRRCSimEditor(buttons={ENABLE_BUTTONS.DELETE, ENABLE_BUTTONS.ADD_MASS})
public class Body extends MassObject implements AVLSerializable  {
    static final long serialVersionUID = -8843371548047761515L;

    @CRRCSimEditorField(text="Body name",
        help="Body name"
    )
    private String name = "new body";

    @CRRCSimEditorField(text="Nbody",
        help="number of source-line nodes"
    )
    private int Nbody;

    @CRRCSimEditorField(text="Bspace",
        help="lengthwise node spacing parameter\r\n\r\n"
            + "3.0        equal         |   |   |   |   |   |   |   |   |\r\n"
            + "2.0        sine          || |  |   |    |    |     |     |\r\n"
            + "1.0        cosine        ||  |    |      |      |    |  ||\r\n"
            + "0.0        equal         |   |   |   |   |   |   |   |   |\r\n"
            + "-1.0        cosine        ||  |    |      |      |    |  ||\r\n"
            + "-2.0       -sine          |     |     |    |    |   |  | ||\r\n"
            + "-3.0        equal         |   |   |   |   |   |   |   |   |\r\n"
            + "The most efficient distribution (best accuracy for a given number of \r\n"
            + "vortices) is usually the cosine (1.0)"
    )
    private float Bspace;

    @CRRCSimEditorField(text="Ydupl",
        help="Y position of X-Z plane about which the current surface is\r\n"
            + "reflected to make the duplicate geometric-image surface.")
    private float Ydupl;

    @CRRCSimEditorField(text="Translate dX",
        help="offset added on to all X value in this body")
    private float dX;

    @CRRCSimEditorField(text="Translate dY",
        help="offset added on to all Y values in this body")
    private float dY;

    @CRRCSimEditorField(text="Translate dZ",
        help="offset added on to all Z values in this body")
    private float dZ;

    @CRRCSimEditorField(text="BFILE",
        help="the shape of the body as an 'airfoil' file\r\n"
            + "which gives the top or side view of the body, which is\r\n"
            + "assumed to have a round cross-section")
    private String BFILE;

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
     * @return the NBody
     */
    public int getNbody() {
        return Nbody;
    }

    /**
     * @return the Bspace
     */
    public float getBspace() {
        return Bspace;
    }

    /**
     * @return the Ydupl
     */
    public float getYdupl() {
        return Ydupl;
    }

    /**
     * @return the dX
     */
    public float getdX() {
        return dX;
    }

    /**
     * @return the dY
     */
    public float getdY() {
        return dY;
    }

    /**
     * @return the dZ
     */
    public float getdZ() {
        return dZ;
    }

    /**
     * @return the BFILE
     */
    public String getBFILE() {
        return BFILE;
    }

    @Override
    public String toString() {
        return this.getName();
    }
    
    @Override
    public void writeAVLData(OutputStream out) {
        PrintStream ps = new PrintStream(out);
        ps.print("BODY\n");                     
        ps.printf(locale, "%1$s\n", this.getName());    

        ps.printf(locale, "#Nbody  Bspace\n" + formatInteger(1) + formatFloat(1,2),
                this.getNbody(), this.getBspace());

        ps.print("\n");

        ps.print("YDUPLICATE\n");               
        ps.printf(locale, formatFloat(1) + "\n", this.getYdupl());

        if (this.getdX() != 0 ||  this.getdY() != 0 || this.getdZ() != 0){
            ps.print("TRANSLATE\n");                      
            ps.printf(locale, "#dX  dY  dZ\n" + formatFloat(3) + "\n",
                    this.getdX(), this.getdY(), this.getdZ());
        }

        ps.print("BFILE\n");
        ps.print(this.getBFILE() + "\n");
    }

    /**
     * @param Nbody the Nbody to set
     */
    public void setNbody(int Nbody) {
        this.Nbody = Nbody;
    }

    /**
     * @param Bspace the Bspace to set
     */
    public void setBspace(float Bspace) {
        this.Bspace = Bspace;
    }

    /**
     * @param Ydupl the Ydupl to set
     */
    public void setYdupl(float Ydupl) {
        this.Ydupl = Ydupl;
    }

    /**
     * @param dX the dX to set
     */
    public void setdX(float dX) {
        this.dX = dX;
    }

    /**
     * @param dY the dY to set
     */
    public void setdY(float dY) {
        this.dY = dY;
    }

    /**
     * @param dZ the dZ to set
     */
    public void setdZ(float dZ) {
        this.dZ = dZ;
    }

    /**
     * @param BFILE the BFILE to set
     */
    public void setBFILE(String BFILE) {
        this.BFILE = BFILE;
    }

    public ArrayList<Mass> getMassesRecursive(){
        return getMasses();
    }
}