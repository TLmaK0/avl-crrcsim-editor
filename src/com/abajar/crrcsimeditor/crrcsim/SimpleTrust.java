/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.abajar.crrcsimeditor.crrcsim;

import com.abajar.crrcsimeditor.view.annotations.CRRCSimEditorField;
import com.abajar.crrcsimeditor.view.annotations.CRRCSimEditorNode;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author Hugo
 */
public class SimpleTrust implements Serializable{

    @Override
    public String toString() {
        return "SimpleTrust";
    }

    @CRRCSimEditorField(text="k_F",
        help="Trust = k_F * omega_p\n"
        + "omega_p = i * omega\n"
        + "omega is the speed of the shaft\n"
        + "i * omega is the speed of the device connected to the shaft using the gearing"
    )
    private float k_F;

    @CRRCSimEditorField(text="k_M",
        help="Torque = -1 * k_M * omega_p * i\n"
        + "omega_p = i * omega\n"
        + "omega is the speed of the shaft\n"
        + "i * omega is the speed of the device connected to the shaft using the gearing"
    )
    private float k_M;

    private Gearing gearing = new Gearing();
    
    /**
     * @return the k_F
     */
    public float getK_F() {
        return k_F;
    }

    /**
     * @param k_F the k_F to set
     */
    public void setK_F(float k_F) {
        this.k_F = k_F;
    }

    /**
     * @return the k_M
     */
    public float getK_M() {
        return k_M;
    }

    /**
     * @param k_M the k_M to set
     */
    public void setK_M(float k_M) {
        this.k_M = k_M;
    }

    /**
     * @return the gearing
     */
    @CRRCSimEditorNode
    @XmlElement
    public Gearing getGearing() {
        return gearing;
    }

    /**
     * @param gearing the gearing to set
     */
    public void setGearing(Gearing gearing) {
        this.gearing = gearing;
    }


}
