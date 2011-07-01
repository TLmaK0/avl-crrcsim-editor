/*
 * CRRCsimEditor.java
 */

package com.abajar.crrcsimeditor;

import com.sun.j3d.utils.geometry.ColorCube;
import com.sun.j3d.utils.universe.SimpleUniverse;
import java.awt.BorderLayout;
import java.awt.GraphicsEnvironment;
import javax.media.j3d.Alpha;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.RotationInterpolator;
import javax.media.j3d.TransformGroup;
import javax.swing.JFrame;
import javax.vecmath.Point3d;
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
        JFrame frame = new MainFrame();
        frame.setSize(640, 480);
        frame.setLayout(new BorderLayout());

        Canvas3D canvas = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
        frame.add("Center", canvas);

        SimpleUniverse univ = new SimpleUniverse(canvas);
        univ.getViewingPlatform().setNominalViewingTransform();

        BranchGroup scene = createSceneGraph();
        scene.compile();
        univ.addBranchGraph(scene);

        frame.setVisible(true);


    }

    /**
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our application come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     */
    @Override protected void configureWindow(java.awt.Window root) {
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

    private static BranchGroup createSceneGraph() {
        // Make a scene graph branch
        BranchGroup branch = new BranchGroup();

        // Make a changeable 3D transform
        TransformGroup trans = new TransformGroup();
        trans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        branch.addChild(trans);

        // Make a shape
        ColorCube demo = new ColorCube(0.4);
        trans.addChild(demo);

        // Make a behavor to spin the shape
        Alpha spinAlpha = new Alpha(-1, 4000);
        RotationInterpolator spinner = new RotationInterpolator(spinAlpha,
            trans);
        spinner.setSchedulingBounds(new BoundingSphere(new Point3d(), 1000.0));
        trans.addChild(spinner);

        return branch;
    }
}
