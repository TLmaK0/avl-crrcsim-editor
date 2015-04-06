/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.abajar.crrcsimeditor.crrcsim;

import com.abajar.crrcsimeditor.view.annotations.CRRCSimEditor;
import com.abajar.crrcsimeditor.view.annotations.CRRCSimEditorField;
import com.abajar.crrcsimeditor.view.annotations.CRRCSimEditorNode;
import com.abajar.crrcsimeditor.view.avl.SelectorMutableTreeNode.ENABLE_BUTTONS;
import java.io.Serializable;
import java.util.ArrayList;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author Hugo
 */
@CRRCSimEditor(buttons={ENABLE_BUTTONS.ADD_ENGINE, ENABLE_BUTTONS.ADD_SYMPLE_TRUST})
public class Shaft implements Serializable {
    static final long serialVersionUID = -4669977187731929600L;
    @Override
    public String toString() {
        return "Shaft";
    }

    @CRRCSimEditorField(text="J",
        help="Inertia in kg m^2"
    )
    private float J;

    @CRRCSimEditorField(text="brake",
        help="if brake is not zero, this shaft will stop rotating as soon as the throttle command is zero. This is needed for folding props."
    )
    private int brake;

    private ArrayList<Engine> engines = new ArrayList<Engine>();
    private ArrayList<Propeller> propellers = new ArrayList<Propeller>();
    private ArrayList<SimpleTrust> simpleTrusts = new ArrayList<SimpleTrust>();


    public Shaft() {
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
     * @return the brake
     */
    @XmlAttribute
    public int getBrake() {
        return brake;
    }

    /**
     * @param brake the brake to set
     */
    public void setBrake(int brake) {
        this.brake = brake;
    }

    /**
     * @return the engines
     */
    @CRRCSimEditorNode(name="Engines")
    @XmlElement(name="engine_dcm")
    public ArrayList<Engine> getEngines() {
        return engines;
    }

    /**
     * @param engines the engines to set
     */
    public void setEngines(ArrayList<Engine> engines) {
        this.engines = engines;
    }

    public Engine createEngine() {
        Engine engine = new Engine();
        this.getEngines().add(engine);
        return engine;
    }

    /**
     * @return the propellers
     */
    @CRRCSimEditorNode(name="Propellers")
    @XmlElement(name="propeller")
    public ArrayList<Propeller> getPropellers() {
        return propellers;
    }

    /**
     * @param propellers the propellers to set
     */
    public void setPropellers(ArrayList<Propeller> propellers) {
        this.propellers = propellers;
    }

    /**
     * @return the simpleTrusts
     */
    @CRRCSimEditorNode(name="Simple Trusts")
    @XmlElement(name="simpletrust")
    public ArrayList<SimpleTrust> getSimpleTrusts() {
        return simpleTrusts;
    }

    /**
     * @param simpleTrusts the simpleTrusts to set
     */
    public void setSimpleTrusts(ArrayList<SimpleTrust> simpleTrusts) {
        this.simpleTrusts = simpleTrusts;
    }

    public SimpleTrust createSimpleTrust() {
        SimpleTrust trust = new SimpleTrust();
        this.getSimpleTrusts().add(trust);
        return trust;
    }
}
