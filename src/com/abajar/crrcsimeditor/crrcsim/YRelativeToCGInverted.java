/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.abajar.crrcsimeditor.crrcsim;

/**
 *
 * @author Hugo
 */
public class YRelativeToCGInverted extends XRelativeToCG{
    public YRelativeToCGInverted(){
        super();
    }

    public YRelativeToCGInverted(String unit, Float origin) {
        super(unit, origin);
    }

    @Override
    public Float convert(Float quantity) {
        return -super.convert(quantity);
    }
}
