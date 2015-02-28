/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.abajar.crrcsimeditor.crrcsim;

import com.abajar.crrcsimeditor.crrcsim.CRRCSim.Change;
import com.abajar.crrcsimeditor.view.annotations.CRRCSimEditor;
import com.abajar.crrcsimeditor.view.avl.SelectorMutableTreeNode.ENABLE_BUTTONS;
import java.util.ArrayList;

/**
 *
 * @author Hugo
 */
@CRRCSimEditor(buttons={ENABLE_BUTTONS.DELETE, ENABLE_BUTTONS.ADD_CHANGELOG})
public  class Changelog extends ArrayList<Change>{

    public Changelog() {
    }

}

