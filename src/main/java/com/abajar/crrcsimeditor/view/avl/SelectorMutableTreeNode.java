/*
 * Copyright (C) 2015  Hugo Freire Gil
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 */

package com.abajar.crrcsimeditor.view.avl;

import com.abajar.crrcsimeditor.crrcsim.Shaft;
import com.abajar.crrcsimeditor.crrcsim.Power;
import com.abajar.crrcsimeditor.view.annotations.CRRCSimEditorNode;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.tree.TreeModel;
import com.abajar.crrcsimeditor.avl.AVLGeometry;
import com.abajar.crrcsimeditor.avl.geometry.Section;
import com.abajar.crrcsimeditor.avl.geometry.Surface;
import javax.swing.tree.DefaultTreeModel;
import com.abajar.crrcsimeditor.crrcsim.CRRCSim;
import com.abajar.crrcsimeditor.avl.mass.MassObject;
import com.abajar.crrcsimeditor.avl.mass.Mass;
import com.abajar.crrcsimeditor.crrcsim.Battery;
import com.abajar.crrcsimeditor.crrcsim.Engine;
import com.abajar.crrcsimeditor.view.annotations.CRRCSimEditor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.tree.DefaultMutableTreeNode;

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
            case SIMPLE_TRUST:
                return generateTreeNode(((Shaft)parentTreeNode).createSimpleTrust());
            case WHEEL:
                return generateTreeNode(((CRRCSim)parentTreeNode).createWheel());
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
    ,   PROPELLER, SIMPLE_TRUST, WHEEL}

    private static final TreeModificator addSurface = new AddSurface();
    private static final TreeModificator addBody = new AddBody();
    private static final TreeModificator addSection = new AddSection();
    private static final TreeModificator addControl = new AddControl();
    private static final TreeModificator addMass = new AddMass();
    private static final TreeModificator addChangeLog = new AddChangeLog();
    //private static final TreeModificator addConfig = new AddConfig();
    //private static final TreeModificator addSound = new AddSound();
    private static final TreeModificator addBattery = new AddBattery();
    private static final TreeModificator addShaft = new AddShaft();
    private static final TreeModificator addEngine = new AddEngine();
    private static final TreeModificator addData = new AddData();
    private static final TreeModificator addDataIdle = new AddDataIdle();
    private static final TreeModificator addSimpleTrust = new AddSimpleTrust();
    private static final TreeModificator addWheel = new AddCollisionPoint();
    private static final TreeModificator addCollisionPoint = new AddCollisionPoint();
    private static final TreeModificator delete = new Delete();

    public enum ENABLE_BUTTONS {
        ADD_SURFACE         (addSurface),
        ADD_BODY            (addBody),
        ADD_SECTION         (addSection),
        ADD_CONTROL         (addControl),
        ADD_MASS            (addMass),
        ADD_CHANGELOG       (addChangeLog),
        //ADD_CONFIG          (addConfig),
        //ADD_SOUND           (addSound),
        ADD_BATTERY         (addBattery),
        ADD_SHAFT           (addShaft),
        ADD_ENGINE          (addEngine),
        ADD_DATA            (addData),
        ADD_DATA_IDLE       (addDataIdle),
        ADD_SYMPLE_TRUST    (addSimpleTrust),
        ADD_WHEEL           (addWheel),
        ADD_COLLISION_POINT (addCollisionPoint),
        DELETE              (delete);

        private final TreeModificator modificator;

        ENABLE_BUTTONS(TreeModificator modificator){
          this.modificator = modificator;
        }

        public void click(Object node, Object parent){
          this.modificator.modify(node, parent);
        }
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

