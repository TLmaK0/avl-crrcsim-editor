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

import javax.xml.bind.annotation.XmlAttribute;

/**
 *
 * @author hfreire
 */
public class Miscellaneous {
    private float Alpha_0;
    private float eta_loc;
    private float CG_arm;
    private float span_eff;

    /**
     * @return the Alpha_0
     */
    @XmlAttribute(name="Alpha_0")
    public float getAlpha_0() {
        return Alpha_0;
    }

    /**
     * @param Alpha_0 the Alpha_0 to set
     */
    public void setAlpha_0(float Alpha_0) {
        this.Alpha_0 = Alpha_0;
    }

    /**
     * @return the eta_loc
     */
    @XmlAttribute(name="eta_loc")
    public float getEta_loc() {
        return eta_loc;
    }

    /**
     * @param eta_loc the eta_loc to set
     */
    public void setEta_loc(float eta_loc) {
        this.eta_loc = eta_loc;
    }

    /**
     * @return the CG_arm
     */
    @XmlAttribute(name="CG_arm")
    public float getCG_arm() {
        return CG_arm;
    }

    /**
     * @param CG_arm the CG_arm to set
     */
    public void setCG_arm(float CG_arm) {
        this.CG_arm = CG_arm;
    }

    /**
     * @return the span_eff
     */
    @XmlAttribute(name="span_eff")
    public float getSpan_eff() {
        return span_eff;
    }

    /**
     * @param span_eff the span_eff to set
     */
    public void setSpan_eff(float span_eff) {
        this.span_eff = span_eff;
    }
}