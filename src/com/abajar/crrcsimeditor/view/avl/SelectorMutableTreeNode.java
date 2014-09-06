/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.abajar.crrcsimeditor.view.avl;

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

    private static MutableTreeNode generateTreeNode(Object obj){
        SelectorMutableTreeNode node = new SelectorMutableTreeNode(obj);

        for(Method method : obj.getClass().getDeclaredMethods()){
            if(method.isAnnotationPresent(CRRCSimEditorNode.class)){
                try {
                    Object childObj = method.invoke(obj);
                    node.add(generateTreeNode(childObj));
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
        CHANGE,
        CONFIG,
        SOUND
    }

    public enum ENABLE_BUTTONS {
        ADD_SURFACE,
        ADD_BODY,
        ADD_SECTION,
        ADD_CONTROL,
        ADD_MASS,
        ADD_CHANGELOG,
        ADD_CONFIG,
        ADD_SOUND,
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
