/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.abajar.crrcsimeditor.crrcsim;

import com.abajar.crrcsimeditor.view.annotations.CRRCSimEditorField;
import com.abajar.crrcsimeditor.view.annotations.CRRCSimEditorNode;
import java.io.Serializable;
import java.util.ArrayList;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author Hugo
 */
public class Power implements Serializable {
    public static class Data {
        private static final String helpData = "Add 2 engine data at least. Use http://www.ecalc.ch/ to calculate Current, Voltage and rpms (you don't need to touch general settings)."
            + "First row use manufacturer data:"
            + "     Voltage = no-load Current: V"
            + "     Current = no-load Current: A"
            + "     Rpms = KV (w/o torque) * Voltage"
            + "Second row use Motor Optimum Efficiency"
            + "     Current"
            + "     Voltage"
            + "     Rpm"
            + "Third row use Motor Maximum"
            + "     Current"
            + "     Voltage"
            + "     Rpm";
        
        @CRRCSimEditorField(text="Voltage",
            help=helpData
        )
        private float U_K;

        @CRRCSimEditorField(text="Current",
            help=helpData
        )
        private float I_M;

        @CRRCSimEditorField(text="Rpms",
            help=helpData
        )
        private float rpms;

        public Data() {
        }

        /**
         * @return the U_K
         */
        public float getU_K() {
            return U_K;
        }

        /**
         * @param U_K the U_K to set
         */
        public void setU_K(float U_K) {
            this.U_K = U_K;
        }

        /**
         * @return the I_M
         */
        public float getI_M() {
            return I_M;
        }

        /**
         * @param I_M the I_M to set
         */
        public void setI_M(float I_M) {
            this.I_M = I_M;
        }

        /**
         * @return the n
         */
        public float getN() {
            return rpms / 60;
        }
        

        /**
         * @return the rpms
         */
        public float getRpms() {
            return rpms;
        }

        /**
         * @param rpms the rpms to set
         */
        public void setRpms(float rpms) {
            this.rpms = rpms;
        }
    }

    public static class DataIdle {
        @CRRCSimEditorField(text="Voltage",
            help="Voltage at idle"
        )
        private float U_K;

        @CRRCSimEditorField(text="Current",
            help="current at idle"
        )
        private float I_M;

        public DataIdle() {
        }

        /**
         * @return the U_K
         */
        public float getU_K() {
            return U_K;
        }

        /**
         * @param U_K the U_K to set
         */
        public void setU_K(float U_K) {
            this.U_K = U_K;
        }

        /**
         * @return the I_M
         */
        public float getI_M() {
            return I_M;
        }

        /**
         * @param I_M the I_M to set
         */
        public void setI_M(float I_M) {
            this.I_M = I_M;
        }
    }

    /**
     * @return the bateries
     */
    @CRRCSimEditorNode
    @XmlElement
    public ArrayList<Batery> getBateries() {
        return bateries;
    }

    /**
     * @param bateries the bateries to set
     */
    public void setBateries(ArrayList<Batery> bateries) {
        this.bateries = bateries;
    }

    public static class Engine {
        @CRRCSimEditorField(text="J_M",
            help="J_M, the engine's rotor's inertia, can be found in the manufacturer's data sheet,\r\n" +
                "or it has to be guessed. You can estimate it by regarding\r\n" +
                "the rotor as a solid iron cylinder of mass m (in kg) and diameter d (in m) using the formula:\r\n" +
                "J_M = 0.5 * m * d^2 / 4"
        )
        private float J_M;

        private ArrayList<Data> data;
        private ArrayList<DataIdle> dataIdle;

        private Gearing gearing = new Gearing();

        private int Calc = 1;

        public Engine() {
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
         * @return the gearing
         */
        @CRRCSimEditorNode
        @XmlElement
        public Gearing getGearing() {
            return gearing;
        }

        /**
         * @param gearing the gearing to set
         */
        public void setGearing(Gearing gearing) {
            this.gearing = gearing;
        }

        /**
         * @return the data
         */
        @CRRCSimEditorNode
        @XmlElement
        public ArrayList<Data> getData() {
            return data;
        }

        /**
         * @param data the data to set
         */
        public void setData(ArrayList<Data> data) {
            this.data = data;
        }

        /**
         * @return the Calc
         */
        public int getCalc() {
            return Calc;
        }

        /**
         * @return the dataIdle
         */
        @CRRCSimEditorNode
        @XmlElement
        public ArrayList<DataIdle> getDataIdle() {
            return dataIdle;
        }

        /**
         * @param dataIdle the dataIdle to set
         */
        public void setDataIdle(ArrayList<DataIdle> dataIdle) {
            this.dataIdle = dataIdle;
        }
    }

    public static class Gearing {
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

    public static class Propeller {
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

    @Override
    public String toString() {
        return "Power";
    }

    public static class Batery {
        @CRRCSimEditorField(text="Throttle_min",
            help="Lower limit for throttle input. Set to >0 if you want a behaviour of"+
                "a piston engine: once started, it keeps running with at least that throttle."+
                "Set to zero otherwise."+
                "This is an attribute of Battery (instead of engine) because of the brake in shaft."
        )
        private int throttle_min;

        @CRRCSimEditorField(text="C",
            help="Capacity at full charge in As"
        )
        private float C;

        @CRRCSimEditorField(text="R_I",
            help="Internal resistance in Ohm"
        )
        private int R_I;

        @CRRCSimEditorField(text="U_off",
            help="Voltage below which all the connected engines are turned off [V]"
        )
        private float U_off;

        @CRRCSimEditorField(text="U_0",
            help="Voltage at full charge [V]"
        )
        private float U_0;

        @CRRCSimEditorField(text="U_0rel",
            help="Semicolon separated values representing the proportinal voltage returned by the batery over the time. Ex. 1;1.05;0.95;0.90;0.85;0.70;0.60;\n"+
            "In this example, voltage at full charge is 1.05 * U_0"
        )
        private String U_0rel;

        private ArrayList<Shaft> shafts = new ArrayList<Shaft>();
        public Batery() {
        }

        /**
         * @return the throttle_min
         */
        @CRRCSimEditorNode
        @XmlAttribute
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
        @CRRCSimEditorNode
        @XmlElement
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


    public static class Shaft {
        @CRRCSimEditorField(text="J",
            help="Inertia in kg m^2"
        )
        private float J;
        
        @CRRCSimEditorField(text="brake",
            help="if brake is not zero, this shaft will stop rotating as soon as the throttle command is zero. This is needed for folding props."
        )
        private int brake;

        private ArrayList<Engine> engines = new ArrayList<Engine>();
        private Propeller propeller = new Propeller();


        public Shaft() {
        }

        /**
         * @return the J
         */
        @XmlAttribute
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
        @XmlAttribute
        public float getBrake() {
            return brake;
        }

        /**
         * @param brake the brake to set
         */
        public void setBrake(int brake) {
            this.brake = brake;
        }

        /**
         * @return the engines
         */
        @CRRCSimEditorNode
        @XmlElement
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
