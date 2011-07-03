/*
 * CRRCsimEditor.java
 */

package com.abajar.crrcsimeditor;

import com.sun.j3d.loaders.Scene;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;
import java.awt.BorderLayout;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.j3d.AmbientLight;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.swing.JFrame;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

/**
 * The main class of the application.
 */
public class CRRCsimEditor extends SingleFrameApplication {

    /**
     * At startup create and show the main frame of the application.
     */
    @Override protected void startup() {
        try {
            JFrame frame = new MainFrame();
            frame.setSize(640, 480);
            frame.setLayout(new BorderLayout());
            Canvas3D canvas = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
            frame.add("Center", canvas);
            SimpleUniverse univ = new SimpleUniverse(canvas);
            canvas.getView().setBackClipDistance(20000);
            univ.getViewingPlatform().setNominalViewingTransform();
            TransformGroup VpTG = univ.getViewingPlatform().getViewPlatformTransform();
            Transform3D Trfcamera = new Transform3D();
            Trfcamera.setTranslation(new Vector3f(50f, 20f, 20000f));
            VpTG.setTransform(Trfcamera);
            Scene model3d = new com.microcrowd.loader.java3d.max3ds.Loader3DS().load("Tank_BRDM3_N280611.3DS");
            BranchGroup scene = model3d.getSceneGroup();
            scene.compile();
            univ.addBranchGraph(scene);
            frame.setVisible(true);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CRRCsimEditor.class.getName()).log(Level.SEVERE, null, ex);
        }
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

}
