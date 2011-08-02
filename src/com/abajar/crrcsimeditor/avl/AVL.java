/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.abajar.crrcsimeditor.avl;

/**
 *
 * @author hfreire
 */
public class AVL{
    private AVLGeometry geometry = new AVLGeometry();
    private AVLMass mass = new AVLMass();

    /**
     * @return the geometry
     */
    public AVLGeometry getGeometry() {
        return geometry;
    }

    /**
     * @param geometry the geometry to set
     */
    public void setGeometry(AVLGeometry geometry) {
        this.geometry = geometry;
    }

    /**
     * @return the mass
     */
    public AVLMass getMass() {
        return mass;
    }

    /**
     * @param mass the mass to set
     */
    public void setMass(AVLMass mass) {
        this.mass = mass;
    }

    @Override
    public String toString() {
        return "AVL";
    }

}
