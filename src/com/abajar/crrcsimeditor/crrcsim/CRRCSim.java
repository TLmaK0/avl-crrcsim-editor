/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.abajar.crrcsimeditor.crrcsim;

import com.abajar.crrcsimeditor.avl.AVL;
import com.abajar.crrcsimeditor.avl.connectivity.AvlRunner;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Path;
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
@XmlType(propOrder={"description","changelog","aero","config","avl"})
public class CRRCSim implements Serializable{

    /**
     * @return the config
     */
    public Config getConfig() {
        return config;
    }

    /**
     * @param config the config to set
     */
    public void setConfig(Config config) {
        this.config = config;
    }

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
     <aero version="1" units="0">
    <ref chord="0.551667" span="6.55" area="3.61111" speed="19.685" />
    <misc Alpha_0="0.0349066" eta_loc="0.3" CG_arm="0.25" span_eff="0.95" />
    <m Cm_0="-0.0112663" Cm_a="-0.575335" Cm_q="-11.4975" Cm_de="-0.597537" />
    <lift CL_0="0.563172" CL_max="1.1" CL_min="-0.6" CL_a="5.5036" CL_q="7.50999"
       CL_de="0.162" CL_drop="0.5" CL_CD0="0" />
    <drag CD_prof="0.02" Uexp_CD="-0.5" CD_stall="0" CD_CLsq="0.01" CD_AIsq="0"
       CD_ELsq="0" />
    <Y CY_b="-0.41561" CY_p="-0.42382" CY_r="0.29754" CY_dr="0" CY_da="-0.13589" />
    <l Cl_b="-0.250926" Cl_p="-0.611798" Cl_r="0.139581" Cl_dr="0"
       Cl_da="-0.00307921" />
    <n Cn_b="0.0567069" Cn_p="-0.0740898" Cn_r="-0.0687755" Cn_dr="0"
       Cn_da="0.0527143" />
  </aero>
  <config version="1">
    <descr_long>
      <en>Automatically converted from sovereign.air.</en>
    </descr_long>
    <descr_short>
      <en>default</en>
    </descr_short>
    <mass_inertia version="1" units="0" Mass="0.0450782" I_xx="0.0664259"
       I_yy="0.0162819" I_zz="0.081474" I_xz="0.000771958" />
    <sound version="1">
      <sample filename="" type="0" pitchfactor="0" maxvolume="1" />
    </sound>
  </config>
       */
    
    public static class Changelog extends ArrayList<Change>{

        public Changelog() {
        }

    }

    private String version = "2";
    private final Description description = new Description();
    private final Changelog changelog = new Changelog();
    private Aero aero;
    private final AVL avl;
    private Config config;

    protected CRRCSim(AVL avl){
        this.avl = avl;
    }

    protected CRRCSim(){
        this(new AVL());
    }

    public void calculateAero(String avlPath, Path originPath) throws IOException, InterruptedException, Exception{
        this.aero = new AeroFactory().createFromAvl(avlPath, this.avl, originPath);
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
     * @return the avl
     */
    public AVL getAvl() {
        return avl;
    }

    public static class Description implements Serializable{
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

    @XmlElement(name="aero")
    public Aero getAero() {
        return aero;
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
