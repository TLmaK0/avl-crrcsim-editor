/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.abajar.crrcsimeditor.avl.geometry;

import com.abajar.crrcsimeditor.view.annotations.CRRCSimEditorField;
import com.abajar.crrcsimeditor.view.avl.SelectorMutableTreeNode.ENABLE_BUTTONS;
import com.abajar.crrcsimeditor.view.annotations.CRRCSimEditor;
import javax.xml.bind.annotation.XmlElement;
import com.abajar.crrcsimeditor.avl.mass.MassObject;
import com.abajar.crrcsimeditor.avl.AVLSerializable;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import javax.xml.bind.annotation.XmlElementWrapper;
import static com.abajar.crrcsimeditor.avl.AVLGeometry.*;

/**
 *
 * @author hfreire
 */
@CRRCSimEditor(buttons={ENABLE_BUTTONS.ADD_CONTROL, ENABLE_BUTTONS.DELETE})
public class Section  extends MassObject implements AVLSerializable{

    //TODO: AIRFOIL
    //TODO: DESIGN
    @CRRCSimEditorField(text="Xle",
        help="airfoil's leading edge X location"
    )
    private float Xle;

    @CRRCSimEditorField(text="Yle",
        help="airfoil's leading edge Y location"
    )
    private float Yle;

    @CRRCSimEditorField(text="Zle",
        help="airfoil's leading edge Z location"
    )
    private float Zle;

    @CRRCSimEditorField(text="Chord",
        help="the airfoil's chord  (trailing edge is at Xle+Chord,Yle,Zle)"
    )
    private float Chord;

    @CRRCSimEditorField(text="Ainc",
        help="incidence angle, taken as a rotation (+ by RH rule) about\r\n"
            + "the surface's spanwise axis projected onto the Y-Z plane"
    )
    private float Ainc;

    @CRRCSimEditorField(text="Nspan",
        help="number of spanwise vortices until the next section [ optional ]"
    )
    private int Nspan;

    @CRRCSimEditorField(text="Sspace",
        help="controls the spanwise spacing of the vortices      [ optional ]"
    )
    private float Sspace;

    @CRRCSimEditorField(text="NACA",
        help="sets the camber line to the NACA 4-digit shape specified"
    )
    private float X1;

    @CRRCSimEditorField(text="AFILE",
        help="XFoil filename"
    )
    private float X2;

    @CRRCSimEditorField(text="X1",
        help="If present, the optional X1 X2 parameters indicate that only the\r\n"
            + "x/c range X1..X2 from the coordinates is to be assigned to the surface.\r\n"
            + "If the surface is a 20%-chord flap, for example, then X1 X2\r\n"
            + "would be 0.80 1.00.  This allows the camber shape to be easily\r\n"
            + "assigned to any number of surfaces in piecewise manner."
    )
    private String NACA="";

    @CRRCSimEditorField(text="X2",
        help="If present, the optional X1 X2 parameters indicate that only the\r\n"
            + "x/c range X1..X2 from the coordinates is to be assigned to the surface.\r\n"
            + "If the surface is a 20%-chord flap, for example, then X1 X2\r\n"
            + "would be 0.80 1.00.  This allows the camber shape to be easily\r\n"
            + "assigned to any number of surfaces in piecewise manner."
    )
    private String AFILE="";

    private final ArrayList<Control> controls = new ArrayList<Control>();

    //TODO: CLAF
    //TODO: CDCL

    /**
     * @return the Chord
     */
    public float getChord() {
        return Chord;
    }

    /**
     * @param Chord the Chord to set
     */
    public void setChord(float Chord) {
        this.Chord = Chord;
    }

    /**
     * @return the Ainc
     */
    public float getAinc() {
        return Ainc;
    }

    /**
     * @param Ainc the Ainc to set
     */
    public void setAinc(float Ainc) {
        this.Ainc = Ainc;
    }

    /**
     * @return the Nspan
     */
    public int getNspan() {
        return Nspan;
    }

    /**
     * @param Nspan the Nspan to set
     */
    public void setNspan(int Nspan) {
        this.Nspan = Nspan;
    }

    /**
     * @return the Sspace
     */
    public float getSspace() {
        return Sspace;
    }

    /**
     * @param Sspace the Sspace to set
     */
    public void setSspace(float Sspace) {
        this.Sspace = Sspace;
    }

    /**
     * @return the NACA
     */
    public String getNACA() {
        return NACA;
    }

    /**
     * @param NACA the NACA to set
     */
    public void setNACA(String NACA) {
        this.NACA = NACA;
    }

    /**
     * @return the controls
     */
    @XmlElementWrapper
    @XmlElement(name = "control")
    public ArrayList<Control> getControls() {
        return controls;
    }


    @Override
    public String toString() {
        return "section";
    }

    /**
     * @return the Xle
     */
    public float getXle() {
        return Xle;
    }

    /**
     * @param Xle the Xle to set
     */
    public void setXle(float Xle) {
        this.Xle = Xle;
    }

    /**
     * @return the Yle
     */
    public float getYle() {
        return Yle;
    }

    /**
     * @param Yle the Yle to set
     */
    public void setYle(float Yle) {
        this.Yle = Yle;
    }

    /**
     * @return the Zle
     */
    public float getZle() {
        return Zle;
    }

    /**
     * @param Zle the Zle to set
     */
    public void setZle(float Zle) {
        this.Zle = Zle;
    }

    /**
     * @return the AFILE
     */
    public String getAFILE() {
        return AFILE;
    }

    /**
     * @param AFILE the AFILE to set
     */
    public void setAFILE(String AFILE) {
        this.AFILE = AFILE;
    }


    @Override
    public void writeAVLData(OutputStream out) {
        PrintStream ps = new PrintStream(out);
        ps.print("\nSECTION\n");                      //        SECTION                             |  (keyword)
        ps.printf(locale, "#Xle      Yle       Zle       Chord     Ainc      Nspan     Sspace\n" + formatFloat(5), this.getXle(),
                this.getYle(), this.getZle(),
                this.getChord(), this.getAinc());     //0.0 5.0 0.2   0.50  1.50   5 -2.0   | Xle Yle Zle   Chord Ainc   [ Nspan Sspace ]

        if (this.getNspan() != 0){
            ps.printf(locale, formatInteger(1) + formatFloat(1), this.getNspan(), this.getSspace());
        }
        ps.print("\n");

        if (!this.getNACA().equals("")){
            //NACA                      |    (keyword)
            ps.println("NACA");
            ps.println(this.getNACA());            //4300                      | section NACA camberline
        }else if (!this.getAFILE().equals("")){
            ps.print("AFILE");
            if (this.getX1()!=0 || this.getX2()!=0) ps.printf(locale, " " + formatFloat(2), this.getX1(), this.getX2());
            ps.println();
            ps.println(this.getAFILE());
        }

        for(Control control : this.getControls()){
            control.writeAVLData(out);
        }
    }

    /**
     * @return the x
     */
    public float getX1() {
        return X1;
    }

    /**
     * @param x the x to set
     */
    public void setX1(float x) {
        this.X1 = x;
    }

    /**
     * @return the y
     */
    public float getX2() {
        return X2;
    }

    /**
     * @param y the y to set
     */
    public void setX2(float y) {
        this.X2 = y;
    }

    public Control createControl() {
        Control control = new Control();
        this.getControls().add(control);
        return control;
    }
}
