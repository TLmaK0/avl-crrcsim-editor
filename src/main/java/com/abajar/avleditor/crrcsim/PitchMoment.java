/*
 * Copyright (C) 2015  Hugo Freire Gil
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 */

package com.abajar.avleditor.crrcsim;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author hfreire
 */
public class PitchMoment {
    private float Cm_0;
    private float Cm_a;
    private float Cm_q;
    private float Cm_de;

    /**
     * @return the Cm_0
     */
    @XmlAttribute(name="Cm_0")
    public float getCm_0() {
        return Cm_0;
    }

    /**
     * @param Cm_0 the Cm_0 to set
     */
    public void setCm_0(float Cm_0) {
        this.Cm_0 = Cm_0;
    }

    /**
     * @return the Cm_a
     */
    @XmlAttribute(name="Cm_a")
    public float getCm_a() {
        return Cm_a;
    }

    /**
     * @param Cm_a the Cm_a to set
     */
    public void setCm_a(float Cm_a) {
        this.Cm_a = Cm_a;
    }

    /**
     * @return the Cm_q
     */
    @XmlAttribute(name="Cm_q")
    public float getCm_q() {
        return Cm_q;
    }

    /**
     * @param Cm_q the Cm_q to set
     */
    public void setCm_q(float Cm_q) {
        this.Cm_q = Cm_q;
    }

    /**
     * @return the Cm_de
     */
    @XmlAttribute(name="Cm_de")
    public float getCm_de() {
        return Cm_de;
    }

    /**
     * @param Cm_de the Cm_de to set
     */
    public void setCm_de(float Cm_de) {
        this.Cm_de = Cm_de;
    }
}