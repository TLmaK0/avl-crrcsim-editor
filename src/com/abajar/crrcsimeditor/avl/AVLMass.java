/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.abajar.crrcsimeditor.avl;

import com.abajar.crrcsimeditor.avl.mass.Mass;
import java.util.ArrayList;

/**
 *
 * @author hfreire
 */
public class AVLMass {

    private float Lunit;
    private float Munit;
    private float Tunit;
    private float g;
    private float rho;
    private final ArrayList<Mass> mass = new ArrayList<Mass>();

    /**
     * @return the Lunit
     */
    public float getLunit() {
        return Lunit;
    }

    /**
     * @param Lunit the Lunit to set
     */
    public void setLunit(float Lunit) {
        this.Lunit = Lunit;
    }

    /**
     * @return the Munit
     */
    public float getMunit() {
        return Munit;
    }

    /**
     * @param Munit the Munit to set
     */
    public void setMunit(float Munit) {
        this.Munit = Munit;
    }

    /**
     * @return the Tunit
     */
    public float getTunit() {
        return Tunit;
    }

    /**
     * @param Tunit the Tunit to set
     */
    public void setTunit(float Tunit) {
        this.Tunit = Tunit;
    }

    /**
     * @return the g
     */
    public float getG() {
        return g;
    }

    /**
     * @param g the g to set
     */
    public void setG(float g) {
        this.g = g;
    }

    /**
     * @return the rho
     */
    public float getRho() {
        return rho;
    }

    /**
     * @param rho the rho to set
     */
    public void setRho(float rho) {
        this.rho = rho;
    }

    /**
     * @return the mass
     */
    public ArrayList<Mass> getMass() {
        return mass;
    }
}
