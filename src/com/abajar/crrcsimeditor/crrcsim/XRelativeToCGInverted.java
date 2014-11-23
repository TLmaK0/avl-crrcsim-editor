/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.abajar.crrcsimeditor.crrcsim;

/**
 *
 * @author Hugo
 */
public class XRelativeToCGInverted extends XRelativeToCG{
    public XRelativeToCGInverted(){
        super();
    }

    public XRelativeToCGInverted(String unit, Float origin) {
        super(unit, origin);
    }

    @Override
    public Float convert(Float quantity) {
        return -super.convert(quantity);
    }
}
