/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.abajar.crrcsimeditor.crrcsim;

import com.abajar.crrcsimeditor.avl.AVL;

/**
 *
 * @author Hugo
 */
public class CRRCSimFactory {
    public CRRCSim create(AVL avl){
        return new CRRCSim(avl);
    }

    public CRRCSim create(){
        return new CRRCSim();
    }
}
