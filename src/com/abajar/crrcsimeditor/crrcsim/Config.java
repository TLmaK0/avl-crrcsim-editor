/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.abajar.crrcsimeditor.crrcsim;

import java.io.Serializable;

/**
 *
 * @author Hugo
 */
public class Config  implements Serializable{

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

    public static class MassInertia {
        private String version = "1";
        private String units;
        private float Mass;
        private float I_xx;
        private float I_yy;
        private float I_zz;
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

    public static class Sound {
        private Sample sample;
        public Sound() {
        }
    }

    public static class Sample {
        private String filename;
        private String type;
        private float pitchfactor;
        private float maxvolume;
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
    }

    private String descr_long;
    private String descr_short;
    private MassInertia mass_inertia;
    private Sound sound;
    private Aero aero;
}
