/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.abajar.crrcsimeditor.avl;

import com.abajar.crrcsimeditor.avl.geometry.Body;
import com.abajar.crrcsimeditor.avl.geometry.Surface;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

/**
 *
 * @author hfreire
 */
public class AVLGeometry implements AVLSerializable{
    private float Mach;
    private final float[] iYiZZsym = new float[3];
    private final float[] SCBref = new float[3];
    private final float[] XYZref = new float[3];
    private float CDp;
    private final ArrayList<Surface> surfaces = new ArrayList<Surface>();
    private final ArrayList<Body> body = new ArrayList<Body>();

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
        ps.println("#Created with CRRCsimEditor");
        ps.printf("%1$-19.4g | Mach\r\n", this.getMach());                                                         //0.0                 | Mach
        ps.printf("%1$-4.4g %2$-4.4g %3$-4.4g | iYsym  iZsym  Zsym\r\n", this.getiYiZZsym()[0], this.getiYiZZsym()[1], this.getiYiZZsym()[2]);          //1     0     0.0     | iYsym  iZsym  Zsym
        ps.printf("%1$-4.4g %2$-4.4g %3$-4.4g | Sref   Cref   Bref\r\n", this.getSCBref()[0], this.getSCBref()[1], this.getSCBref()[2]);          //4.0   0.4   0.1     | Sref   Cref   Bref
        ps.printf("%1$-4.4g %2$-4.4g %3$-4.4g | Xref   Yref   Zref\r\n", this.getXYZref()[0], this.getXYZref()[1], this.getXYZref()[2]);          //0.1   0.0   0.0     | Xref   Yref   Zref
        ps.printf("%1$-19.4g | CDp  (optional)\r\n", this.CDp);                                                         //0.020               | CDp  (optional)
    }

    /**
     * @return the iYiZZsym
     */
    public float[] getiYiZZsym() {
        return iYiZZsym;
    }



}
