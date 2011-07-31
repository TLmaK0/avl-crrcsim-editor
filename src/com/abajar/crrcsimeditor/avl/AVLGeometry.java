/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.abajar.crrcsimeditor.avl;

import com.abajar.crrcsimeditor.avl.geometry.Body;
import com.abajar.crrcsimeditor.avl.geometry.Surface;
import java.util.ArrayList;

/**
 *
 * @author hfreire
 */
public class AVLGeometry {
    private float Mach;
    private final float[] SCBref = new float[3];
    private final float[] XYZref = new float[3];
    private float CDp;
    private final ArrayList<Surface> surfaces = new ArrayList<Surface>();
    private final ArrayList<Body> body = new ArrayList<Body>();;

    /**
     * @return the Mach
     */
    public float getMach() {
        return Mach;
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

}
