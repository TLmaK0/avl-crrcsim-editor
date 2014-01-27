/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.abajar.crrcsimeditor.avl.geometry;

import com.abajar.crrcsimeditor.avl.AVLSerializable;
import com.abajar.crrcsimeditor.avl.mass.MassObject;
import java.io.OutputStream;
import java.io.PrintStream;
import static com.abajar.crrcsimeditor.avl.AVLGeometry.formatFloat;
import static com.abajar.crrcsimeditor.avl.AVLGeometry.formatInteger;

/**
 *
 * @author hfreire
 */
public class Body extends MassObject implements AVLSerializable  {

    private String name;
    private int Nbody;
    private float Bspace;
    private float Ydupl;
    private float dX;
    private float dY;
    private float dZ;
    private String BFILE;

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
     * @return the NBody
     */
    public int getNbody() {
        return Nbody;
    }

    /**
     * @return the Bspace
     */
    public float getBspace() {
        return Bspace;
    }

    /**
     * @return the Ydupl
     */
    public float getYdupl() {
        return Ydupl;
    }

    /**
     * @return the dX
     */
    public float getdX() {
        return dX;
    }

    /**
     * @return the dY
     */
    public float getdY() {
        return dY;
    }

    /**
     * @return the dZ
     */
    public float getdZ() {
        return dZ;
    }

    /**
     * @return the BFILE
     */
    public String getBFILE() {
        return BFILE;
    }

    @Override
    public String toString() {
        return this.getName();
    }
    
    @Override
    public void writeAVLData(OutputStream out) {
        PrintStream ps = new PrintStream(out);
        ps.print("BODY\n");                     
        ps.printf("%1$s\n", this.getName());    

        ps.printf("#Nbody  Bspace\n" + formatInteger(1) + formatFloat(1,2),
                this.getNbody(), this.getBspace());

        ps.print("\n");

        ps.print("YDUPLICATE\n");               
        ps.printf(formatFloat(1) + "\n", this.getYdupl());

        if (this.getdX() != 0 ||  this.getdY() != 0 || this.getdZ() != 0){
            ps.print("TRANSLATE\n");                      
            ps.printf("#dX  dY  dZ\n" + formatFloat(3) + "\n",
                    this.getdX(), this.getdY(), this.getdZ());
        }

        ps.print("BFILE\n");
        ps.print(this.getBFILE() + "\n");
    }

    /**
     * @param Nbody the Nbody to set
     */
    public void setNbody(int Nbody) {
        this.Nbody = Nbody;
    }

    /**
     * @param Bspace the Bspace to set
     */
    public void setBspace(float Bspace) {
        this.Bspace = Bspace;
    }

    /**
     * @param Ydupl the Ydupl to set
     */
    public void setYdupl(float Ydupl) {
        this.Ydupl = Ydupl;
    }

    /**
     * @param dX the dX to set
     */
    public void setdX(float dX) {
        this.dX = dX;
    }

    /**
     * @param dY the dY to set
     */
    public void setdY(float dY) {
        this.dY = dY;
    }

    /**
     * @param dZ the dZ to set
     */
    public void setdZ(float dZ) {
        this.dZ = dZ;
    }

    /**
     * @param BFILE the BFILE to set
     */
    public void setBFILE(String BFILE) {
        this.BFILE = BFILE;
    }
}
