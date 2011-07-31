/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.abajar.crrcsimeditor.avl.geometry;

import java.util.ArrayList;

/**
 *
 * @author hfreire
 */
public class Surface {

    //TODO: NOWAKE
    //TODO: NOALBE
    //TODO: NOLOAD

    //SECTION
    private String name;
    private float Nchord;
    private float Cspace;
    private float Nspan;
    private float Sspace;
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
}
