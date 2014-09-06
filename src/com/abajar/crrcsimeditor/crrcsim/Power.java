/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.abajar.crrcsimeditor.crrcsim;

import com.abajar.crrcsimeditor.view.annotations.CRRCSimEditorField;
import java.util.ArrayList;

/**
 *
 * @author Hugo
 */
public class Power {

    /**
     * @return the bateries
     */
    public ArrayList<Batery> getBateries() {
        return bateries;
    }

    /**
     * @param bateries the bateries to set
     */
    public void setBateries(ArrayList<Batery> bateries) {
        this.bateries = bateries;
    }

    private static class Engine {
        @CRRCSimEditorField(text="k_M",
            help="k_M = (U_L - R_I * I_L) / (2 * PI * n_L / 60)"
                + "U_L voltage with load\r\n"
                + "R_I internal resistance"
                + "I_L current with load\r\n"
                + "n_L rpm with load\r\n"
        )
        private float k_M;

        @CRRCSimEditorField(text="R_I",
            help="Internal resistance.\r\n" +
                "R_I = (n_L * U_0 - n_0 * U_L) / (n_L * I_0 - n_0 * I_L)\r\n" +
                "U_0 voltage without load\r\n" +
                "U_L voltage with load\r\n" +
                "n_O rpm without load\r\n" +
                "n_L rpm with load\r\n" +
                "I_0 current without load\r\n" +
                "I_L current with load\r\n"
        )
        private float R_I;

        @CRRCSimEditorField(text="J_M",
            help="J_M, the engine's rotor's inertia, can be found in the manufacturer's data sheet,\r\n" +
                "or it has to be guessed. You can estimate it by regarding\r\n" +
                "the rotor as a solid iron cylinder of mass m (in kg) and diameter d (in m) using the formula:\r\n" +
                "J_M = 0.5 * m * d^2 / 4"
        )
        private float J_M;

        @CRRCSimEditorField(text="I_0",
            help="Current without load"
                + "I_0 = (U_0 - (2 * PI * n_0 / 60)) / R_I\r\n"
                + "U_0 voltage without load\r\n"
                + "n_O rpm without load\r\n"
                + "R_I internal resistance"
        )
        private float I_0;

        private Gearing gearing = new Gearing();

        public Engine() {
        }

        /**
         * @return the k_M
         */
        public float getK_M() {
            return k_M;
        }

        /**
         * @param k_M the k_M to set
         */
        public void setK_M(float k_M) {
            this.k_M = k_M;
        }

        /**
         * @return the R_I
         */
        public float getR_I() {
            return R_I;
        }

        /**
         * @param R_I the R_I to set
         */
        public void setR_I(float R_I) {
            this.R_I = R_I;
        }

        /**
         * @return the J_M
         */
        public float getJ_M() {
            return J_M;
        }

        /**
         * @param J_M the J_M to set
         */
        public void setJ_M(float J_M) {
            this.J_M = J_M;
        }

        /**
         * @return the I_0
         */
        public float getI_0() {
            return I_0;
        }

        /**
         * @param I_0 the I_0 to set
         */
        public void setI_0(float I_0) {
            this.I_0 = I_0;
        }

        /**
         * @return the gearing
         */
        public Gearing getGearing() {
            return gearing;
        }

        /**
         * @param gearing the gearing to set
         */
        public void setGearing(Gearing gearing) {
            this.gearing = gearing;
        }
    }

    private static class Gearing {
        @CRRCSimEditorField(text="J",
            help="Inertia"
        )
        private float J;

        @CRRCSimEditorField(text="i",
            help="Given omega is the speed of the shaft, i*omega is the speed of the device which is connected to the shaft using this gearing. "
        )
        private float i;
        public Gearing() {
        }

        /**
         * @return the J
         */
        public float getJ() {
            return J;
        }

        /**
         * @param J the J to set
         */
        public void setJ(float J) {
            this.J = J;
        }

        /**
         * @return the i
         */
        public float getI() {
            return i;
        }

        /**
         * @param i the i to set
         */
        public void setI(float i) {
            this.i = i;
        }
    }

    private static class Propeller {
        @CRRCSimEditorField(text="D",
            help="meters"
        )
        private float D;

        @CRRCSimEditorField(text="H",
            help="meters"
        )
        private float H;

        @CRRCSimEditorField(text="J",
            help="Inertia"
        )
        private float J;
        
        @CRRCSimEditorField(text="n_fold",
            help="The Propeller can be configured to be a folding prop, which folds as soon as it rotates slower than omega_fold."
            + " From the xml config, n_fold is read and converted using (omega_fold = n_fold * 2 * pi)"
        )
        private float n_fold;

        public Propeller() {
        }

        /**
         * @return the D
         */
        public float getD() {
            return D;
        }

        /**
         * @param D the D to set
         */
        public void setD(float D) {
            this.D = D;
        }

        /**
         * @return the H
         */
        public float getH() {
            return H;
        }

        /**
         * @param H the H to set
         */
        public void setH(float H) {
            this.H = H;
        }

        /**
         * @return the J
         */
        public float getJ() {
            return J;
        }

        /**
         * @param J the J to set
         */
        public void setJ(float J) {
            this.J = J;
        }

        /**
         * @return the n_fold
         */
        public float getN_fold() {
            return n_fold;
        }

        /**
         * @param n_fold the n_fold to set
         */
        public void setN_fold(float n_fold) {
            this.n_fold = n_fold;
        }
    }
    
    private ArrayList<Batery> bateries = new ArrayList<Batery>();
    public Power() {
    }

    private static class Batery {
        private String filename;
        private int throttle_min;

        private ArrayList<Shaft> shafts = new ArrayList<Shaft>();
        public Batery() {
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
         * @return the throttle_min
         */
        public int getThrottle_min() {
            return throttle_min;
        }

        /**
         * @param throttle_min the throttle_min to set
         */
        public void setThrottle_min(int throttle_min) {
            this.throttle_min = throttle_min;
        }

        /**
         * @return the shafts
         */
        public ArrayList<Shaft> getShafts() {
            return shafts;
        }

        /**
         * @param shafts the shafts to set
         */
        public void setShafts(ArrayList<Shaft> shafts) {
            this.shafts = shafts;
        }
    }


    private static class Shaft {
        @CRRCSimEditorField(text="J",
            help="Inertia"
        )
        private float J;
        
        @CRRCSimEditorField(text="brake",
            help="if brake is not zero, this shaft will stop rotating as soon as the throttle command is zero. This is needed for folding props."
        )
        private float brake;

        private ArrayList<Engine> engines = new ArrayList<Engine>();
        private Propeller propeller = new Propeller();


        public Shaft() {
        }

        /**
         * @return the J
         */
        public float getJ() {
            return J;
        }

        /**
         * @param J the J to set
         */
        public void setJ(float J) {
            this.J = J;
        }

        /**
         * @return the brake
         */
        public float getBrake() {
            return brake;
        }

        /**
         * @param brake the brake to set
         */
        public void setBrake(float brake) {
            this.brake = brake;
        }

        /**
         * @return the engines
         */
        public ArrayList<Engine> getEngines() {
            return engines;
        }

        /**
         * @param engines the engines to set
         */
        public void setEngines(ArrayList<Engine> engines) {
            this.engines = engines;
        }

        /**
         * @return the propeller
         */
        public Propeller getPropeller() {
            return propeller;
        }

        /**
         * @param propeller the propeller to set
         */
        public void setPropeller(Propeller propeller) {
            this.propeller = propeller;
        }
    }
}
