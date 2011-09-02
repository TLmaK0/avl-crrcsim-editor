/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.abajar.crrcsimeditor.crrcsim;

import com.abajar.crrcsimeditor.avl.runcase.AvlCalculation;
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
        ref.setChord(avlCalculation.getConfiguration().getCref());
        ref.setSpan(avlCalculation.getConfiguration().getBref());
        ref.setArea(avlCalculation.getConfiguration().getSref());
        ref.setSpeed(avlCalculation.getConfiguration().getVelocity());

        misc.setAlpha_0(avlCalculation.getConfiguration().getAlpha());
        //TODO: eta_loc, CG_arm, span_eff

        pitchMoment.setCm_0(avlCalculation.getConfiguration().getCmtot());
        pitchMoment.setCm_a(avlCalculation.getStabilityDerivatives().getCma());
        pitchMoment.setCm_q(avlCalculation.getStabilityDerivatives().getCmq());
        pitchMoment.setCm_de(avlCalculation.getStabilityDerivatives().getCmd()[elevatorPosition]);

        lift.setCL_0(avlCalculation.getConfiguration().getCLtot());
        //TODO: CL_max, CL_min
        lift.setCL_a(avlCalculation.getStabilityDerivatives().getCLa());
        lift.setCL_q(avlCalculation.getStabilityDerivatives().getCLq());   //TODO: check CL_q to CLq instead of Clq
        lift.setCL_de(avlCalculation.getStabilityDerivatives().getCld()[elevatorPosition]);
        lift.setCL_drop(0);     //TODO: check CL_drop parameter
        lift.setCL_CD0(0);      //TODO: check CL_CD0 parameter

        drag.setCD_prof(avlCalculation.getConfiguration().getCDvis());
        //TODO: Uexp_CD, CD_stall, CD_CLsq, CD_AIsq, CD_ELsq

        sideForce.setCY_b(avlCalculation.getStabilityDerivatives().getCYb());
        sideForce.setCY_p(avlCalculation.getStabilityDerivatives().getCYp());
        sideForce.setCY_r(avlCalculation.getStabilityDerivatives().getCYr());
        sideForce.setCY_dr(avlCalculation.getStabilityDerivatives().getCYd()[rudderPosition]);
        sideForce.setCY_da(avlCalculation.getStabilityDerivatives().getCYd()[aileronPosition]);

        rollMomment.setCl_b(avlCalculation.getStabilityDerivatives().getClb());
        rollMomment.setCl_p(avlCalculation.getStabilityDerivatives().getClp());
        rollMomment.setCl_r(avlCalculation.getStabilityDerivatives().getClr());
        rollMomment.setCl_dr(avlCalculation.getStabilityDerivatives().getCld()[rudderPosition]);
        rollMomment.setCl_da(avlCalculation.getStabilityDerivatives().getCld()[aileronPosition]);

        yawMomment.setCn_b(avlCalculation.getStabilityDerivatives().getCnb());
        yawMomment.setCn_p(avlCalculation.getStabilityDerivatives().getCnp());
        yawMomment.setCn_r(avlCalculation.getStabilityDerivatives().getCnr());
        yawMomment.setCn_dr(avlCalculation.getStabilityDerivatives().getCnd()[rudderPosition]);
        yawMomment.setCn_da(avlCalculation.getStabilityDerivatives().getCnd()[aileronPosition]);        
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
