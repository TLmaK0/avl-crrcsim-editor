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
public class ZRelativeToCG extends UnitConversorTypeAdapter{
    private UnitConversor unitConversor = new UnitConversor();

    public ZRelativeToCG(){
        super();
    }
    
    public ZRelativeToCG(String unit, Float origin){
        super(unit, origin);
    }

    @Override
    public Float unmarshal(Float v) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
