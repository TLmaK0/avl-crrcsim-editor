/*
 * Copyright (C) 2015  Hugo Freire Gil
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 */
package com.abajar.crrcsimeditor.crrcsim;

/**
 *
 * @author Hugo
 */
public class MultiUnit {
    private String timeUnit;
    private String massUnit;
    private String lengthUnit;

    public MultiUnit(String lengthUnit, String massUnit, String timeUnit) {
        this.lengthUnit = lengthUnit;
        this.massUnit = massUnit;
        this.timeUnit = timeUnit;
    }

    /**
     * @return the timeUnit
     */
    public String getTimeUnit() {
        return timeUnit;
    }

    /**
     * @return the massUnit
     */
    public String getMassUnit() {
        return massUnit;
    }

    /**
     * @return the lengthUnit
     */
    public String getLengthUnit() {
        return lengthUnit;
    }
}