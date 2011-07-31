/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.abajar.crrcsimeditor.avl.geometry;

/**
 *
 * @author hfreire
 */
public class Control {
    private String name;
    private float gain;
    private float Xhinge;
    private final float[] XYZhvec = new float[3];
    private float SgnDup;

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
     * @return the gain
     */
    public float getGain() {
        return gain;
    }

    /**
     * @param gain the gain to set
     */
    public void setGain(float gain) {
        this.gain = gain;
    }

    /**
     * @return the Xhinge
     */
    public float getXhinge() {
        return Xhinge;
    }

    /**
     * @param Xhinge the Xhinge to set
     */
    public void setXhinge(float Xhinge) {
        this.Xhinge = Xhinge;
    }

    /**
     * @return the XYZhvec
     */
    public float[] getXYZhvec() {
        return XYZhvec;
    }

    /**
     * @return the SgnDup
     */
    public float getSgnDup() {
        return SgnDup;
    }

    /**
     * @param SgnDup the SgnDup to set
     */
    public void setSgnDup(float SgnDup) {
        this.SgnDup = SgnDup;
    }

}
