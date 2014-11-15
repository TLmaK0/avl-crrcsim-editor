/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.abajar.crrcsimeditor.crrcsim;

import com.abajar.crrcsimeditor.view.annotations.CRRCSimEditorNode;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 *
 * @author Hugo
 */
public class Wheel implements Serializable {

    @Override
    public String toString() {
        return "Wheel";
    }

    /**
     * <wheel percent_brake="0.5" caster_angle_rad="0">
     *   <pos x="-2.7" y="0.69999999" z="-0.083300002" />
     *   <spring constant="65" damping="0.25" />
     * </wheel>
     */
    private float percent_brake = 0.5f;
    private float caster_angle_rad = 0;
    private Pos pos = new Pos();
    private Spring spring = new Spring();

    /**
     * @return the percent_brake
     */
    @XmlAttribute(name="percent_brake")
    public float getPercent_brake() {
        return percent_brake;
    }

    /**
     * @param percent_brake the percent_brake to set
     */
    public void setPercent_brake(float percent_brake) {
        this.percent_brake = percent_brake;
    }

    /**
     * @return the caster_angle_rad
     */
    @XmlAttribute(name="caster_angle_rad")
    public float getCaster_angle_rad() {
        return caster_angle_rad;
    }

    /**
     * @param caster_angle_rad the caster_angle_rad to set
     */
    public void setCaster_angle_rad(float caster_angle_rad) {
        this.caster_angle_rad = caster_angle_rad;
    }

    /**
     * @return the pos
     */
    @XmlElement
    @CRRCSimEditorNode
    public Pos getPos() {
        return pos;
    }

    /**
     * @param pos the pos to set
     */
    public void setPos(Pos pos) {
        this.pos = pos;
    }

    /**
     * @return the spring
     */
    @XmlElement
    @CRRCSimEditorNode
    public Spring getSpring() {
        return spring;
    }

    /**
     * @param spring the spring to set
     */
    public void setSpring(Spring spring) {
        this.spring = spring;
    }

}
