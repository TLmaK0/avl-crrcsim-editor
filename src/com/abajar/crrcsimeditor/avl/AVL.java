/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.abajar.crrcsimeditor.avl;

import com.abajar.crrcsimeditor.UnitConversor;
import com.abajar.crrcsimeditor.view.annotations.CRRCSimEditorField;
import com.abajar.crrcsimeditor.view.annotations.CRRCSimEditorNode;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.Serializable;

/**
 *
 * @author hfreire
 */
public class AVL implements Serializable{
    static final long serialVersionUID = 791092777497735586L;
    static final float DEFAULT_REYNOLDS_NUMBER = 1.225f;
    static final String DEFAULT_LENGTH_UNIT = "cm";
    static final String DEFAULT_MASS_UNIT = "g";
    static final String DEFAULT_TIME_UNIT = "s";
    static final float VELOCITY = 30; // 30m/s

    private AVLGeometry geometry = new AVLGeometry();

    @CRRCSimEditorField(text="Length unit (default cm)",
        help="Choose cm, m, in"
    )
    private String lengthUnit = DEFAULT_LENGTH_UNIT;

    @CRRCSimEditorField(text="Mass unit (default g)",
        help="Choose g, kg, oz"
    )
    private String massUnit = DEFAULT_MASS_UNIT;

    @CRRCSimEditorField(text="Time unit (default s)",
        help="Choose s, m, h"
    )
    private String timeUnit = DEFAULT_TIME_UNIT;

    @CRRCSimEditorField(text="Reynolds number",
        help="http://en.wikipedia.org/wiki/Reynolds_number"
    )
    private float reynoldsNumber = DEFAULT_REYNOLDS_NUMBER;

    @CRRCSimEditorField(text="Alpha",
        help="Simulation Alpha (angle of attack)"
    )
    private float alpha = 0;

    @CRRCSimEditorField(text="Velocity",
        help="Simulation velocity in Mass unit/Time unit"
    )
    private float velocity = VELOCITY;

    /**
     * @return the geometry
     */
    @CRRCSimEditorNode
    public AVLGeometry getGeometry() {
        return geometry;
    }

    /**
     * @param geometry the geometry to set
     */
    public void setGeometry(AVLGeometry geometry) {
        this.geometry = geometry;
    }

    public int getAileronPosition() throws Exception{
        return this.geometry.getAileronPosition();
    }

    public int getElevatorPosition() throws Exception{
        return this.geometry.getElevatorPosition();
    }

    public int getRudderPosition() throws Exception{
        return this.geometry.getRudderPosition();
    }

    @Override
    public String toString() {
        return "AVL";
    }

    /**
     * @return the lengthUnit
     */
    public String getLengthUnit() {
        return lengthUnit == null ? DEFAULT_LENGTH_UNIT : this.lengthUnit;
    }

    /**
     * @param lengthUnit the lengthUnit to set
     */
    public void setLengthUnit(String lengthUnit) {
        this.lengthUnit = lengthUnit;
    }

    /**
     * @return the massUnit
     */
    public String getMassUnit() {
        return massUnit == null ? DEFAULT_MASS_UNIT : this.massUnit;
    }

    /**
     * @param massUnit the massUnit to set
     */
    public void setMassUnit(String massUnit) {
        this.massUnit = massUnit;
    }

    /**
     * @return the timeUnit
     */
    public String getTimeUnit() {
        return timeUnit == null ? DEFAULT_TIME_UNIT : this.timeUnit;
    }

    /**
     * @param timeUnit the timeUnit to set
     */
    public void setTimeUnit(String timeUnit) {
        this.timeUnit = timeUnit;
    }

    void writeAVLMassData(FileOutputStream fos) {
        PrintStream ps = new PrintStream(fos);

        String lunit = "0.01 m";
        if(this.getLengthUnit().equals("m")) lunit = "1 m";
        else if(this.getLengthUnit().equals("in")) lunit = UnitConversor.INCHES_TO_METERS + " m";

        String munit = "0.001 kg";
        if(this.getMassUnit().equals("kg")) munit = "1 kg";
        else if(this.getMassUnit().equals("oz")) munit = UnitConversor.OUNCES_TO_KILOGRAMS + " kg";

        String tunit = "1.0 s";
        if(this.getTimeUnit().equals("h")) tunit = "3600 s";
        else if(this.getTimeUnit().equals("m")) tunit = "60 s";
        
        ps.print("Lunit = " + lunit + "\n" +
                    "Munit = " + munit + "\n" +
                    "Tunit = " + tunit + "\n" +
                    "g   = 9.81\n" +
                    "rho = " + this.getReynoldsNumber() + "\n");
        ps.print("#mass     x       y        z        Ixx      Iyy      Izz\n");

        this.getGeometry().writeAVLMassData(ps);
    }

    /**
     * @return the reynoldsNumber
     */
    public float getReynoldsNumber() {
        return reynoldsNumber == 0 ? DEFAULT_REYNOLDS_NUMBER : reynoldsNumber; //Old compatibility
    }

    /**
     * @param reynoldsNumber the reynoldsNumber to set
     */
    public void setReynoldsNumber(float reynoldsNumber) {
        this.reynoldsNumber = reynoldsNumber;
    }

    /**
     * @return the alpha
     */
    public float getAlpha() {
        return alpha;
    }

    /**
     * @param alpha the alpha to set
     */
    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    /**
     * @return the velocity
     */
    public float getVelocity() {
        return velocity;
    }

    /**
     * @param velocity the velocity to set
     */
    public void setVelocity(float velocity) {
        this.velocity = velocity;
    }

}
