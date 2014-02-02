/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.abajar.crrcsimeditor.avl;

import com.abajar.crrcsimeditor.avl.mass.Mass;
import com.abajar.crrcsimeditor.avl.mass.MassObject;
import com.abajar.crrcsimeditor.avl.geometry.Body;
import com.abajar.crrcsimeditor.avl.geometry.Surface;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 *
 * @author hfreire
 */
@XmlRootElement
@XmlSeeAlso({
    MassObject.class
})
public class AVLGeometry extends MassObject implements AVLSerializable{
    private String name = "Geometry";
    private float Mach;

    private  int iYsym;
    private  int iZsym;
    private float Zsym;

    private  float Sref;
    private  float Cref;
    private  float Bref;

    private  float Xref;
    private  float Yref;
    private  float Zref;

    private float CDp;
    private final ArrayList<Surface> surfaces = new ArrayList<Surface>();
    private final ArrayList<Body> body = new ArrayList<Body>();

    static final long serialVersionUID = 7590357473387179207L;

    /**
     * @return the Mach
     */
    public float getMach() {
        return Mach;
    }

    /**
     * @param Mach the Mach to set
     */
    public void setMach(float Mach) {
        this.Mach = Mach;
    }
    
    /**
     * @return the CDp
     */
    public float getCDp() {
        return CDp;
    }

    /**
     * @return the surfaces
     */
    @XmlElementWrapper
    @XmlElement(name="surface")
    public ArrayList<Surface> getSurfaces() {
        return surfaces;
    }

    /**
     * @return the body
     */
    @XmlElement(name="body")
    public ArrayList<Body> getBodies() {
        return body;
    }

    @Override
    public String toString() {
        return this.name;
    }

    /**
     * @param CDp the CDp to set
     */
    public void setCDp(float CDp) {
        this.CDp = CDp;
    }

    @Override
    public void writeAVLData(OutputStream out) {
        PrintStream ps = new PrintStream(out);
        ps.print("#Created with CRRCsimEditor http://sourceforge.net/projects/crrcsimeditor/ \n");
        ps.printf(locale, "%1$s\n", this.getName());
        ps.printf(locale, "#Mach\n%1$-19.4g\n", this.getMach());                                                         //0.0                 | Mach
        ps.printf(locale, "#iYsym   iZsym    Zsym\n" + formatInteger(2) + formatFloat(1,3) + "\n", this.getiYsym(), this.getiZsym(), this.getZsym());          //1     0     0.0     | iYsym  iZsym  Zsym
        ps.printf(locale, "#Sref    Cref     Bref\n" + formatFloat(3) + "\n", this.getSref(), this.getCref(), this.getBref());          //4.0   0.4   0.1     | Sref   Cref   Bref
        ps.printf(locale, "#Xref    Yref      Zref\n" + formatFloat(3) + "\n", this.getXref(), this.getYref(), this.getZref());          //0.1   0.0   0.0     | Xref   Yref   Zref
        
        if(this.CDp != 0){
            ps.printf(locale, "#CDp\n%1$-19.4g\n", this.CDp);                                                         //0.020               | CDp  (optional)
        }
        
        for(Surface surf : this.getSurfaces()){
            surf.writeAVLData(out);
        }

        for(Body body : this.getBodies()){
            body.writeAVLData(out);
        }
    }

    @Override
    public void writeAVLMassData(OutputStream out) {
        PrintStream ps = new PrintStream(out);
        ps.print("Lunit = 0.01 m\n" +
                    "Munit = 0.001 kg\n" +
                    "Tunit = 1.0 s\n" +
                    "g   = 9.81\n" +
                    "rho = 1.225\n");
        ps.print("#mass     x       y        z        Ixx      Iyy      Izz\n");
        super.writeAVLMassData(out);
    }



    public static String formatInteger(int numberOfValues, int startValue){
        String format ="";
        for(int n=startValue; n < startValue + numberOfValues; n++){
            format += "%" + n + "$-8d ";
        }
        return format;
    }

    public static String formatInteger(int numberOfValues){
        return formatInteger(numberOfValues, 1);
    }

    public static String formatFloat(int numberOfValues){
        return formatFloat(numberOfValues, 1);
    }

    public static String formatFloat(int numberOfValues, int startValue){
        String format ="";
        for(int n=startValue; n < startValue + numberOfValues; n++){
            format += "%" + n + "$-8.1f ";
        }
        return format;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the Zsym
     */
    public float getZsym() {
        return Zsym;
    }

    /**
     * @param Zsym the Zsym to set
     */
    public void setZsym(float Zsym) {
        this.Zsym = Zsym;
    }

    /**
     * @return the iYsym
     */
    public int getiYsym() {
        return iYsym;
    }

    /**
     * @param iYsym the iYsym to set
     */
    public void setiYsym(int iYsym) {
        this.iYsym = iYsym;
    }

    /**
     * @return the iZsym
     */
    public int getiZsym() {
        return iZsym;
    }

    /**
     * @param iZsym the iZsym to set
     */
    public void setiZsym(int iZsym) {
        this.iZsym = iZsym;
    }

    /**
     * @return the Sref
     */
    public float getSref() {
        return Sref;
    }

    /**
     * @param Sref the Sref to set
     */
    public void setSref(float Sref) {
        this.Sref = Sref;
    }

    /**
     * @return the Cref
     */
    public float getCref() {
        return Cref;
    }

    /**
     * @param Cref the Cref to set
     */
    public void setCref(float Cref) {
        this.Cref = Cref;
    }

    /**
     * @return the Bref
     */
    public float getBref() {
        return Bref;
    }

    /**
     * @param Bref the Bref to set
     */
    public void setBref(float Bref) {
        this.Bref = Bref;
    }

    /**
     * @return the Xref
     */
    public float getXref() {
        return Xref;
    }

    /**
     * @param Xref the Xref to set
     */
    public void setXref(float Xref) {
        this.Xref = Xref;
    }

    /**
     * @return the Yref
     */
    public float getYref() {
        return Yref;
    }

    /**
     * @param Yref the Yref to set
     */
    public void setYref(float Yref) {
        this.Yref = Yref;
    }

    /**
     * @return the Zref
     */
    public float getZref() {
        return Zref;
    }

    /**
     * @param Zref the Zref to set
     */
    public void setZref(float Zref) {
        this.Zref = Zref;
    }


}
