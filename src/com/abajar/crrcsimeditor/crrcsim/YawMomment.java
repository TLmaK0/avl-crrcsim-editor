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
@XmlRootElement(name = "n")
class YawMomment {
    private float Cn_b;
    private float Cn_p;
    private float Cn_r;
    private float Cn_dr;
    private float Cn_da;

    /**
     * @return the Cn_b
     */
    @XmlAttribute
    public float getCn_b() {
        return Cn_b;
    }

    /**
     * @param Cn_b the Cn_b to set
     */
    public void setCn_b(float Cn_b) {
        this.Cn_b = Cn_b;
    }

    /**
     * @return the Cn_p
     */
    @XmlAttribute
    public float getCn_p() {
        return Cn_p;
    }

    /**
     * @param Cn_p the Cn_p to set
     */
    public void setCn_p(float Cn_p) {
        this.Cn_p = Cn_p;
    }

    /**
     * @return the Cn_r
     */
    @XmlAttribute
    public float getCn_r() {
        return Cn_r;
    }

    /**
     * @param Cn_r the Cn_r to set
     */
    public void setCn_r(float Cn_r) {
        this.Cn_r = Cn_r;
    }

    /**
     * @return the Cn_dr
     */
    @XmlAttribute
    public float getCn_dr() {
        return Cn_dr;
    }

    /**
     * @param Cn_dr the Cn_dr to set
     */
    public void setCn_dr(float Cn_dr) {
        this.Cn_dr = Cn_dr;
    }

    /**
     * @return the Cn_da
     */
    @XmlAttribute
    public float getCn_da() {
        return Cn_da;
    }

    /**
     * @param Cn_da the Cn_da to set
     */
    public void setCn_da(float Cn_da) {
        this.Cn_da = Cn_da;
    }
}
