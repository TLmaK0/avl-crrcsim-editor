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

/**
 *
 * @author hfreire
 */
public class Reference {
    private float chord;
    private float span;
    private float area;
    private float speed;

    /**
     * @return the chord
     */
    @XmlAttribute
    public float getChord() {
        return chord;
    }

    /**
     * @param chord the chord to set
     */
    public void setChord(float chord) {
        this.chord = chord;
    }

    /**
     * @return the span
     */
    @XmlAttribute
    public float getSpan() {
        return span;
    }

    /**
     * @param span the span to set
     */
    public void setSpan(float span) {
        this.span = span;
    }

    /**
     * @return the area
     */
    @XmlAttribute
    public float getArea() {
        return area;
    }

    /**
     * @param area the area to set
     */
    public void setArea(float area) {
        this.area = area;
    }

    /**
     * @return the speed
     */
    @XmlAttribute
    public float getSpeed() {
        return speed;
    }

    /**
     * @param speed the speed to set
     */
    public void setSpeed(float speed) {
        this.speed = speed;
    }
}