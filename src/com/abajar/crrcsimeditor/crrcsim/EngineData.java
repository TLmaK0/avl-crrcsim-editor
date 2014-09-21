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
public class EngineData implements Serializable {

    @Override
    public String toString() {
        return "Data";
    }

    private static final String helpData = "Add 2 engine data at least. Use http://www.ecalc.ch/ to calculate Current, Voltage and rpms (you don't need to touch general settings)."
        + "First row use manufacturer data:"
        + "     Voltage = no-load Current: V"
        + "     Current = no-load Current: A"
        + "     Rpms = KV (w/o torque) * Voltage"
        + "Second row use Motor Optimum Efficiency"
        + "     Current"
        + "     Voltage"
        + "     Rpm"
        + "Third row use Motor Maximum"
        + "     Current"
        + "     Voltage"
        + "     Rpm";

    @CRRCSimEditorField(text="Voltage",
        help=helpData
    )
    private float U_K;

    @CRRCSimEditorField(text="Current",
        help=helpData
    )
    private float I_M;

    @CRRCSimEditorField(text="Rpms",
        help=helpData
    )
    private float rpms;

    public EngineData() {
    }

    /**
     * @return the U_K
     */
    public float getU_K() {
        return U_K;
    }

    /**
     * @param U_K the U_K to set
     */
    public void setU_K(float U_K) {
        this.U_K = U_K;
    }

    /**
     * @return the I_M
     */
    public float getI_M() {
        return I_M;
    }

    /**
     * @param I_M the I_M to set
     */
    public void setI_M(float I_M) {
        this.I_M = I_M;
    }

    /**
     * @return the n
     */
    public float getN() {
        return rpms / 60;
    }


    /**
     * @return the rpms
     */
    public float getRpms() {
        return rpms;
    }

    /**
     * @param rpms the rpms to set
     */
    public void setRpms(float rpms) {
        this.rpms = rpms;
    }


}

