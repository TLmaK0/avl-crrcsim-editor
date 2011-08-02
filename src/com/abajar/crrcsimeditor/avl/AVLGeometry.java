/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.abajar.crrcsimeditor.avl;

import com.abajar.crrcsimeditor.avl.geometry.Body;
import com.abajar.crrcsimeditor.avl.geometry.Surface;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author hfreire
 */
public class AVLGeometry{
    private float Mach;
    private final float[] SCBref = new float[3];
    private final float[] XYZref = new float[3];
    private float CDp;
    private final ArrayList<Surface> surfaces = new ArrayList<Surface>();
    private final ArrayList<Body> body = new ArrayList<Body>();

    /**
     * @return the Mach
     */
    public float getMach() {
        return Mach;
    }

    /**
     * @param Mach the Mach to set
     */
    public void setMach(float Mach) {
        this.Mach = Mach;
    }
    
    /**
     * @return the SCBref
     */
    public float[] getSCBref() {
        return SCBref;
    }

    /**
     * @return the XYZref
     */
    public float[] getXYZref() {
        return XYZref;
    }

    /**
     * @return the CDp
     */
    public float getCDp() {
        return CDp;
    }

    /**
     * @return the surfaces
     */
    public ArrayList<Surface> getSurfaces() {
        return surfaces;
    }

    /**
     * @return the body
     */
    public ArrayList<Body> getBodies() {
        return body;
    }

    @Override
    public String toString() {
        return "Geometry";
    }

    /**
     * @param CDp the CDp to set
     */
    public void setCDp(float CDp) {
        this.CDp = CDp;
    }



}
