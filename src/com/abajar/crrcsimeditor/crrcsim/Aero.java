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
    private Y Y = new Y();                                      //sideForce
    private l l = new l();    //rollMomment
    private n n = new n(); //yawMomment


    public Aero(){

    }

    public Aero(AvlCalculation avlCalculation, int elevatorPosition, int rudderPosition, int aileronPosition){
        StabilityDerivatives std = avlCalculation.getStabilityDerivatives();
        Configuration config = avlCalculation.getConfiguration();
        
        ref.setChord(config.getCref());
        ref.setSpan(config.getBref());
        ref.setArea(config.getSref());
        ref.setSpeed(config.getVelocity());

        misc.setAlpha_0((float)(config.getAlpha() * Math.PI / 180));

        misc.setEta_loc(0.3f); //eta_loc for stall model http://en.wikipedia.org/wiki/Pseudorapidity
        misc.setCG_arm(0.25f); //The typical value CG_arm = 0.25 means that the point of application of the averaged dCL is 0.25*chord ahead of the CG.
        misc.setSpan_eff(0.95f); //span efficiency: Effective span, 0.95 for most planes, 0.85 flying wing.

        //TODO: eta_loc, CG_arm, span_eff add to editor

        pitchMoment.setCm_0(config.getCmtot());
        pitchMoment.setCm_a(std.getCma());
        pitchMoment.setCm_q(std.getCmq());
        if (elevatorPosition != -1) pitchMoment.setCm_de(std.getCmd()[elevatorPosition]);

        lift.setCL_0(config.getCLtot());

        //TODO: CL_max, CL_min add to editor
        lift.setCL_max(1.1f);
        lift.setCL_min(-0.6f);

        lift.setCL_a(std.getCLa());
        lift.setCL_q(std.getCLq()); 
        if (elevatorPosition != -1) lift.setCL_de(std.getCld()[elevatorPosition]);
        lift.setCL_drop(0.1f);     //CL drop during stall break //TODO: CL_drop add to editor
        lift.setCL_CD0(0);      //CL at minimum profile //TODO: CL_CD0 add to editor
        lift.setCL_0(config.getCLtot());

        drag.setCD_prof(config.getCDvis());

        drag.setUexp_CD(0.5f); //CD Re-scaling exponent //TODO: Uexp_CD add to editor
        drag.setCD_stall(0.5f); //drag coeff. during stalling //TODO: CD_stall add to editor
        drag.setCD_CLsq(0.01f); //d(CD)/d(CL^2), curvature of parabolic profile polar: 0.01 composites, 0.015 saggy ships, 0.02 beat up ship //TODO: CD_CLsq add to editor
        drag.setCD_AIsq(0.01f); //drag due to aileron deflection //TODO: CD_AIsq add to editor
        drag.setCD_ELsq(0f); //drag due to elevon deflection //TODO: CD_ELsq add to editor
        
        Y.setCY_b(std.getCYb());
        Y.setCY_p(std.getCYp());
        Y.setCY_r(std.getCYr());
        if (rudderPosition != -1) Y.setCY_dr(std.getCYd()[rudderPosition]);
        if (aileronPosition != -1)Y.setCY_da(std.getCYd()[aileronPosition]);

        l.setCl_b(std.getClb());
        l.setCl_p(std.getClp());
        l.setCl_r(std.getClr());
        if (rudderPosition != -1) l.setCl_dr(std.getCld()[rudderPosition]);
        if (aileronPosition != -1) l.setCl_da(std.getCld()[aileronPosition]);

        n.setCn_b(std.getCnb());
        n.setCn_p(std.getCnp());
        n.setCn_r(std.getCnr());
        if (rudderPosition != -1) n.setCn_dr(std.getCnd()[rudderPosition]);
        if (aileronPosition != -1)n.setCn_da(std.getCnd()[aileronPosition]);

        //TODO: add flap section
        //TODO: add spoilder section
        //TODO: add retract section
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
