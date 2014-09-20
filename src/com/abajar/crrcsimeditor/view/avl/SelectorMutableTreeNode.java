/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.abajar.crrcsimeditor.view.avl;

import com.abajar.crrcsimeditor.crrcsim.Power.Engine;
import com.abajar.crrcsimeditor.crrcsim.Power.Data;
import com.abajar.crrcsimeditor.crrcsim.Power.Shaft;
import com.abajar.crrcsimeditor.crrcsim.Power;
import com.abajar.crrcsimeditor.crrcsim.Power.Battery;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import com.abajar.crrcsimeditor.view.annotations.CRRCSimEditorNode;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.tree.TreeModel;
import com.abajar.crrcsimeditor.crrcsim.Config;
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
    public static TreeModel generateTree(Object obj){
        return new DefaultTreeModel(generateTreeNode(obj));
    }

    private static SelectorMutableTreeNode generateTreeNode(Object obj){
        SelectorMutableTreeNode node = new SelectorMutableTreeNode(obj);

        for(Method method : obj.getClass().getDeclaredMethods()){
            if(method.isAnnotationPresent(CRRCSimEditorNode.class)){
                try {
                    Object childObj = method.invoke(obj);
                    if (childObj instanceof List<?>) {
                        for(Object childObjItem : (List)childObj){
                            node.add(generateTreeNode(childObjItem));
                        }
                    }else node.add(generateTreeNode(childObj));
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(SelectorMutableTreeNode.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalArgumentException ex) {
                    Logger.getLogger(SelectorMutableTreeNode.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InvocationTargetException ex) {
                    Logger.getLogger(SelectorMutableTreeNode.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return node;
    }

    public static SelectorMutableTreeNode createNode(Object parentTreeNode, SelectorMutableTreeNode.TYPES type){
        switch(type){
            case SECTION:
                return generateTreeNode(((Surface)parentTreeNode).createSection());
            case SURFACE:
                return generateTreeNode(((AVLGeometry)parentTreeNode).createSurface());
            case CONTROL:
                return generateTreeNode(((Section)parentTreeNode).createControl());
            case MASS:
                return generateTreeNode(((MassObject)parentTreeNode).createMass());
            case BODY:
                return generateTreeNode(((AVLGeometry)parentTreeNode).createBody());
            case CHANGE:
                return generateTreeNode(((CRRCSim)parentTreeNode).createChange());
            case BATTERY:
                return generateTreeNode(((Power)parentTreeNode).createBattery());
            case SHAFT:
                return generateTreeNode(((Battery)parentTreeNode).createShaft());
            case ENGINE:
                return generateTreeNode(((Shaft)parentTreeNode).createEngine());
            case DATA:
                return generateTreeNode(((Engine)parentTreeNode).createData());
            case DATA_IDLE:
                return generateTreeNode(((Engine)parentTreeNode).createDataIdle());
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
        CHANGE,
        CONFIG,
        BATTERY
    ,   SHAFT, ENGINE, DATA, DATA_IDLE
    ,   PROPELLER}

    public enum ENABLE_BUTTONS {
        ADD_SURFACE,
        ADD_BODY,
        ADD_SECTION,
        ADD_CONTROL,
        ADD_MASS,
        ADD_CHANGELOG,
        ADD_CONFIG,
        ADD_SOUND,
        ADD_BATTERY,
        ADD_SHAFT,
        ADD_ENGINE,
        ADD_DATA,
        ADD_DATA_IDLE,
        DELETE
    }

    public SelectorMutableTreeNode(Object obj){
        super(obj);
        
        if (obj.getClass().isAnnotationPresent(CRRCSimEditor.class)) this.options = this.extractOptions(obj);
        
        if (MassObject.class.isAssignableFrom(obj.getClass())){
            for(Mass mass : ((MassObject)obj).getMasses()){
                this.add(new SelectorMutableTreeNode(mass));
            }
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
