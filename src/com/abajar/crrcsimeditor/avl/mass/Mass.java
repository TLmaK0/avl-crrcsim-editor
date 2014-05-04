/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.abajar.crrcsimeditor.avl.mass;

import com.abajar.crrcsimeditor.view.annotations.CRRCSimEditorField;
import com.abajar.crrcsimeditor.view.avl.SelectorMutableTreeNode.ENABLE_BUTTONS;
import com.abajar.crrcsimeditor.view.annotations.CRRCSimEditor;
import java.util.Locale;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import static com.abajar.crrcsimeditor.avl.AVLGeometry.*;

/**
 *
 * @author hfreire
 */
@XmlRootElement
@CRRCSimEditor(buttons={ENABLE_BUTTONS.DELETE})
public class Mass implements Serializable{
    protected static final Locale locale = new Locale("en");


    @CRRCSimEditorField(text="name",
        help="name. Mass objects must have absolute position."
    )
    private String name="new mass";

    @CRRCSimEditorField(text="mass",
        help="weight"
    )
    private float mass;

    @CRRCSimEditorField(text="x gravity center",
        help="x location of item's own CG"
    )
    private float x;

    @CRRCSimEditorField(text="y gravityc center",
        help="y location of item's own CG"
    )
    private float y;

    @CRRCSimEditorField(text="z gravity center",
        help="z location of item's own CG"
    )
    private float z;

    @CRRCSimEditorField(text="x inertia",
        help="object longitude over the x axis"
    )
    private float xLength;

    @CRRCSimEditorField(text="y inertia",
        help="object longitude over the y axis"
    )
    private float yLength;

    @CRRCSimEditorField(text="z inertia",
        help="object longitude over the z axis"
    )
    private float zLength;

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
        ps.printf(locale, formatFloat(7)  + "     ! %8$s\n",
                this.getMass(), this.getX(), this.getY(), this.getZ(),
                this.getIxx(), this.getIyy(),this.getIzz(),
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
        return getMass() * (float) (Math.pow(getyLength(), 2) + Math.pow(getzLength(), 2)) / 12;
    }

    /**
     * @return the Iyy
     */
    public float getIyy() {
        return getMass() * (float) (Math.pow(getxLength(), 2) + Math.pow(getzLength(), 2)) / 12;
    }

    /**
     * @return the Izz
     */
    public float getIzz() {
        return getMass() * (float) (Math.pow(getyLength(), 2) + Math.pow(getxLength(), 2)) / 12;
    }

    /**
     * @return the mass
     */
    public float getMass() {
        return mass;
    }

    /**
     * @param mass the mass to set
     */
    public void setMass(float mass) {
        this.mass = mass;
    }

    /**
     * @return the xLength
     */
    public float getxLength() {
        return xLength;
    }

    /**
     * @param xLength the xLength to set
     */
    public void setxLength(float xLength) {
        this.xLength = xLength;
    }

    /**
     * @return the yLength
     */
    public float getyLength() {
        return yLength;
    }

    /**
     * @param yLength the yLength to set
     */
    public void setyLength(float yLength) {
        this.yLength = yLength;
    }

    /**
     * @return the zLength
     */
    public float getzLength() {
        return zLength;
    }

    /**
     * @param zLength the zLength to set
     */
    public void setzLength(float zLength) {
        this.zLength = zLength;
    }
}
