/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.abajar.crrcsimeditor.crrcsim;

import com.abajar.crrcsimeditor.UnitConversor;
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
