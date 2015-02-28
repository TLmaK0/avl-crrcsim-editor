/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.abajar.crrcsimeditor.crrcsim;

import com.abajar.crrcsimeditor.view.annotations.CRRCSimEditorNode;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author Hugo
 */
public class Sound implements Serializable {
    private Sample sample = new Sample();

    @Override
    public String toString(){
        return "Sound";
    }

    public Sound() {
    }

    /**
     * @return the sample
     */
    @CRRCSimEditorNode
    @XmlElement
    public Sample getSample() {
        return sample;
    }
}

