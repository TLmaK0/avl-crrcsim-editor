/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.abajar.crrcsimeditor.avl;

import com.abajar.crrcsimeditor.avl.mass.Mass;
import com.abajar.crrcsimeditor.avl.mass.MassObject;
import com.abajar.crrcsimeditor.avl.geometry.Body;
import com.abajar.crrcsimeditor.avl.geometry.Control;
import com.abajar.crrcsimeditor.avl.geometry.Section;
import com.abajar.crrcsimeditor.avl.geometry.Surface;
import com.abajar.crrcsimeditor.view.annotations.CRRCSimEditor;
import com.abajar.crrcsimeditor.view.annotations.CRRCSimEditorField;
import com.abajar.crrcsimeditor.view.annotations.CRRCSimEditorNode;
import com.abajar.crrcsimeditor.view.avl.SelectorMutableTreeNode.ENABLE_BUTTONS;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 *
 * @author hfreire
 */
@XmlRootElement
@XmlSeeAlso({
    MassObject.class
})
@CRRCSimEditor(buttons={ENABLE_BUTTONS.ADD_SURFACE, ENABLE_BUTTONS.ADD_MASS, ENABLE_BUTTONS.ADD_BODY})
public class AVLGeometry extends MassObject implements AVLSerializable{
    final static Logger logger = Logger.getLogger(AVLGeometry.class.getName());


    @CRRCSimEditorField(text="Name",
        help="Project name"
    )
    private String name = "Geometry";

    @CRRCSimEditorField(text="Mach",
        help="default freestream Mach number for Prandtl-Glauert correction. http://en.wikipedia.org/wiki/Mach_number. 0 should be ok for RC airplanes"
    )
    private float Mach;


    @CRRCSimEditorField(text="iYsym",
        help="Allow you to draw only a part of the airplane\r\n"
            + "and left AVL to mirror about Y axis\r\n"
            + "1 case is symmetric about Y=0, (X-Z plane is a solid wall)\r\n"
            + "-1  case is antisymmetric about Y=0, (X-Z plane is at const. Cp)\r\n"
            + "0  no Y-symmetry is assumed\r\n"
            + "use 0 by default and draw all elements"
    )
    private  int iYsym;

    @CRRCSimEditorField(text="iZsym",
        help="Allow you to draw only a part of the airplane\r\n"
            + "and left AVL to mirror about Z axis"
            + "1  case is symmetric about Z=Zsym, (X-Y plane is a solid wall)\r\n"
            + "-1  case is antisymmetric about Z=Zsym, (X-Y plane is at const. Cp)\r\n"
            + "0  no Z-symmetry is assumed (Zsym ignored)\r\n"
            + "use 0 by default and draw all elements"
    )
    private  int iZsym;

    @CRRCSimEditorField(text="Zsym",
        help="Zsym"
    )
    private float Zsym;


    @CRRCSimEditorField(text="Sref",
        help="reference area used to define all coefficients (CL, CD, Cm, etc)\r\n"
            + "the area of the wing in square units"
    )
    private  float Sref;

    @CRRCSimEditorField(text="Cref",
        help="reference chord used to define pitching moment (Cm).\r\n"
            + "the chord of the wing http://en.wikipedia.org/wiki/Chord_(aircraft)"
    )
    private  float Cref;

    @CRRCSimEditorField(text="Bref",
        help="reference span  used to define roll,yaw moments\r\n"
            + "the wing span http://en.wikipedia.org/wiki/Wingspan"
    )
    private  float Bref;


    @CRRCSimEditorField(text="Xref",
        help="default location about which moments and rotation rates are defined\r\n"
            + "Center of Gravity X axis position"
    )
    private  float Xref;

    @CRRCSimEditorField(text="Yref",
        help="default location about which moments and rotation rates are defined\r\n"
            + "Center of Gravity Y axis position"
    )
    private  float Yref;

    @CRRCSimEditorField(text="Zref",
        help="default location about which moments and rotation rates are defined\r\n"
            + "Center of Gravity Z axis position"
    )
    private  float Zref;

    
    @CRRCSimEditorField(text="CDp",
        help="default profile drag coefficient added to geometry, applied at XYZref\r\n"
            + "http://en.wikipedia.org/wiki/Drag_coefficient\r\n"
            + "0.020 seems to be a common default value"
    )
    private float CDp = 0.02f;
    
    private final ArrayList<Surface> surfaces = new ArrayList<Surface>();
    private final ArrayList<Body> body = new ArrayList<Body>();

    static final long serialVersionUID = 7590357473387179207L;

    
    /**
     * @return the Mach
     */
    public float getMach() {
        return Mach;
    }

    /**
     * @param Mach the Mach to set
     */
    public void setMach(float Mach) {
        this.Mach = Mach;
    }
    
    /**
     * @return the CDp
     */
    public float getCDp() {
        return CDp;
    }

    /**
     * @return the surfaces
     */
    @XmlElementWrapper
    @XmlElement(name="surface")
    @CRRCSimEditorNode
    public ArrayList<Surface> getSurfaces() {
        return surfaces;
    }

    /**
     * @return the body
     */
    @XmlElement(name="body")
    public ArrayList<Body> getBodies() {
        return body;
    }

    @Override
    public String toString() {
        return this.name;
    }

    /**
     * @param CDp the CDp to set
     */
    public void setCDp(float CDp) {
        this.CDp = CDp;
    }

