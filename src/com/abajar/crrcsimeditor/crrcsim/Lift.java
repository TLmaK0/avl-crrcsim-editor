/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.abajar.crrcsimeditor.crrcsim;

import javax.xml.bind.annotation.XmlAttribute;

/**
 *
 * @author hfreire
 */
public class Lift {
    //    <lift CL_0="..." CL_max="..." CL_min="..." CL_a="..." CL_q="..."
//          CL_de="..." CL_drop="..." CL_CD0="..." />
    private float CL_0;
    private float CL_max;
    private float CL_min;
    private float CL_a;
    private float CL_q;
    private float CL_de;
    private float CL_drop;
    private float CL_CD0;

    /**
     * @return the CL_0
     */
    @XmlAttribute
    public float getCL_0() {
        return CL_0;
    }

    /**
     * @param CL_0 the CL_0 to set
     */
    public void setCL_0(float CL_0) {
        this.CL_0 = CL_0;
    }

    /**
     * @return the CL_max
     */
    @XmlAttribute
    public float getCL_max() {
        return CL_max;
    }

    /**
     * @param CL_max the CL_max to set
     */
    public void setCL_max(float CL_max) {
        this.CL_max = CL_max;
    }

    /**
     * @return the CL_min
     */
    @XmlAttribute
    public float getCL_min() {
        return CL_min;
    }

    /**
     * @param CL_min the CL_min to set
     */
    public void setCL_min(float CL_min) {
        this.CL_min = CL_min;
    }

    /**
     * @return the CL_a
     */
    @XmlAttribute
    public float getCL_a() {
        return CL_a;
    }

    /**
     * @param CL_a the CL_a to set
     */
    public void setCL_a(float CL_a) {
        this.CL_a = CL_a;
    }

    /**
     * @return the CL_q
     */
    @XmlAttribute
    public float getCL_q() {
        return CL_q;
    }

    /**
     * @param CL_q the CL_q to set
     */
    public void setCL_q(float CL_q) {
        this.CL_q = CL_q;
    }

    /**
     * @return the CL_de
     */
    @XmlAttribute
    public float getCL_de() {
        return CL_de;
    }

    /**
     * @param CL_de the CL_de to set
     */
    public void setCL_de(float CL_de) {
        this.CL_de = CL_de;
    }

    /**
     * @return the CL_drop
     */
    @XmlAttribute
    public float getCL_drop() {
        return CL_drop;
    }

    /**
     * @param CL_drop the CL_drop to set
     */
    public void setCL_drop(float CL_drop) {
        this.CL_drop = CL_drop;
    }

    /**
     * @return the CL_CD0
     */
    @XmlAttribute
    public float getCL_CD0() {
        return CL_CD0;
    }

    /**
     * @param CL_CD0 the CL_CD0 to set
     */
    public void setCL_CD0(float CL_CD0) {
        this.CL_CD0 = CL_CD0;
    }
}
