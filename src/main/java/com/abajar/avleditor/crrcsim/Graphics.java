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

import com.abajar.avleditor.view.annotations.AvlEditorField;
import com.abajar.avleditor.view.annotations.AvlEditorFileField;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlAttribute;
import org.eclipse.persistence.oxm.annotations.XmlPath;

/**
 *
 * @author Hugo
 */
public class Graphics implements Serializable {
    private String version="1";

    @AvlEditorFileField(text="model",
        help="name of the graphics file",
        extensions={"ac"},
        extensionDescription="AC3D files"
    )
    private String model="Crossfire.ac";

    @AvlEditorField(text="descr_long",
        help="A long description"
    )
    private String descr_long="Default description";

    @AvlEditorField(text="descr_short",
        help="A short description"
    )
    private String descr_short="default";

    @AvlEditorField(text="scale",
        help="Scale factor (e.g. 10 means 1:10, displayed size = model size / 10)"
    )
    private float scale = 1.0f;

    @AvlEditorField(text="avlXAxis",
        help="Model axis for AVL X (forward): X, Y, Z, -X, -Y, or -Z"
    )
    private String avlXAxis = "X";

    @AvlEditorField(text="avlYAxis",
        help="Model axis for AVL Y (spanwise): X, Y, Z, -X, -Y, or -Z"
    )
    private String avlYAxis = "-Z";

    @AvlEditorField(text="avlZAxis",
        help="Model axis for AVL Z (up): X, Y, Z, -X, -Y, or -Z"
    )
    private String avlZAxis = "Y";

    @AvlEditorField(text="showReferenceLine",
        help="Show horizontal plane reference line in 3D viewer"
    )
    private boolean showReferenceLine = true;

    @Override
    public String toString() {
        return "Graphics";
    }
    /**
     * @return the version
     */
    @XmlAttribute
    public String getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * @return the model
     */
    @XmlAttribute
    public String getModel() {
        return model;
    }

    /**
     * @param model the model to set
     */
    public void setModel(String model) {
        this.model = model;
    }

    /**
     * @return the descr_long
     */
    @XmlPath(value="descr_long/en/text()")
    public String getDescr_long() {
        return descr_long;
    }

    /**
     * @param descr_long the descr_long to set
     */
    public void setDescr_long(String descr_long) {
        this.descr_long = descr_long;
    }

    /**
     * @return the descr_short
     */
    @XmlPath(value="descr_short/en/text()")
    public String getDescr_short() {
        return descr_short;
    }

    /**
     * @param descr_short the descr_short to set
     */
    public void setDescr_short(String descr_short) {
        this.descr_short = descr_short;
    }

    /**
     * @return the scale factor
     */
    public float getScale() {
        return scale;
    }

    /**
     * @param scale the scale factor to set
     */
    public void setScale(float scale) {
        this.scale = scale;
    }

    // Helper to get base axis (without sign)
    private String getBaseAxis(String axis) {
        return axis.replace("-", "").toUpperCase();
    }

    /**
     * @return the AVL X axis mapping
     */
    public String getAvlXAxis() {
        return avlXAxis;
    }

    /**
     * @param newValue the AVL X axis mapping to set
     */
    public void setAvlXAxis(String newValue) {
        String oldValue = this.avlXAxis;
        String newBase = getBaseAxis(newValue);
        // Swap if another axis has the same base value
        if (getBaseAxis(avlYAxis).equals(newBase)) {
            this.avlYAxis = oldValue;
        } else if (getBaseAxis(avlZAxis).equals(newBase)) {
            this.avlZAxis = oldValue;
        }
        this.avlXAxis = newValue;
    }

    /**
     * @return the AVL Y axis mapping
     */
    public String getAvlYAxis() {
        return avlYAxis;
    }

    /**
     * @param newValue the AVL Y axis mapping to set
     */
    public void setAvlYAxis(String newValue) {
        String oldValue = this.avlYAxis;
        String newBase = getBaseAxis(newValue);
        // Swap if another axis has the same base value
        if (getBaseAxis(avlXAxis).equals(newBase)) {
            this.avlXAxis = oldValue;
        } else if (getBaseAxis(avlZAxis).equals(newBase)) {
            this.avlZAxis = oldValue;
        }
        this.avlYAxis = newValue;
    }

    /**
     * @return the AVL Z axis mapping
     */
    public String getAvlZAxis() {
        return avlZAxis;
    }

    /**
     * @param newValue the AVL Z axis mapping to set
     */
    public void setAvlZAxis(String newValue) {
        String oldValue = this.avlZAxis;
        String newBase = getBaseAxis(newValue);
        // Swap if another axis has the same base value
        if (getBaseAxis(avlXAxis).equals(newBase)) {
            this.avlXAxis = oldValue;
        } else if (getBaseAxis(avlYAxis).equals(newBase)) {
            this.avlYAxis = oldValue;
        }
        this.avlZAxis = newValue;
    }

    /**
     * @return whether to show reference line
     */
    public boolean getShowReferenceLine() {
        return showReferenceLine;
    }

    /**
     * @param showReferenceLine whether to show reference line
     */
    public void setShowReferenceLine(boolean showReferenceLine) {
        this.showReferenceLine = showReferenceLine;
    }
}