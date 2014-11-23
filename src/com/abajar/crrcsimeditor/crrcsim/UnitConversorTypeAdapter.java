/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.abajar.crrcsimeditor.crrcsim;

import com.abajar.crrcsimeditor.UnitConversor;
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
