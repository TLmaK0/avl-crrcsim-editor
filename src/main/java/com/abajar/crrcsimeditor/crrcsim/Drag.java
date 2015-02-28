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
public class Drag {
    private float CD_prof;
    private float Uexp_CD;
    private float CD_stall;
    private float CD_CLsq;
    private float CD_AIsq;
    private float CD_ELsq;

    /**
     * @return the CD_prof
     */
    @XmlAttribute(name="CD_prof")
    public float getCD_prof() {
        return CD_prof;
    }

    /**
     * @param CD_prof the CD_prof to set
     */
    public void setCD_prof(float CD_prof) {
        this.CD_prof = CD_prof;
    }

    /**
     * @return the Uexp_CD
     */
    @XmlAttribute(name="Uexp_CD")
    public float getUexp_CD() {
        return Uexp_CD;
    }

    /**
     * @param Uexp_CD the Uexp_CD to set
     */
    public void setUexp_CD(float Uexp_CD) {
        this.Uexp_CD = Uexp_CD;
    }

    /**
     * @return the CD_stall
     */
    @XmlAttribute(name="CD_stall")
    public float getCD_stall() {
        return CD_stall;
    }

    /**
     * @param CD_stall the CD_stall to set
     */
    public void setCD_stall(float CD_stall) {
        this.CD_stall = CD_stall;
    }

    /**
     * @return the CD_CLsq
     */
    @XmlAttribute(name="CD_CLsq")
    public float getCD_CLsq() {
        return CD_CLsq;
    }

    /**
     * @param CD_CLsq the CD_CLsq to set
     */
    public void setCD_CLsq(float CD_CLsq) {
        this.CD_CLsq = CD_CLsq;
    }

    /**
     * @return the CD_AIsq
     */
    @XmlAttribute(name="CD_AIsq")
    public float getCD_AIsq() {
        return CD_AIsq;
    }

    /**
     * @param CD_AIsq the CD_AIsq to set
     */
    public void setCD_AIsq(float CD_AIsq) {
        this.CD_AIsq = CD_AIsq;
    }

    /**
     * @return the CD_ELsq
     */
    @XmlAttribute(name="CD_ELsq")
    public float getCD_ELsq() {
        return CD_ELsq;
    }

    /**
     * @param CD_ELsq the CD_ELsq to set
     */
    public void setCD_ELsq(float CD_ELsq) {
        this.CD_ELsq = CD_ELsq;
    }
}
