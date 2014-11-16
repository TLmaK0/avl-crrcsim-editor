/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.abajar.crrcsimeditor.desing;

import com.abajar.crrcsimeditor.avl.AVL;
import com.abajar.crrcsimeditor.view.annotations.CRRCSimEditorNode;

/**
 *
 * @author Hugo
 */
public class DesignRules {
    private final CenterOfMass centerOfMass;
    private final AVL avl;

    @Override
    public String toString() {
        return "Design Rules";
    }

    public DesignRules(AVL avl){
        this.avl = avl;
        this.centerOfMass = new CenterOfMass(avl);
    }

    /**
     * @return the centerOfMass
     */
    @CRRCSimEditorNode
    public CenterOfMass getCenterOfMass() {
        return centerOfMass;
    }
}
