/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.abajar.crrcsimeditor.avl.geometry;

import java.util.ArrayList;

/**
 *
 * @author hfreire
 */
public class Section {

    //TODO: AIRFOIL
    //TODO: AFILE
    //TODO: DESIGN

    //CONTROL
    private final float[] XYZle = new float[3];
    private float Chord;
    private float Ainc;
    private float Nspan;
    private float Sspace;
    private String NACA;
    private final ArrayList<Control> controls = new ArrayList<Control>();

    /**
     * @return the XYZle
     */
    public float[] getXYZle() {
        return XYZle;
    }

    /**
     * @return the Chord
     */
    public float getChord() {
        return Chord;
    }

    /**
     * @param Chord the Chord to set
     */
    public void setChord(float Chord) {
        this.Chord = Chord;
    }

    /**
     * @return the Ainc
     */
    public float getAinc() {
        return Ainc;
    }

    /**
     * @param Ainc the Ainc to set
     */
    public void setAinc(float Ainc) {
        this.Ainc = Ainc;
    }

    /**
     * @return the Nspan
     */
    public float getNspan() {
        return Nspan;
    }

    /**
     * @param Nspan the Nspan to set
     */
    public void setNspan(float Nspan) {
        this.Nspan = Nspan;
    }

    /**
     * @return the Sspace
     */
    public float getSspace() {
        return Sspace;
    }

    /**
     * @param Sspace the Sspace to set
     */
    public void setSspace(float Sspace) {
        this.Sspace = Sspace;
    }

    /**
     * @return the NACA
     */
    public String getNACA() {
        return NACA;
    }

    /**
     * @param NACA the NACA to set
     */
    public void setNACA(String NACA) {
        this.NACA = NACA;
    }

    /**
     * @return the controls
     */
    public ArrayList<Control> getControls() {
        return controls;
    }

    //TODO: CLAF
    //TODO: CDCL
}
