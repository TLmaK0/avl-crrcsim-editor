/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.abajar.crrcsimeditor.avl.mass;

/**
 *
 * @author hfreire
 */
public class Mass {
    private String name;
    private float[] xyz = new float[3];
    private float[] Ixxyyzzxz = new float[4];

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
     * @return the xyz
     */
    public float[] getXyz() {
        return xyz;
    }

    /**
     * @return the Ixxyyzzxz
     */
    public float[] getIxxyyzzxz() {
        return Ixxyyzzxz;
    }
}
