/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.abajar.crrcsimeditor.crrcsim;

import com.abajar.crrcsimeditor.UnitConversor;

/**
 *
 * @author Hugo
 */
public class MetersConversor extends UnitConversorTypeAdapter{
    private UnitConversor unitConversor = new UnitConversor();

    public MetersConversor(){
        super();
    }
    
    public MetersConversor(MultiUnit multiUnit){
        super(multiUnit);
    }

    @Override
    public Float convert(Float quantity) {
        return unitConversor.convertToMeters(quantity, multiUnit.getLengthUnit());
    }

    @Override
    public Float unmarshal(Float v) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
