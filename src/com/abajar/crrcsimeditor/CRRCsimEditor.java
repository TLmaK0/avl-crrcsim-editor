/*
 * CRRCsimEditor.java
 */

package com.abajar.crrcsimeditor;

import com.abajar.crrcsimeditor.avl.AVL;
import com.abajar.crrcsimeditor.avl.AVLGeometry;
import com.abajar.crrcsimeditor.avl.geometry.Body;
import com.abajar.crrcsimeditor.avl.geometry.Control;
import com.abajar.crrcsimeditor.avl.geometry.Section;
import com.abajar.crrcsimeditor.avl.geometry.Surface;
import com.abajar.crrcsimeditor.avl.mass.Mass;
import com.microcrowd.loader.java3d.max3ds.Loader3DS;
import com.sun.j3d.loaders.Scene;
import com.sun.j3d.utils.universe.SimpleUniverse;
import java.awt.BorderLayout;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.View;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.vecmath.AxisAngle4f;
import javax.vecmath.Vector3f;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

/**
 * The main class of the application.
 */
public class CRRCsimEditor extends SingleFrameApplication {

    SimpleUniverse univ;
    AVL avl;
    MainFrame frame;

    public CRRCsimEditor() {
        avl = new AVL();
    }

    



    /**
     * At startup create and show the main frame of the application.
     */
    @Override protected void startup() {
        try {
            frame = new MainFrame(this);
            frame.setSize(640, 480);
            frame.setLayout(new BorderLayout());
            Canvas3D canvas = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
            frame.add("Center", canvas);
    
            univ = new SimpleUniverse(canvas);
            View view = canvas.getView();
            view.setProjectionPolicy(View.PARALLEL_PROJECTION);
            view.setScreenScalePolicy(View.SCALE_EXPLICIT);
            view.setScreenScale(0.004);
            univ.getViewingPlatform().setNominalViewingTransform();

            rightView();
            
            Loader3DS loader3ds = new Loader3DS();
            Scene model3d = loader3ds.load("model\\EF2000.3ds");
            BranchGroup scene = model3d.getSceneGroup();
            scene.compile();
            univ.addBranchGraph(scene);
            frame.setVisible(true);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CRRCsimEditor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void frontView(){
        TransformGroup VpTG = univ.getViewingPlatform().getViewPlatformTransform();
        Transform3D Trfcamera = new Transform3D();

        Trfcamera.setRotation(new AxisAngle4f(new Vector3f(0f, 1f, 0f), (float)Math.PI/2 ));
        Trfcamera.setTranslation(new Vector3f(100f, 0f, 0f));
        VpTG.setTransform(Trfcamera);
    }

    public void topView(){
        TransformGroup VpTG = univ.getViewingPlatform().getViewPlatformTransform();
        Transform3D Trfcamera = new Transform3D();

        Trfcamera.setRotation(new AxisAngle4f(new Vector3f(-1f, 0f, 0f), (float)Math.PI/2 ));
        Trfcamera.setTranslation(new Vector3f(0f, 100f, 0f));
        VpTG.setTransform(Trfcamera);
    }

    public void rightView(){
        TransformGroup VpTG = univ.getViewingPlatform().getViewPlatformTransform();
        Transform3D Trfcamera = new Transform3D();

        Trfcamera.setRotation(new AxisAngle4f(new Vector3f(0f, 0f, 0f), 0 ));
        Trfcamera.setTranslation(new Vector3f(0f, 0f, 100f));
        VpTG.setTransform(Trfcamera);
    }

    public void showAvlEditor(){
        frame.showGeoEditor();
    }

    public AVL getAvl(){
        return this.avl;
    }

    /**
     * A convenient static getter for the application instance.
     * @return the instance of CRRCsimEditor
     */
    public static CRRCsimEditor getApplication() {
        return Application.getInstance(CRRCsimEditor.class);
    }

    /**
     * Main method launching the application.
     */
    public static void main(String[] args) {
        launch(CRRCsimEditor.class, args);
    }

    public Surface createNewSurfaceFor(AVLGeometry aVLGeometry) {
       Surface surface = new Surface();
       surface.setName("new surface");
       aVLGeometry.getSurfaces().add(surface);
       return surface;
    }

}
