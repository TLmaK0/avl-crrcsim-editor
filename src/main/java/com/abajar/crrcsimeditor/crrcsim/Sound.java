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

import com.abajar.crrcsimeditor.view.annotations.CRRCSimEditorNode;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author Hugo
 */
public class Sound implements Serializable {
    private Sample sample = new Sample();

    @Override
    public String toString(){
        return "Sound";
    }

    public Sound() {
    }

    /**
     * @return the sample
     */
    @CRRCSimEditorNode
    @XmlElement
    public Sample getSample() {
        return sample;
    }
}