    @Override
    public void writeAVLData(OutputStream out) {
        PrintStream ps = new PrintStream(out);
        ps.print("#Created with CRRCsimEditor http://sourceforge.net/projects/crrcsimeditor/ \n");
        ps.printf(locale, "%1$s\n", this.getName());
        ps.printf(locale, "#Mach\n%1$-19.4g\n", this.getMach());                                                         //0.0                 | Mach
        ps.printf(locale, "#iYsym   iZsym    Zsym\n" + formatInteger(2) + formatFloat(1,3) + "\n", this.getiYsym(), (int)this.getiZsym(), (int)this.getZsym());          //1     0     0.0     | iYsym  iZsym  Zsym
        ps.printf(locale, "#Sref     Cref      Bref\n" + formatFloat(3) + "\n", this.getSref(), this.getCref(), this.getBref());          //4.0   0.4   0.1     | Sref   Cref   Bref
        ps.printf(locale, "#Xref     Yref      Zref\n" + formatFloat(3) + "\n", this.getXref(), this.getYref(), this.getZref());          //0.1   0.0   0.0     | Xref   Yref   Zref
        
        if(this.CDp != 0){
            ps.printf(locale, "#CDp\n%1$-19.4g\n", this.CDp);                                                         //0.020               | CDp  (optional)
        }
        
        for(Surface surf : this.getSurfaces()){
            surf.writeAVLData(out);
        }

        for(Body body : this.getBodies()){
            body.writeAVLData(out);
        }
    }

    @Override
    public void writeAVLMassData(OutputStream out) {
        
        super.writeAVLMassData(out);
    }



    public static String formatInteger(int numberOfValues, int startValue){
        String format ="";
        for(int n=startValue; n < startValue + numberOfValues; n++){
            format += "%" + n + "$-8d ";
        }
        return format;
    }

    public static String formatInteger(int numberOfValues){
        return formatInteger(numberOfValues, 1);
    }

    public static String formatFloat(int numberOfValues){
        return formatFloat(numberOfValues, 1);
    }

    public static String formatFloat(int numberOfValues, int startValue){
        String format ="";
        for(int n=startValue; n < startValue + numberOfValues; n++){
            format += "%" + n + "$-9s ";
        }
        return format;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the Zsym
     */
    public float getZsym() {
        return Zsym;
    }

    /**
     * @param Zsym the Zsym to set
     */
    public void setZsym(float Zsym) {
        this.Zsym = Zsym;
    }

    /**
     * @return the iYsym
     */
    public int getiYsym() {
        return iYsym;
    }

    /**
     * @param iYsym the iYsym to set
     */
    public void setiYsym(int iYsym) {
        this.iYsym = iYsym;
    }

    /**
     * @return the iZsym
     */
    public int getiZsym() {
        return iZsym;
    }

    /**
     * @param iZsym the iZsym to set
     */
    public void setiZsym(int iZsym) {
        this.iZsym = iZsym;
    }

    /**
     * @return the Sref
     */
    public float getSref() {
        return Sref;
    }

    /**
     * @param Sref the Sref to set
     */
    public void setSref(float Sref) {
        this.Sref = Sref;
    }

    /**
     * @return the Cref
     */
    public float getCref() {
        return Cref;
    }

    /**
     * @param Cref the Cref to set
     */
    public void setCref(float Cref) {
        this.Cref = Cref;
    }

    /**
     * @return the Bref
     */
    public float getBref() {
        return Bref;
    }

    /**
     * @param Bref the Bref to set
     */
    public void setBref(float Bref) {
        this.Bref = Bref;
    }

    /**
     * @return the Xref
     */
    public float getXref() {
        return Xref;
    }

    /**
     * @param Xref the Xref to set
     */
    public void setXref(float Xref) {
        this.Xref = Xref;
    }

    /**
     * @return the Yref
     */
    public float getYref() {
        return Yref;
    }

    /**
     * @param Yref the Yref to set
     */
    public void setYref(float Yref) {
        this.Yref = Yref;
    }

    /**
     * @return the Zref
     */
    public float getZref() {
        return Zref;
    }

    /**
     * @param Zref the Zref to set
     */
    public void setZref(float Zref) {
        this.Zref = Zref;
    }

    int getAileronPosition() throws Exception {
        return getControlPosition(Control.AILERON);
    }

    int getElevatorPosition() throws Exception {
        return getControlPosition(Control.ELEVATOR);
    }

    int getRudderPosition() throws Exception {
        return getControlPosition(Control.RUDDER);
    }

    private int getControlPosition(int controlType) throws Exception{
        ArrayList<Integer> controls = new ArrayList<Integer>();
        for(Surface surface: this.getSurfaces()){
            for(Section section: surface.getSections()){
                for(Control control: section.getControls()){
                    if (!controls.contains(control.getType())){
                        if (control.getType() == controlType) {
                            int position = controls.size();
                            logger.log(Level.FINE, "Control {0} found at {1}", new Object[]{controlType, position});
                            return position;
                        }
                        controls.add(control.getType());
                    }
                }
            }
        }
        return -1;
    }

    public Surface createSurface() {
        Surface surface = new Surface();
        this.getSurfaces().add(surface);
        return surface;
    }

    public Body createBody() {
        Body body = new Body();
        this.getBodies().add(body);
        return body;
    }
}
