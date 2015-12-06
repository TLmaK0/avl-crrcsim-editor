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
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 *
 * @author Hugo
 */
public class Pos implements Serializable{

    @Override
    public String toString() {
        return "Position";
    }

    @CRRCSimEditorField(text="X",
        help="X position\r\n"
    )
    private float x;

    @CRRCSimEditorField(text="Y",
        help="Y position\r\n"
    )
    private float y;

    @CRRCSimEditorField(text="Z",
        help="Z position\r\n"
    )
    private float z;

    /**
     * @return the x
     */
    @XmlAttribute
    @XmlJavaTypeAdapter(XRelativeToCGInverted.class)
    public float getX() {
        return x;
    }

    /**
     * @param x the x to set
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     * @return the y
     */
    @XmlAttribute
    @XmlJavaTypeAdapter(YRelativeToCG.class)
    public float getY() {
        return y;
    }

    /**
     * @param y the y to set
     */
    public void setY(float y) {
        this.y = y;
    }

    /**
     * @return the z
     */
    @XmlAttribute
    @XmlJavaTypeAdapter(ZRelativeToCGInverted.class)
    public float getZ() {
        return z;
    }

    /**
     * @param z the z to set
     */
    public void setZ(float z) {
        this.z = z;
    }
}