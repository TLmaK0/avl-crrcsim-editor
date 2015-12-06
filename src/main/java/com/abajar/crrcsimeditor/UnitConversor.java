/*
 * Copyright (C) 2015  Hugo Freire Gil
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
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
    public static final float OUNCES_TO_KILOGRAMS = 0.0283495231f;
    public static final float GRAMS_TO_KILOGRAMS = 0.001f;
    public static final float HOURS_TO_SECONDS = 3600f;
    public static final float MINUTES_TO_SECONDS = 36f;

    public float convertToMeters(float quantity, String lengthUnit){
        return quantity * this.getFactorLength(lengthUnit, false);
    }

    private float getFactorLength(String lengthUnit, boolean square){
        float factor;
        if (lengthUnit.equals("in")) factor = INCHES_TO_METERS;
        else if (lengthUnit.equals("cm")) factor = CENTIMETERS_TO_METERS;
        else if (lengthUnit.equals("m")) factor = 1f;
        else throw new UnsupportedOperationException("unreconized unity " + lengthUnit + ". Only allowed in, m or cm");
        return square ? factor * factor : factor;
    }

    public float convertToSquareMeters(float quantity, String lengthUnit){
        return quantity * this.getFactorLength(lengthUnit, true);
    }

    public float convertToKilograms(float quantity, String massUnit){
        return quantity * this.getFactorMass(massUnit);
    }

    public float convertToKilogramsSquareMeters(float quantity, String massUnit, String lengthUnit){
        return quantity * this.getFactorMass(massUnit) * this.getFactorLength(lengthUnit, true);
    }

    private float getFactorMass(String massUnit) {
        float factor;
        if (massUnit.equals("oz")) factor = OUNCES_TO_KILOGRAMS;
        else if (massUnit.equals("g")) factor = GRAMS_TO_KILOGRAMS;
        else if (massUnit.equals("kg")) factor = 1f;
        else throw new UnsupportedOperationException("unreconized unity " + massUnit + ". Only allowed g, kg or oz");
        return factor;
    }

    public float convertToKilogramsMeters(float quantity, String massUnit, String lengthUnit) {
        return quantity * this.getFactorMass(massUnit) * this.getFactorLength(lengthUnit, false);
    }

    public float convertToSeconds(float quantity, String timeUnit){
        float factor;
        if (timeUnit.equals("h")) factor = HOURS_TO_SECONDS;
        else if (timeUnit.equals("m")) factor = MINUTES_TO_SECONDS;
        else if (timeUnit.equals("s")) factor = 1f;
        else throw new UnsupportedOperationException("unreconized unity " + timeUnit + ". Only allowed s, m or h");
        return factor;
    }
    public float convertToMetersPerSecond(float velocity, String lengthUnit, String timeUnit) {
        return convertToSeconds(convertToMeters(velocity, lengthUnit), timeUnit);
    }
}