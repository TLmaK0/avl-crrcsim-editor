/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.abajar.crrcsimeditor.avl.mass;

import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.OutputStream;
import java.io.PrintStream;
import static com.abajar.crrcsimeditor.avl.AVLGeometry.*;

/**
 *
 * @author hfreire
 */
@XmlRootElement
public class Mass {
    private String name;
    private float x;
    private float y;
    private float z;
    private float Ixx;
    private float Iyy;
    private float Izz;
    private float Ixz;

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

    @Override
    public String toString() {
        return "mass: " + this.name;
    }

    public void writeAVLMassData(OutputStream out){
        PrintStream ps = new PrintStream(out);
        ps.printf(formatFloat(7)  + "     ! %8$s\n",
                this.getX(), this.getY(), this.getZ(),
                this.getIxx(), this.getIyy(), this.getIzz(), this.getIxz(),
                this.getName());
    }

    /**
     * @return the x
     */
    public float getX() {
        return x;
    }

    /**
     * @param x the x to set
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     * @return the y
     */
    public float getY() {
        return y;
    }

    /**
     * @param y the y to set
     */
    public void setY(float y) {
        this.y = y;
    }

    /**
     * @return the z
     */
    public float getZ() {
        return z;
    }

    /**
     * @param z the z to set
     */
    public void setZ(float z) {
        this.z = z;
    }

    /**
     * @return the Ixx
     */
    public float getIxx() {
        return Ixx;
    }

    /**
     * @param Ixx the Ixx to set
     */
    public void setIxx(float Ixx) {
        this.Ixx = Ixx;
    }

    /**
     * @return the Iyy
     */
    public float getIyy() {
        return Iyy;
    }

    /**
     * @param Iyy the Iyy to set
     */
    public void setIyy(float Iyy) {
        this.Iyy = Iyy;
    }

    /**
     * @return the Izz
     */
    public float getIzz() {
        return Izz;
    }

    /**
     * @param Izz the Izz to set
     */
    public void setIzz(float Izz) {
        this.Izz = Izz;
    }

    /**
     * @return the Ixz
     */
    public float getIxz() {
        return Ixz;
    }

    /**
     * @param Ixz the Ixz to set
     */
    public void setIxz(float Ixz) {
        this.Ixz = Ixz;
    }
}
