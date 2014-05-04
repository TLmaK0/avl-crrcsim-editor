/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.abajar.crrcsimeditor.avl.geometry;

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

    private float Xle;
    private float Yle;
    private float Zle;
    private float Chord;
    private float Ainc;
    private int Nspan;
    private float Sspace;
    private float X1;
    private float X2;
    private String NACA="";
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
