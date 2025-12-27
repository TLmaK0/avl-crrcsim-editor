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
import javax.xml.bind.annotation.adapters.XmlAdapter;


/**
 *
 * @author Hugo
 */
abstract class UnitConversorTypeAdapter extends XmlAdapter<Float,Float> {
    private final UnitConversor unitConversor = new UnitConversor();


    private final String unit;
    private final Float origin;

    public UnitConversorTypeAdapter(){  //No parameter constructor for JAXB requirement
        unit = null;
        origin = null;
    }

    public UnitConversorTypeAdapter(String unit, Float origin){
        this.unit = unit;
        this.origin = origin;
    }

    public Float convert(Float quantity) {
        return unitConversor.convertToMeters(quantity - origin, unit);
    }

    @Override
    public Float marshal(Float v) throws Exception {
        return convert(v);
    }

}