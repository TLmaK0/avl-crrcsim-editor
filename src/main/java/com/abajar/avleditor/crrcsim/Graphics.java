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
}