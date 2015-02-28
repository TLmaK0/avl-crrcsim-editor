/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.abajar.crrcsimeditor.crrcsim;

import java.io.NotSerializableException;

/**
 *
 * @author Hugo
 */
public class MetersConversorInverted extends MetersConversor{

    public MetersConversorInverted(){
        super();
    }

    public MetersConversorInverted(MultiUnit multiUnit) {
        super(multiUnit);
    }

    @Override
    public Float convert(Float quantity) {
        return -super.convert(quantity);
    }
}
