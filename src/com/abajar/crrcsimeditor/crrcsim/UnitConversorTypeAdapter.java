/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.abajar.crrcsimeditor.crrcsim;

import javax.xml.bind.annotation.adapters.XmlAdapter;


/**
 *
 * @author Hugo
 */
abstract class UnitConversorTypeAdapter extends XmlAdapter<Float,Float> {
    protected final MultiUnit multiUnit;

    public UnitConversorTypeAdapter(){  //No parameter constructor for JAXB requirement
        this.multiUnit = new MultiUnit(null, null, null);
    }

    public UnitConversorTypeAdapter(MultiUnit multiUnit) {
        this.multiUnit = multiUnit;
    }

    abstract public Float convert(Float quantity);

    @Override
    public Float marshal(Float v) throws Exception {
        return convert(v);
    }

}
