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
@XmlRootElement(name ="Y")
class SideForce {
    //    <Y CY_b="..." CY_p="..." CY_r="..." CY_dr="..." CY_da="..." />

    private float CY_b;
    private float CY_p;
    private float CY_r;
    private float CY_dr;
    private float CY_da;

    /**
     * @return the CY_b
     */
    @XmlAttribute
    public float getCY_b() {
        return CY_b;
    }

    /**
     * @param CY_b the CY_b to set
     */
    public void setCY_b(float CY_b) {
        this.CY_b = CY_b;
    }

    /**
     * @return the CY_p
     */
    @XmlAttribute
    public float getCY_p() {
        return CY_p;
    }

    /**
     * @param CY_p the CY_p to set
     */
    public void setCY_p(float CY_p) {
        this.CY_p = CY_p;
    }

    /**
     * @return the CY_r
     */
    @XmlAttribute
    public float getCY_r() {
        return CY_r;
    }

    /**
     * @param CY_r the CY_r to set
     */
    public void setCY_r(float CY_r) {
        this.CY_r = CY_r;
    }

    /**
     * @return the CY_dr
     */
    @XmlAttribute
    public float getCY_dr() {
        return CY_dr;
    }

    /**
     * @param CY_dr the CY_dr to set
     */
    public void setCY_dr(float CY_dr) {
        this.CY_dr = CY_dr;
    }

    /**
     * @return the CY_da
     */
    @XmlAttribute
    public float getCY_da() {
        return CY_da;
    }

    /**
     * @param CY_da the CY_da to set
     */
    public void setCY_da(float CY_da) {
        this.CY_da = CY_da;
    }
}
