/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.abajar.crrcsimeditor.avl;

import com.abajar.crrcsimeditor.view.annotations.CRRCSimEditorNode;
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
    @CRRCSimEditorNode
    public AVLGeometry getGeometry() {
        return geometry;
    }

    /**
     * @param geometry the geometry to set
     */
    public void setGeometry(AVLGeometry geometry) {
        this.geometry = geometry;
    }

    public int getAileronPosition() throws Exception{
        return this.geometry.getAileronPosition();
    }

    public int getElevatorPosition() throws Exception{
        return this.geometry.getElevatorPosition();
    }

    public int getRudderPosition() throws Exception{
        return this.geometry.getRudderPosition();
    }

    @Override
    public String toString() {
        return "AVL";
    }

}
