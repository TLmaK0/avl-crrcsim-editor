/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.abajar.crrcsimeditor.avl.mass;

import java.io.OutputStream;
import java.io.PrintStream;
import static com.abajar.crrcsimeditor.avl.AVLGeometry.*;

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

    @Override
    public String toString() {
        return "mass: " + this.name;
    }

    public void writeAVLMassData(OutputStream out){
        PrintStream ps = new PrintStream(out);
        ps.printf(formatFloat(7)  + "     ! %8$s\n",
                this.getXyz()[0], this.getXyz()[1], this.getXyz()[2],
                this.getIxxyyzzxz()[0], this.getIxxyyzzxz()[1], this.getIxxyyzzxz()[2], this.getIxxyyzzxz()[3],
                this.getName());
    }
}
