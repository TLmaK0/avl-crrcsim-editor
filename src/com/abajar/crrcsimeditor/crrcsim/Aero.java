/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.abajar.crrcsimeditor.crrcsim;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author hfreire
 */
@XmlRootElement
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
    private int units = 0;
    private Reference ref = new Reference();  //ref
    private Miscellaneous misc = new Miscellaneous(); //misc
    private PitchMoment pitchMoment = new PitchMoment(); //m
    private Lift lisft = new Lift();
    private Drag drag = new Drag();
    private SideForce sideForce = new SideForce();                                      //Y
    private RollMomment rollMomment = new RollMomment();    //l
    private YawMomment yawMomment = new YawMomment(); //n

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
}
