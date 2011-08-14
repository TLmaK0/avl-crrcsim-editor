/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.abajar.crrcsimeditor.avl;

import com.abajar.crrcsimeditor.avl.geometry.Body;
import com.abajar.crrcsimeditor.avl.geometry.Surface;
import com.abajar.crrcsimeditor.avl.mass.MassObject;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

/**
 *
 * @author hfreire
 */
public class AVLGeometry extends MassObject implements AVLSerializable{
    private String name = "";
    private float Mach;
    private final int[] iYiZsym = new int[2];
    private float Zsym;

    private final float[] SCBref = new float[3];
    private final float[] XYZref = new float[3];
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
     * @return the SCBref
     */
    public float[] getSCBref() {
        return SCBref;
    }

    /**
     * @return the XYZref
     */
    public float[] getXYZref() {
        return XYZref;
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
    public ArrayList<Surface> getSurfaces() {
        return surfaces;
    }

    /**
     * @return the body
     */
    public ArrayList<Body> getBodies() {
        return body;
    }

    @Override
    public String toString() {
        return "Geometry";
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
        ps.printf("%1$s\n", this.getName());
        ps.printf("#Mach\n%1$-19.4g\n", this.getMach());                                                         //0.0                 | Mach
        ps.printf("#iYsym   iZsym    Zsym\n" + formatInteger(2) + formatFloat(1,3) + "\n", this.getiYiZsym()[0], this.getiYiZsym()[1], this.getZsym());          //1     0     0.0     | iYsym  iZsym  Zsym
        ps.printf("#Sref    Cref     Bref\n" + formatFloat(3) + "\n", this.getSCBref()[0], this.getSCBref()[1], this.getSCBref()[2]);          //4.0   0.4   0.1     | Sref   Cref   Bref
        ps.printf("#Xref    Yref      Zref\n" + formatFloat(3) + "\n", this.getXYZref()[0], this.getXYZref()[1], this.getXYZref()[2]);          //0.1   0.0   0.0     | Xref   Yref   Zref
        
        if(this.CDp != 0){
            ps.printf("#CDp\n%1$-19.4g\n", this.CDp);                                                         //0.020               | CDp  (optional)
        }
        
        for(Surface surf : this.getSurfaces()){
            surf.writeAVLData(out);
        }
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
            format += "%" + n + "$-8.2g ";
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
     * @return the iYiZsym
     */
    public int[] getiYiZsym() {
        return iYiZsym;
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

}
