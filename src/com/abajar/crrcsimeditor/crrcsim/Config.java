/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.abajar.crrcsimeditor.crrcsim;

import com.abajar.crrcsimeditor.avl.mass.Mass;
import com.abajar.crrcsimeditor.view.annotations.CRRCSimEditor;
import com.abajar.crrcsimeditor.view.annotations.CRRCSimEditorField;
import com.abajar.crrcsimeditor.view.annotations.CRRCSimEditorNode;
import com.abajar.crrcsimeditor.view.avl.SelectorMutableTreeNode.ENABLE_BUTTONS;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Hugo
 */
@CRRCSimEditor(buttons={ENABLE_BUTTONS.ADD_SOUND})
public class Config  implements Serializable{

    @CRRCSimEditorField(text="short description",
        help="Short description of the config"
    )
    private String descr_short = "new config";

    @CRRCSimEditorField(text="long description",
        help="Long description of the config"
    )
    private String descr_long;

    private MassInertia mass_inertia = new MassInertia();
    private Sound sound = new Sound();
    private Power power = new Power();
    private Aero aero;

    @Override
    public String toString() {
        return this.descr_short;
    }


    /**
     * @return the descr_long
     */
    public String getDescr_long() {
        return descr_long;
    }

    /**
     * @param descr_long the descr_long to set
     */
    public void setDescr_long(String descr_long) {
        this.descr_long = descr_long;
    }

    /**
     * @return the descr_short
     */
    public String getDescr_short() {
        return descr_short;
    }

    /**
     * @param descr_short the descr_short to set
     */
    public void setDescr_short(String descr_short) {
        this.descr_short = descr_short;
    }

    /**
     * @return the mass_inertia
     */
    public MassInertia getMass_inertia() {
        return mass_inertia;
    }

    /**
     * @param mass_inertia the mass_inertia to set
     */
    public void setMass_inertia(MassInertia mass_inertia) {
        this.mass_inertia = mass_inertia;
    }

    /**
     * @return the sound
     */
    @CRRCSimEditorNode
    public Sound getSound() {
        return sound;
    }

    /**
     * @param sound the sound to set
     */
    public void setSound(Sound sound) {
        this.sound = sound;
    }

    /**
     * @return the aero
     */
    public Aero getAero() {
        return aero;
    }

    /**
     * @param aero the aero to set
     */
    public void setAero(Aero aero) {
        this.aero = aero;
    }

    void setMass_inertiaFromMasses(ArrayList<Mass> masses) {
        this.calculateInertiasMasses(masses);
    }

    private double calculateMomentInertiaFromAxis(float coord1, float coord2, float originalMomentInertia, float mass){
        //Parallel Axis Teorem http://en.wikipedia.org/wiki/Parallel_axis_theorem
        //Ixx_0 = I_xx + m * r^2
        //r = square(y^2 + z^2)
        //Ixx_0 = I_xx + m * square(y^2 + z^2)^2
        //Ixx_0 = I_xx + m * y^2 + z^2
        return originalMomentInertia + mass * (Math.pow(coord1, 2) + Math.pow(coord2, 2));
    }

    private double calculateProductInertiaFromAxis(float coord1, float coord2, float originalProductInertia, float mass){
        //Parallel Axes Theorem for Products of Inertia http://homepages.wmich.edu/~kamman/Me659InertiaMatrix.pdf
        //I_xz_0 = Ixz + m * x * z
        return originalProductInertia + mass * coord1 * coord2;
    }

    private void calculateInertiasMasses(ArrayList<Mass> masses) {
        float I_xx = 0;
        float I_yy = 0;
        float I_zz = 0;
        float I_xz = 0;
        float totalMass = 0;
        for(Mass mass: masses){
            I_xx += calculateMomentInertiaFromAxis(mass.getY(), mass.getZ(), mass.getIxx(), mass.getMass());
            I_yy += calculateMomentInertiaFromAxis(mass.getX(), mass.getZ(), mass.getIyy(), mass.getMass());
            I_zz += calculateMomentInertiaFromAxis(mass.getX(), mass.getY(), mass.getIzz(), mass.getMass());
            I_xz += calculateProductInertiaFromAxis(mass.getX(), mass.getZ(), mass.getIxz(), mass.getMass());
            totalMass += mass.getMass();
        }

        //setting and convert to kg * m2
        this.mass_inertia.setI_xx(I_xx / 10000000);
        this.mass_inertia.setI_yy(I_yy / 10000000);
        this.mass_inertia.setI_zz(I_zz / 10000000);
        this.mass_inertia.setI_xz(I_xz / 10000000);
        this.mass_inertia.setMass(totalMass / 1000);
    }

    public static class MassInertia implements Serializable {
        private String version = "1";
        private String units = "1";

        @Override
        public String toString() {
            return "mass";
        }

        @CRRCSimEditorField(text="mass",
            help="total aireplane mass"
        )
        private float Mass;

        @CRRCSimEditorField(text="I_xx",
            help="Moment of Inertia xx"
        )
        private float I_xx;

