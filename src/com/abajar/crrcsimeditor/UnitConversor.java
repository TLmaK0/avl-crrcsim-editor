/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.abajar.crrcsimeditor;

import java.lang.Exception;

/**
 *
 * @author Hugo
 */
public class UnitConversor {
    public static final float INCHES_TO_METERS = 0.0254f;
    public static final float CENTIMETERS_TO_METERS = 0.01f;

    public float convertToMeters(float quantity, String lengthUnit){
        return quantity * this.getFactor(lengthUnit, false);
    }

    private float getFactor(String lengthUnit, boolean square){
        float factor;
        if (lengthUnit.equals("in")) factor = INCHES_TO_METERS;
        else if (lengthUnit.equals("cm")) factor = CENTIMETERS_TO_METERS;
        else if (lengthUnit.equals("m")) factor = 1f;
        else throw new UnsupportedOperationException("unreconized unity " + lengthUnit + ". Only allowed in, m or cm");
        return square ? factor * factor : factor;
    }

    public float convertToSquareMeters(float quantity, String lengthUnit){
        return quantity * this.getFactor(lengthUnit, true);
    }
}
