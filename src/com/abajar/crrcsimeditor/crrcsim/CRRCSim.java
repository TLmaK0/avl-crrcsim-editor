/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.abajar.crrcsimeditor.crrcsim;

import com.abajar.crrcsimeditor.avl.AVL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author Hugo
 */
@XmlRootElement(name="CRRCSim_airplane")
@XmlType(propOrder={"description","changelog","aero"})
public class CRRCSim {

    /**
    <CRRCSim_airplane version="2">
  <description>
    <en>
        This plane has been automatically converted from superzagi.air.
        Please update this text if you know more about it.
    </en>
  </description>
  <changelog>
    <change>
      <date>Unknown</date>
      <author>CRRCSim 0.9.5</author>
      <en>Automatically converted from .air file.</en>
    </change>
    <change>
      <date>Please write date.</date>
      <author>Please write your name and email.</author>
      <en>Please write down what you changed.</en>
    </change>
  </changelog>
       */
    
    public static class Changelog extends ArrayList<Change>{

        public Changelog() {
        }

    }

    private String version = "2";
    private Description description = new Description();
    private final Changelog changelog = new Changelog();
    private Aero aero;
    private AVL avl;

    protected CRRCSim(){

    }

    @Override
    public String toString(){
        return "Airplane";
    }

    /**
     * @return the description
     */
    public Description getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(Description description) {
        this.description = description;
    }

    /**
     * @return the avl
     */
    @XmlTransient
    public AVL getAvl() {
        return avl;
    }

    /**
     * @param avl the avl to set
     */
    public void setAvl(AVL avl) {
        this.avl = avl;
    }

    public static class Description {
        private String en;

        public Description() {
        }

        @Override
        public String toString(){
            return "description";
        }

        /**
         * @return the en
         */
        public String getEn() {
            return en;
        }

        /**
         * @param en the en to set
         */
        public void setEn(String en) {
            this.en = en;
        }
    }
    
    /**
     * @return the version
     */
    @XmlAttribute
    public String getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(String version) {
        this.version = version;
    }
    
    /**
     * @return the changelog
     */
    @XmlElement(name="changelog")
    public Changelog getChangelog() {
        return changelog;
    }

    /**
     * @return the aero
     */
    public Aero getAero() {
        return aero;
    }

    /**
     * @param aero the aero to set
     */
    public void setAero(Aero aero) {
        this.aero = aero;
    }

    public static class Change {
        private Date date;
        private String author;
        private String en;

        /**
         * @return the date
         */
        public Date getDate() {
            return date;
        }

        /**
         * @param date the date to set
         */
        public void setDate(Date date) {
            this.date = date;
        }

        /**
         * @return the author
         */
        public String getAuthor() {
            return author;
        }

        /**
         * @param author the author to set
         */
        public void setAuthor(String author) {
            this.author = author;
        }

        /**
         * @return the en
         */
        public String getEn() {
            return en;
        }

        /**
         * @param en the en to set
         */
        public void setEn(String en) {
            this.en = en;
        }


        @Override
        public String toString(){
            return "changelog";
        }
    }
    
}
