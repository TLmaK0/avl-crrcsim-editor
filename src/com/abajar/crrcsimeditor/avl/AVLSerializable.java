/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.abajar.crrcsimeditor.avl;

import java.io.OutputStream;
import java.io.Serializable;

/**
 *
 * @author hfreire
 */
public interface AVLSerializable extends  Serializable{
    public void writeAVLData(OutputStream out);
}
