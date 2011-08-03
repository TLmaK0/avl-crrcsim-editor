/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.abajar.crrcsimeditor.avl.view;

import java.util.EnumSet;
import javax.swing.tree.DefaultMutableTreeNode;
import static java.util.EnumSet.of;

/**
 *
 * @author hfreire
 */
public class SelectorMutableTreeNode  extends DefaultMutableTreeNode{
    public enum ENABLE_BUTTONS {
        NONE,
        ADD_SURFACE,
        ADD_BODY,
        ADD_SECTION,
        ADD_CONTROL
    }

    public SelectorMutableTreeNode(Object object) {
        super(object);
    }

    public SelectorMutableTreeNode(Object object, EnumSet<ENABLE_BUTTONS> options) {
        super(object);
        this.options = options;
    }


    private EnumSet<ENABLE_BUTTONS> options = of(ENABLE_BUTTONS.NONE);

    /**
     * @return the options
     */
    public EnumSet<ENABLE_BUTTONS> getOptions() {
        return options;
    }
}
