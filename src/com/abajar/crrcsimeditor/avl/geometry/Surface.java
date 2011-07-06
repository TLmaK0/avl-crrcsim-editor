/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.abajar.crrcsimeditor.avl.geometry;

/**
 *
 * @author hfreire
 */
public class Surface {
    String name;
    float Nchord, Cspace, Nspan, Sspace;

    //TODO: COMPONENT
    //TODO: YDUPLICATE
    //TODO: SCALE

    //TRANSLATE
    final float[] dXYZ = new float[3];

    //TODO: ANGLE
    float dAinc;

    //TODO: NOWAKE
    //TODO: NOALBE
    //TODO: NOLOAD

    //SECTION
    final Section[] sections = new Section[5];

    //BODY
    final Body[] body = new Body[5];
    
}
