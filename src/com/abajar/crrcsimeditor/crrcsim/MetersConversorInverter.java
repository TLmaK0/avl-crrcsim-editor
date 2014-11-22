/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.abajar.crrcsimeditor.crrcsim;

/**
 *
 * @author Hugo
 */
public class MetersConversorInverter extends MetersConversor{
    public MetersConversorInverter(){
        super();
    }

    public MetersConversorInverter(MultiUnit multiUnit) {
        super(multiUnit);
    }

    @Override
    public Float convert(Float quantity) {
        return -super.convert(quantity);
    }
}
