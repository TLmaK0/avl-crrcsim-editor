/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.abajar.crrcsimeditor.view.avl;

import java.lang.annotation.Annotation;
import com.abajar.crrcsimeditor.avl.AVLGeometry;
import com.abajar.crrcsimeditor.avl.geometry.Body;
import com.abajar.crrcsimeditor.avl.geometry.Control;
import com.abajar.crrcsimeditor.avl.AVL;
import com.abajar.crrcsimeditor.avl.geometry.Section;
import com.abajar.crrcsimeditor.avl.geometry.Surface;
import com.abajar.crrcsimeditor.crrcsim.CRRCSim.Change;
import javax.swing.tree.DefaultTreeModel;
import com.abajar.crrcsimeditor.crrcsim.CRRCSim;
import com.abajar.crrcsimeditor.avl.mass.MassObject;
import com.abajar.crrcsimeditor.avl.mass.Mass;
import com.abajar.crrcsimeditor.view.annotations.CRRCSimEditor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.EnumSet;
import javax.swing.tree.DefaultMutableTreeNode;
import static java.util.EnumSet.of;

/**
 *
 * @author hfreire
 */
public class SelectorMutableTreeNode  extends DefaultMutableTreeNode{
    public static DefaultTreeModel generateTreeNode(CRRCSim crrcsim){
        SelectorMutableTreeNode airplaneNode = new SelectorMutableTreeNode(crrcsim);

        AVL avl = crrcsim.getAvl();

        SelectorMutableTreeNode avlNode = new SelectorMutableTreeNode(avl);
        airplaneNode.add(avlNode);

        for(Change change : crrcsim.getChangelog()){
            airplaneNode.add(new SelectorMutableTreeNode(change));
        }

        SelectorMutableTreeNode geometryNode = new SelectorMutableTreeNode(avl.getGeometry());
        avlNode.add(geometryNode);

        for(Surface surf : avl.getGeometry().getSurfaces()){
            SelectorMutableTreeNode surfNode = new SelectorMutableTreeNode(surf);
            geometryNode.add(surfNode);

            for(Section section:surf.getSections()){
                SelectorMutableTreeNode sectionNode = new SelectorMutableTreeNode(section);
                surfNode.add(sectionNode);

                for(Control control:section.getControls()){
                    SelectorMutableTreeNode controlNode = new SelectorMutableTreeNode(control);
                    sectionNode.add(controlNode);
                }
            }
        }

        for(Body body : avl.getGeometry().getBodies()){
            SelectorMutableTreeNode bodyNode = new SelectorMutableTreeNode(body);
            geometryNode.add(bodyNode);
        }


        return new DefaultTreeModel(airplaneNode);
    }

    public static SelectorMutableTreeNode createNode(Object parentTreeNode, SelectorMutableTreeNode.TYPES type){
        switch(type){
            case SECTION:
                return new SelectorMutableTreeNode(((Surface)parentTreeNode).createSection());
            case SURFACE:
                return new SelectorMutableTreeNode(((AVLGeometry)parentTreeNode).createSurface());
            case CONTROL:
                return new SelectorMutableTreeNode(((Section)parentTreeNode).createControl());
            case MASS:
                return new SelectorMutableTreeNode(((MassObject)parentTreeNode).createMass());
            case BODY:
                return new SelectorMutableTreeNode(((AVLGeometry)parentTreeNode).createBody());
            case CHANGE:
                return new SelectorMutableTreeNode(((CRRCSim)parentTreeNode).createChange());
            default:
                throw new UnsupportedOperationException("Node of type " + type + " not suported");
        }
    }
    
    private List<ENABLE_BUTTONS> extractOptions(Object obj) {
        ArrayList<ENABLE_BUTTONS> newOptions = new ArrayList();
        Class objClass = obj.getClass();
        if (objClass.isAnnotationPresent(CRRCSimEditor.class)) {
            CRRCSimEditor crrcsimAnnotations = (CRRCSimEditor) objClass.getAnnotation(CRRCSimEditor.class);
            newOptions.addAll(Arrays.asList(crrcsimAnnotations.buttons()));
        }
        return newOptions;
    }

    public enum TYPES {
        SURFACE,
        BODY,
        SECTION,
        CONTROL,
        MASS,
        CHANGE
    }

    public enum ENABLE_BUTTONS {
        ADD_SURFACE,
        ADD_BODY,
        ADD_SECTION,
        ADD_CONTROL,
        ADD_MASS,
        DELETE,
        ADD_CHANGELOG
    }

    public SelectorMutableTreeNode(Object obj){
        super(obj);
    }

    public SelectorMutableTreeNode(Mass mass){
        super(mass);
        this.options = this.extractOptions(mass);
    }

    public SelectorMutableTreeNode(MassObject object) {
        super(object);
        this.options = this.extractOptions(object);
        for(Mass mass : object.getMasses()){
            this.add(new SelectorMutableTreeNode(mass));
        }
    }



    private List<ENABLE_BUTTONS> options = new ArrayList();

    /**
     * @return the options
     */
    public List<ENABLE_BUTTONS> getOptions() {
        return options;
    }
}
