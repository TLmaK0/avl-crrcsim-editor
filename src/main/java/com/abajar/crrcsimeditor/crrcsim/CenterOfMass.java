/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.abajar.crrcsimeditor.crrcsim;

import com.abajar.crrcsimeditor.avl.AVL;
import com.abajar.crrcsimeditor.avl.AVLGeometry;
import com.abajar.crrcsimeditor.avl.mass.Mass;
import com.abajar.crrcsimeditor.crrcsim.CRRCSim;
import com.abajar.crrcsimeditor.view.annotations.CRRCSimEditorField;
import com.abajar.crrcsimeditor.view.annotations.CRRCSimEditorReadOnly;
import java.util.ArrayList;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 *
 * @author Hugo
 */
public class CenterOfMass {
    private final CRRCSim crrcsim;

    @XmlAttribute
    public final String units = "1";

    @Override
    public String toString() {
        return "Center of Mass";
    }

    public CenterOfMass(){
        this.crrcsim = null;
    }

    public CenterOfMass(CRRCSim crrcsim){
        this.crrcsim = crrcsim;
    }

    private float getMassesSum(){
        float total=0;
        for(Mass mass: this.crrcsim.getAvl().getGeometry().getMasses()){
            total += mass.getMass();
        }
        return total;
    }

    @CRRCSimEditorReadOnly(text="X position",
        help="X position of center of masses"
    )
    @XmlAttribute(name="x")
    @XmlJavaTypeAdapter(value=MetersConversorInverted.class, type=float.class)
    public float getX(){
        float total = 0;
        float totalMass = this.getMassesSum();
        for(Mass mass: this.crrcsim.getAvl().getGeometry().getMasses()) total += mass.getX() * mass.getMass();
        return  totalMass == 0 ? 0 : total / totalMass;
    }

    @CRRCSimEditorReadOnly(text="Y position",
        help="Y position of center of masses"
    )
    @XmlAttribute(name="y")
    @XmlJavaTypeAdapter(value=MetersConversor.class, type=float.class)
    public float getY(){
        float total = 0;
        float totalMass = this.getMassesSum();
        for(Mass mass: this.crrcsim.getAvl().getGeometry().getMasses()) total += mass.getY() * mass.getMass();
        return  totalMass == 0 ? 0 : total / totalMass;
    }

    @CRRCSimEditorReadOnly(text="Z position",
        help="Z position of center of masses"
    )
    @XmlAttribute(name="z")
    @XmlJavaTypeAdapter(value=MetersConversorInverted.class, type=float.class)
    public float getZ(){
        float total = 0;
        float totalMass = this.getMassesSum();
        for(Mass mass: this.crrcsim.getAvl().getGeometry().getMasses()) total += mass.getZ() * mass.getMass();
        return  totalMass == 0 ? 0 : total / totalMass;
    }
}
