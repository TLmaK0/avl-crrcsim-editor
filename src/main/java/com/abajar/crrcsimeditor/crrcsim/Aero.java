/*
 * Copyright (C) 2015  Hugo Freire Gil 
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 */
package com.abajar.crrcsimeditor.crrcsim;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author hfreire
 */
@XmlRootElement
@XmlType(propOrder={"ref","misc","pitchMoment","lift","drag","sideForce","rollMomment","yawMomment"})
public class Aero implements Serializable{
//<aero version="1" units="0">
//    <ref chord="..." span="..." area="..." speed="..." />
//    <misc Alpha_0="..." eta_loc="..." CG_arm="..." span_eff="..." />
//    <m Cm_0="..." Cm_a="..." Cm_q="..." Cm_de="..." />
//    <lift CL_0="..." CL_max="..." CL_min="..." CL_a="..." CL_q="..."
//          CL_de="..." CL_drop="..." CL_CD0="..." />
//    <drag CD_prof="..." Uexp_CD="..." CD_stall="..."
//          CD_CLsq="..." CD_AIsq="..." CD_ELsq="..." />
//    <Y CY_b="..." CY_p="..." CY_r="..." CY_dr="..." CY_da="..." />
//    <l Cl_b="..." Cl_p="..." Cl_r="..." Cl_dr="..." Cl_da="..." />
//    <n Cn_b="..." Cn_p="..." Cn_r="..." Cn_dr="..." Cn_da="..." />
//  </aero>

    private int version = 1;
    private int units = 1;
    private Reference ref = new Reference();  //ref
    private Miscellaneous misc = new Miscellaneous(); //misc
    private PitchMoment pitchMoment = new PitchMoment(); //m
    private Lift lift = new Lift();
    private Drag drag = new Drag();
    private Y Y = new Y();                                      //sideForce
    private l l = new l();    //rollMomment
    private n n = new n(); //yawMomment


    protected Aero(){

    }

    

    /**
     * @return the version
     */
    @XmlAttribute
    public int getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(int version) {
        this.version = version;
    }

    /**
     * @return the units
     */
    @XmlAttribute
    public int getUnits() {
        return units;
    }

    /**
     * @param units the units to set
     */
    public void setUnits(int units) {
        this.units = units;
    }

    /**
     * @return the ref
     */
    @XmlElement
    public Reference getRef() {
        return ref;
    }

    /**
     * @return the misc
     */
    @XmlElement
    public Miscellaneous getMisc() {
        return misc;
    }

    /**
     * @return the pitchMoment
     */
    @XmlElement(name="m")
    public PitchMoment getPitchMoment() {
        return pitchMoment;
    }

    /**
     * @return the lisft
     */
    @XmlElement
    public Lift getLift() {
        return lift;
    }

    /**
     * @return the drag
     */
    @XmlElement
    public Drag getDrag() {
        return drag;
    }

    /**
     * @return the sideForce
     */
    @XmlElement(name="Y")
    public Y getSideForce() {
        return Y;
    }

    /**
     * @return the rollMomment
     */
    @XmlElement(name="l")
    public l getRollMomment() {
        return l;
    }

    /**
     * @return the yawMomment
     */
    @XmlElement(name="n")
    public n getYawMomment() {
        return n;
    }
}