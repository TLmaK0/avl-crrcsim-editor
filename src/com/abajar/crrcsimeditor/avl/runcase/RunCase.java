/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.abajar.crrcsimeditor.avl.runcase;

/**
 *
 * @author hfreire
 */
public class RunCase {
    private Configuration configuration = new Configuration();
    private StabilityDerivatives stabilityDerivatives = new StabilityDerivatives();

    /**
     * @return the stabilityDerivatives
     */
    public StabilityDerivatives getStabilityDerivatives() {
        return stabilityDerivatives;
    }

    /**
     * @param stabilityDerivatives the stabilityDerivatives to set
     */
    public void setStabilityDerivatives(StabilityDerivatives stabilityDerivatives) {
        this.stabilityDerivatives = stabilityDerivatives;
    }

    /**
     * @return the configuration
     */
    public Configuration getConfiguration() {
        return configuration;
    }

    /**
     * @param configuration the configuration to set
     */
    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }
}
