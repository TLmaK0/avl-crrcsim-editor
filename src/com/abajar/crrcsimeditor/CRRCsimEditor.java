/*
 * CRRCsimEditor.java
 */

package com.abajar.crrcsimeditor;

import com.sun.j3d.loaders.Scene;
import com.sun.j3d.utils.universe.SimpleUniverse;
import java.awt.BorderLayout;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        JFrame frame = new MainFrame();
        frame.setSize(640, 480);
        frame.setLayout(new BorderLayout());

        Canvas3D canvas = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
        frame.add("Center", canvas);

        SimpleUniverse univ = new SimpleUniverse(canvas);
        univ.getViewingPlatform().setNominalViewingTransform();
        BranchGroup scene = createSceneGraph();
addLights(scene);

        scene.compile();
        univ.addBranchGraph(scene);

        TransformGroup VpTG = univ.getViewingPlatform().getViewPlatformTransform();

  Transform3D Trfcamera = new Transform3D();
  Trfcamera.setTranslation(new Vector3f(20f, 10f, 110f));
  Trfcamera.rotX(0.5);
  Trfcamera.rotY(0.3);
  Trfcamera.setTranslation(new Vector3f(60f, 10f, 40f));
  VpTG.setTransform( Trfcamera );
        
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
//        BranchGroup branch = new BranchGroup();
//
//        // Make a changeable 3D transform
//        TransformGroup trans = new TransformGroup();
//        trans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
//        branch.addChild(trans);
        Scene model3d = null;

        try {
            model3d = new com.microcrowd.loader.java3d.max3ds.Loader3DS().load("Bounce.3DS");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CRRCsimEditor.class.getName()).log(Level.SEVERE, null, ex);
        }

        BranchGroup branch = model3d.getSceneGroup();
        // Make a shape
        //ColorCube demo = new ColorCube(0.4);
        //trans.addChild(demo);


        // Make a behavor to spin the shape
//        Alpha spinAlpha = new Alpha(-1, 4000);
//        RotationInterpolator spinner = new RotationInterpolator(spinAlpha,
//            trans);
//        spinner.setSchedulingBounds(new BoundingSphere(new Point3d(), 1000.0));
//        trans.addChild(spinner);

        return branch;
    }

    public void addLights( BranchGroup bg )
 {
  // create the color for the light
  Color3f color = new Color3f( 1.0f,1.0f,0.0f );


  // create a vector that describes the direction that
  // the light is shining.
  Vector3f direction  = new Vector3f( -1.0f,-1.0f,-1.0f );


  // create the directional light with the color and direction
  DirectionalLight light = new DirectionalLight( color, direction );


  // set the volume of influence of the light.
  // Only objects within the Influencing Bounds
  // will be illuminated.
  light.setInfluencingBounds( new BoundingSphere( new Point3d(0.0,0.0,0.0), 200.0 ) );


  // add the light to the BranchGroup
  bg.addChild( light );
 }
}
