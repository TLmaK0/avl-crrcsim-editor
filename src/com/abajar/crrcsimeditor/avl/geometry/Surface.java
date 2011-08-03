/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.abajar.crrcsimeditor.avl.geometry;

import com.abajar.crrcsimeditor.avl.AVLSerializable;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import static com.abajar.crrcsimeditor.avl.AVLGeometry.fs;
/**
 *
 * @author hfreire
 */
public class Surface implements AVLSerializable {

    //TODO: NOWAKE
    //TODO: NOALBE
    //TODO: NOLOAD

    //SECTION
    private String name;
    private float Nchord;
    private float Cspace;
    private float Nspan;
    private float Sspace;

    //TODO: COMPONENT
    //TODO: YDUPLICATE
    //TODO: SCALE

    private final float[] dXYZ = new float[3];
    private float dAinc;
    
    private final ArrayList<Section> sections = new ArrayList<Section>();

    @Override
    public String toString() {
        return this.getName();
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
     * @return the Nchord
     */
    public float getNchord() {
        return Nchord;
    }

    /**
     * @param Nchord the Nchord to set
     */
    public void setNchord(float Nchord) {
        this.Nchord = Nchord;
    }

    /**
     * @return the Cspace
     */
    public float getCspace() {
        return Cspace;
    }

    /**
     * @param Cspace the Cspace to set
     */
    public void setCspace(float Cspace) {
        this.Cspace = Cspace;
    }

    /**
     * @return the Nspan
     */
    public float getNspan() {
        return Nspan;
    }

    /**
     * @param Nspan the Nspan to set
     */
    public void setNspan(float Nspan) {
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
     * @return the dXYZ
     */
    public float[] getdXYZ() {
        return dXYZ;
    }

    /**
     * @return the dAinc
     */
    public float getdAinc() {
        return dAinc;
    }

    /**
     * @param dAinc the dAinc to set
     */
    public void setdAinc(float dAinc) {
        this.dAinc = dAinc;
    }

    /**
     * @return the sections
     */
    public ArrayList<Section> getSections() {
        return sections;
    }

    @Override
    public void writeAVLData(OutputStream out) {
        PrintStream ps = new PrintStream(out);
        ps.print("SURFACE\n");                             //        SURFACE              | (keyword)
        ps.printf("%1$s\n", this.getName());                                        //Main Wing            | surface name string
        ps.printf("! Nchord  Cspace   [ Nspan Sspace ]\n" + fs(4) +"\n",
                this.getNchord(), this.getCspace(),
                this.getNspan(), this.getSspace());                                 //12   1.0  20  -1.5   | Nchord  Cspace   [ Nspan Sspace ]
        ps.print("TRANSLATE\n");                                 //TRANSLATE         |  (keyword)
        ps.printf("! dX  dY  dZ\n" + fs(3) + "\n",
                this.getdXYZ()[0], this.getdXYZ()[1], this.getdXYZ()[2]);              //10.0  0.0  0.5    | dX  dY  dZ
        ps.print("ANGLE\n");                                         //ANGLE       |  (keyword)
        ps.printf("! dAinc\n" + fs(1) + "\n", this.getdAinc());                                                     //2.0         | dAinc

        for(Section sect : this.getSections()){
            sect.writeAVLData(out);
        }
    }


}
