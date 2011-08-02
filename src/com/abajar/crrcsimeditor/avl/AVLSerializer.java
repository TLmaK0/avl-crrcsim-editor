/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.abajar.crrcsimeditor.avl;

import java.io.OutputStream;

/**
 *
 * @author hfreire
 */
public interface AVLSerializer{
    public void toAVLFile(OutputStream out);
}
