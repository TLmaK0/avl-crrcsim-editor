/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.abajar.crrcsimeditor.avl.geometry;

import com.abajar.crrcsimeditor.avl.AVLSerializable;
import java.io.OutputStream;
import java.io.PrintStream;
import static com.abajar.crrcsimeditor.avl.AVLGeometry.fs;

/**
 *
 * @author hfreire
 */
public class Control implements AVLSerializable {
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

    @Override
    public void writeAVLData(OutputStream out) {
        PrintStream ps = new PrintStream(out);
        ps.print("CONTROL                              | (keyword)\n");                                                          //        CONTROL                              | (keyword)
        ps.printf("%1$-s " + fs(6, 2)  + "| name, gain,  Xhinge,  XYZhvec,  SgnDup\n",
                this.getName(), this.getGain(), this.getXhinge(),
                this.getXYZhvec()[0], this.getXYZhvec()[1],
                this.getXYZhvec()[2], this.getSgnDup()) ;                                                                   //elevator  1.0  0.6   0. 1. 0.   1.0  | name, gain,  Xhinge,  XYZhvec,  SgnDup

    }


}
