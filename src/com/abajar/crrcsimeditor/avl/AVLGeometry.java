/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.abajar.crrcsimeditor.avl;

import com.abajar.crrcsimeditor.avl.geometry.Body;
import com.abajar.crrcsimeditor.avl.geometry.Surface;

/**
 *
 * @author hfreire
 */
public class AVLGeometry {

    float Mach;
    final float[] SCBref = new float[3];
    final float[] XYZref = new float[3];
    float CDp;
    final Surface[] surfaces = new Surface[20];
    final Body[] body = new Body[10];

}
