/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.abajar.crrcsimeditor.avl.mass;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author hfreire
 */
public class MassObject {
    private final List<Mass> masses = new ArrayList<Mass>();

    /**
     * @return the masses
     */
    public List<Mass> getMasses() {
        return masses;
    }

    public void writeAVLMassData(OutputStream out) {
        for(Mass mass : this.getMasses()){
            mass.writeAVLMassData(out);
        }
    }

}
