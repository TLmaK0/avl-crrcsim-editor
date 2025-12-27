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

import com.abajar.avleditor.UnitConversor;
import java.io.NotSerializableException;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 *
 * @author Hugo
 */
public class MetersConversor extends XmlAdapter<Float,Float> {
    protected UnitConversor unitConversor = new UnitConversor();
    protected final MultiUnit multiUnit;

    public MetersConversor(){
        multiUnit = null;
    }

    public MetersConversor(MultiUnit multiUnit){
        this.multiUnit = multiUnit;
    }

    public Float convert(Float quantity) {
        return unitConversor.convertToMeters(quantity, multiUnit.getLengthUnit());
    }

    @Override
    public Float unmarshal(Float v) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Float marshal(Float v) throws Exception {
        return convert(v);
    }

}