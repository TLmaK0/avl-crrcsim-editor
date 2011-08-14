/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.abajar.crrcsimeditor.avl;

import java.io.Serializable;

/**
 *
 * @author hfreire
 */
public class AVL implements Serializable{
    private AVLGeometry geometry = new AVLGeometry();

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

    @Override
    public String toString() {
        return "AVL";
    }

}
