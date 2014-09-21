/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.abajar.crrcsimeditor.crrcsim;

import com.abajar.crrcsimeditor.view.annotations.CRRCSimEditorField;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlAttribute;

/**
 *
 * @author Hugo
 */
public class Propeller implements Serializable {

    @Override
    public String toString() {
        return "Propeller";
    }

    @CRRCSimEditorField(text="D",
        help="meters"
    )
    private float D;

    @CRRCSimEditorField(text="H",
        help="meters"
    )
    private float H;

    @CRRCSimEditorField(text="J",
        help="Inertia"
    )
    private float J;

    @CRRCSimEditorField(text="n_fold",
        help="The Propeller can be configured to be a folding prop, which folds as soon as it rotates slower than omega_fold."
        + " From the xml config, n_fold is read and converted using (omega_fold = n_fold * 2 * pi)"
    )
    private int n_fold;

    public Propeller() {
    }

    /**
     * @return the D
     */
    @XmlAttribute
    public float getD() {
        return D;
    }

    /**
     * @param D the D to set
     */
    public void setD(float D) {
        this.D = D;
    }

    /**
     * @return the H
     */
    @XmlAttribute
    public float getH() {
        return H;
    }

    /**
     * @param H the H to set
     */
    public void setH(float H) {
        this.H = H;
    }

    /**
     * @return the J
     */
    @XmlAttribute
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
     * @return the n_fold
     */
    @XmlAttribute(name="n_fold")
    public int getN_fold() {
        return n_fold;
    }

    /**
     * @param n_fold the n_fold to set
     */
    public void setN_fold(int n_fold) {
        this.n_fold = n_fold;
    }
}

