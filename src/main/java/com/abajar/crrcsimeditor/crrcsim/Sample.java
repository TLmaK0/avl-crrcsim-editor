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

import com.abajar.crrcsimeditor.view.annotations.CRRCSimEditorField;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlAttribute;

/**
 *
 * @author Hugo
 */
public class Sample implements Serializable {

    @Override
    public String toString() {
        return this.filename;
    }

    @CRRCSimEditorField(text="sound file",
        help="name of file for engine sound"
    )
    private String filename = "sound.wav";

    @CRRCSimEditorField(text="type",
        help="Type of sound: 0 glow engine, 1 electric engine, 2 glider sound"
    )
    private String type="0";

    @CRRCSimEditorField(text="pitchfactor",
        help="This number converts from speed of propeller to pitch of engine sound."
    )
    private float pitchfactor;

    @CRRCSimEditorField(text="maxvolume",
        help="The maximum sample volume (0.0 ... 1.0). The loudest sample should be set to 1.0."
    )
    private float maxvolume;

    @CRRCSimEditorField(text="v_min",
        help="Only for type=2: minimal velocity (relative to the airplane's \"neutral\" velocity) at which the sound can be heard."
    )
    private int v_min;

    @CRRCSimEditorField(text="v_max",
        help="Only for type=2: velocity (relative to the airplane's \"neutral\" velocity) at which the sound reaches maximum volume."
    )
    private int v_max;


    @CRRCSimEditorField(text="dist_max",
        help="Only for type=2: distance at which the sound reaches the minimum volume."
    )
    private int dist_max;

    public Sample() {
    }

    /**
     * @return the filename
     */
    public String getFilename() {
        return filename;
    }

    /**
     * @param filename the filename to set
     */
    @XmlAttribute
    public void setFilename(String filename) {
        this.filename = filename;
    }

    /**
     * @return the type
     */
    @XmlAttribute
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the pitchfactor
     */
    @XmlAttribute
    public float getPitchfactor() {
        return pitchfactor;
    }

    /**
     * @param pitchfactor the pitchfactor to set
     */
    public void setPitchfactor(float pitchfactor) {
        this.pitchfactor = pitchfactor;
    }

    /**
     * @return the maxvolume
     */
    @XmlAttribute
    public float getMaxvolume() {
        return maxvolume;
    }

    /**
     * @param maxvolume the maxvolume to set
     */
    public void setMaxvolume(float maxvolume) {
        this.maxvolume = maxvolume;
    }

    /**
     * @return the v_min
     */
    @XmlAttribute(name="v_min")
    public int getV_min() {
        return v_min;
    }

    /**
     * @param v_min the v_min to set
     */
    public void setV_min(int v_min) {
        this.v_min = v_min;
    }

    /**
     * @return the v_max
     */
    @XmlAttribute(name="v_max")
    public int getV_max() {
        return v_max;
    }

    /**
     * @param v_max the v_max to set
     */
    public void setV_max(int v_max) {
        this.v_max = v_max;
    }

    /**
     * @return the dist_max
     */
    @XmlAttribute(name="dist_max")
    public int getDist_max() {
        return dist_max;
    }

    /**
     * @param dist_max the dist_max to set
     */
    public void setDist_max(int dist_max) {
        this.dist_max = dist_max;
    }
}