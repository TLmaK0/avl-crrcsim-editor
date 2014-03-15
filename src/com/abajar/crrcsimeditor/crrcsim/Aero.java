/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.abajar.crrcsimeditor.crrcsim;

import com.abajar.crrcsimeditor.avl.runcase.AvlCalculation;
import com.abajar.crrcsimeditor.avl.runcase.Configuration;
import com.abajar.crrcsimeditor.avl.runcase.StabilityDerivatives;
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
public class Aero {
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
    private SideForce sideForce = new SideForce();                                      //Y
    private RollMomment rollMomment = new RollMomment();    //l
    private YawMomment yawMomment = new YawMomment(); //n


    public Aero(){

    }

    public Aero(AvlCalculation avlCalculation, int elevatorPosition, int rudderPosition, int aileronPosition){
        StabilityDerivatives std = avlCalculation.getStabilityDerivatives();
        Configuration config = avlCalculation.getConfiguration();
        
        ref.setChord(config.getCref());
        ref.setSpan(config.getBref());
        ref.setArea(config.getSref());
        ref.setSpeed(config.getVelocity());

        misc.setAlpha_0(config.getAlpha());
        //TODO: eta_loc, CG_arm, span_eff

        pitchMoment.setCm_0(config.getCmtot());
        pitchMoment.setCm_a(std.getCma());
        pitchMoment.setCm_q(std.getCmq());
        if (elevatorPosition != -1) pitchMoment.setCm_de(std.getCmd()[elevatorPosition]);

        lift.setCL_0(config.getCLtot());
        //TODO: CL_max, CL_min
        lift.setCL_a(std.getCLa());
        lift.setCL_q(std.getCLq());   //TODO: check CL_q to CLq instead of Clq
        if (elevatorPosition != -1) lift.setCL_de(std.getCld()[elevatorPosition]);
        lift.setCL_drop(0);     //TODO: check CL_drop parameter
        lift.setCL_CD0(0);      //TODO: check CL_CD0 parameter
        lift.setCL_0(config.getCLtot());

        drag.setCD_prof(config.getCDvis());
        //TODO: Uexp_CD, CD_stall, CD_CLsq, CD_AIsq, CD_ELsq

        sideForce.setCY_b(std.getCYb());
        sideForce.setCY_p(std.getCYp());
        sideForce.setCY_r(std.getCYr());
        if (rudderPosition != -1) sideForce.setCY_dr(std.getCYd()[rudderPosition]);
        if (aileronPosition != -1)sideForce.setCY_da(std.getCYd()[aileronPosition]);

        rollMomment.setCl_b(std.getClb());
        rollMomment.setCl_p(std.getClp());
        rollMomment.setCl_r(std.getClr());
        if (rudderPosition != -1) rollMomment.setCl_dr(std.getCld()[rudderPosition]);
        if (aileronPosition != -1) rollMomment.setCl_da(std.getCld()[aileronPosition]);

        yawMomment.setCn_b(std.getCnb());
        yawMomment.setCn_p(std.getCnp());
        yawMomment.setCn_r(std.getCnr());
        if (rudderPosition != -1) yawMomment.setCn_dr(std.getCnd()[rudderPosition]);
        if (aileronPosition != -1)yawMomment.setCn_da(std.getCnd()[aileronPosition]);
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
    public SideForce getSideForce() {
        return sideForce;
    }

    /**
     * @return the rollMomment
     */
    @XmlElement(name="l")
    public RollMomment getRollMomment() {
        return rollMomment;
    }

    /**
     * @return the yawMomment
     */
    @XmlElement(name="n")
    public YawMomment getYawMomment() {
        return yawMomment;
    }
}
