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
public class RollMomment {
    //    <l Cl_b="..." Cl_p="..." Cl_r="..." Cl_dr="..." Cl_da="..." />
    private float Cl_b;
    private float Cl_p;
    private float Cl_r;
    private float Cl_dr;
    private float Cl_da;

    /**
     * @return the Cl_b
     */
    @XmlAttribute(name="Cl_b")
    public float getCl_b() {
        return Cl_b;
    }

    /**
     * @param Cl_b the Cl_b to set
     */
    public void setCl_b(float Cl_b) {
        this.Cl_b = Cl_b;
    }

    /**
     * @return the Cl_p
     */
    @XmlAttribute(name="Cl_p")
    public float getCl_p() {
        return Cl_p;
    }

    /**
     * @param Cl_p the Cl_p to set
     */
    public void setCl_p(float Cl_p) {
        this.Cl_p = Cl_p;
    }

    /**
     * @return the Cl_r
     */
    @XmlAttribute(name="Cl_r")
    public float getCl_r() {
        return Cl_r;
    }

    /**
     * @param Cl_r the Cl_r to set
     */
    public void setCl_r(float Cl_r) {
        this.Cl_r = Cl_r;
    }

    /**
     * @return the Cl_dr
     */
    @XmlAttribute(name="Cl_dr")
    public float getCl_dr() {
        return Cl_dr;
    }

    /**
     * @param Cl_dr the Cl_dr to set
     */
    public void setCl_dr(float Cl_dr) {
        this.Cl_dr = Cl_dr;
    }

    /**
     * @return the Cl_da
     */
    @XmlAttribute(name="Cl_da")
    public float getCl_da() {
        return Cl_da;
    }

    /**
     * @param Cl_da the Cl_da to set
     */
    public void setCl_da(float Cl_da) {
        this.Cl_da = Cl_da;
    }
}