        @CRRCSimEditorField(text="I_yy",
            help="Moment of Inertia yy"
        )
        private float I_yy;

        @CRRCSimEditorField(text="I_zz",
            help="Moment of Inertia zz"
        )
        private float I_zz;

        @CRRCSimEditorField(text="I_xz",
            help="Product of Inertia xz"
        )
        private float I_xz;

        public MassInertia() {
        }

        /**
         * @return the version
         */
        public String getVersion() {
            return version;
        }

        /**
         * @param version the version to set
         */
        public void setVersion(String version) {
            this.version = version;
        }

        /**
         * @return the units
         */
        public String getUnits() {
            return units;
        }

        /**
         * @param units the units to set
         */
        public void setUnits(String units) {
            this.units = units;
        }

        /**
         * @return the Mass
         */
        public float getMass() {
            return Mass;
        }

        /**
         * @param Mass the Mass to set
         */
        public void setMass(float Mass) {
            this.Mass = Mass;
        }

        /**
         * @return the I_xx
         */
        public float getI_xx() {
            return I_xx;
        }

        /**
         * @param I_xx the I_xx to set
         */
        public void setI_xx(float I_xx) {
            this.I_xx = I_xx;
        }

        /**
         * @return the I_yy
         */
        public float getI_yy() {
            return I_yy;
        }

        /**
         * @param I_yy the I_yy to set
         */
        public void setI_yy(float I_yy) {
            this.I_yy = I_yy;
        }

        /**
         * @return the I_zz
         */
        public float getI_zz() {
            return I_zz;
        }

        /**
         * @param I_zz the I_zz to set
         */
        public void setI_zz(float I_zz) {
            this.I_zz = I_zz;
        }

        /**
         * @return the I_xz
         */
        public float getI_xz() {
            return I_xz;
        }

        /**
         * @param I_xz the I_xz to set
         */
        public void setI_xz(float I_xz) {
            this.I_xz = I_xz;
        }
    }

    public static class Sound implements Serializable {
        private Sample sample = new Sample();

        @Override
        public String toString(){
            return this.sample.getFilename();
        }

        public Sound() {
        }
    }

    public static class Sample implements Serializable {
        @CRRCSimEditorField(text="sound file",
            help="name of file for engine sound"
        )
        private String filename = "sound.wav";

        @CRRCSimEditorField(text="type",
            help="Type of sound: 0 glow engine, 1 electric engine, 2 glider sound"
        )
        private String type;

        @CRRCSimEditorField(text="pitchfactor",
            help="This number converts from speed of propeller to pitch of engine sound."
        )
        private float pitchfactor;
        
        @CRRCSimEditorField(text="maxvolume",
            help="The maximum sample volume (0.0 ... 1.0). The loudest sample should be set to 1.0."
        )
        private float maxvolume;

        @CRRCSimEditorField(text="v_min",
            help="Only for type=2: minimal velocity (relative to the airplane's \"neutral\" velocity) at which the sound can be heard."
        )
        private int v_min;

        @CRRCSimEditorField(text="v_max",
            help="Only for type=2: velocity (relative to the airplane's \"neutral\" velocity) at which the sound reaches maximum volume."
        )
        private int v_max;


        @CRRCSimEditorField(text="dist_max",
            help="Only for type=2: distance at which the sound reaches the minimum volume."
        )
        private int dist_max;

        public Sample() {
        }

        /**
         * @return the filename
         */
        public String getFilename() {
            return filename;
        }

        /**
         * @param filename the filename to set
         */
        public void setFilename(String filename) {
            this.filename = filename;
        }

        /**
         * @return the type
         */
        public String getType() {
            return type;
        }

        /**
         * @param type the type to set
         */
        public void setType(String type) {
            this.type = type;
        }

        /**
         * @return the pitchfactor
         */
        public float getPitchfactor() {
            return pitchfactor;
        }

        /**
         * @param pitchfactor the pitchfactor to set
         */
        public void setPitchfactor(float pitchfactor) {
            this.pitchfactor = pitchfactor;
        }

        /**
         * @return the maxvolume
         */
        public float getMaxvolume() {
            return maxvolume;
        }

        /**
         * @param maxvolume the maxvolume to set
         */
        public void setMaxvolume(float maxvolume) {
            this.maxvolume = maxvolume;
        }

        /**
         * @return the v_min
         */
        public int getV_min() {
            return v_min;
        }

        /**
         * @param v_min the v_min to set
         */
        public void setV_min(int v_min) {
            this.v_min = v_min;
        }

        /**
         * @return the v_max
         */
        public int getV_max() {
            return v_max;
        }

        /**
         * @param v_max the v_max to set
         */
        public void setV_max(int v_max) {
            this.v_max = v_max;
        }

        /**
         * @return the dist_max
         */
        public int getDist_max() {
            return dist_max;
        }

        /**
         * @param dist_max the dist_max to set
         */
        public void setDist_max(int dist_max) {
            this.dist_max = dist_max;
        }
    }

}
