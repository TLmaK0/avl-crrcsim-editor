/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.abajar.crrcsimeditor.avl.mass;

import com.abajar.crrcsimeditor.avl.mass.Mass;
import com.abajar.crrcsimeditor.view.annotations.CRRCSimEditor;
import com.abajar.crrcsimeditor.view.avl.SelectorMutableTreeNode.ENABLE_BUTTONS;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Locale;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author hfreire
 */
public class MassObject implements Serializable{
    protected static final Locale locale = Mass.locale;
    private final ArrayList<Mass> masses = new ArrayList<Mass>();

    /**
     * @return the masses
     */
    @XmlElementWrapper
    @XmlElement(name="mass")
    public ArrayList<Mass> getMasses() {
        return masses;
    }

    public void writeAVLMassData(OutputStream out) {
        for(Mass mass : this.getMasses()){
            mass.writeAVLMassData(out);
        }
    }

    public Mass createMass() {
        Mass mass = new Mass();
        this.getMasses().add(mass);
        return mass;
    }

}
