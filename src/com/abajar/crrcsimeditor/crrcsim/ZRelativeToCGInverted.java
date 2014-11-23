/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.abajar.crrcsimeditor.crrcsim;

/**
 *
 * @author Hugo
 */
public class ZRelativeToCGInverted extends XRelativeToCG{
    public ZRelativeToCGInverted(){
        super();
    }

    public ZRelativeToCGInverted(String unit, Float origin) {
        super(unit, origin);
    }

    @Override
    public Float convert(Float quantity) {
        return -super.convert(quantity);
    }
}
