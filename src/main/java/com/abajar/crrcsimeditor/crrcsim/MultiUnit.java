/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
