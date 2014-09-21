/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.abajar.crrcsimeditor.crrcsim;

import com.abajar.crrcsimeditor.view.annotations.CRRCSimEditorField;
import java.io.Serializable;

/**
 *
 * @author Hugo
 */
public class Gearing implements Serializable{

    @Override
    public String toString() {
        return "Gearing";
    }

    @CRRCSimEditorField(text="J",
        help="Inertia"
    )
    private float J;

    @CRRCSimEditorField(text="i",
        help="Given omega is the speed of the shaft, i*omega is the speed of the device which is connected to the shaft using this gearing. "
    )
    private float i=1;
    public Gearing() {
    }

    /**
     * @return the J
     */
    public float getJ() {
        return J;
    }

    /**
     * @param J the J to set
     */
    public void setJ(float J) {
        this.J = J;
    }

    /**
     * @return the i
     */
    public float getI() {
        return i;
    }

    /**
     * @param i the i to set
     */
    public void setI(float i) {
        this.i = i;
    }
}
