/*
 * CRRCsimEditor.java
 */

package com.abajar.crrcsimeditor;

import com.abajar.crrcsimeditor.avl.AVL;
import com.abajar.crrcsimeditor.avl.AVLS;
import com.abajar.crrcsimeditor.crrcsim.CRRCSim;
import com.abajar.crrcsimeditor.crrcsim.CRRCSimFactory;
import com.abajar.crrcsimeditor.crrcsim.CRRCSimRepository;
//import com.microcrowd.loader.java3d.max3ds.Loader3DS;
//import com.sun.j3d.loaders.Scene;
//import com.sun.j3d.utils.universe.SimpleUniverse;
import com.abajar.crrcsimeditor.crrcsim.MetersConversor;
import com.abajar.crrcsimeditor.crrcsim.MultiUnit;
import java.awt.BorderLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.View;
import javax.vecmath.AxisAngle4f;
import javax.vecmath.Vector3f;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

/**
 * The main class of the application.
 */
public class CRRCsimEditor extends SingleFrameApplication {
    static final Logger logger = Logger.getLogger(CRRCsimEditor.class.getName());

//    SimpleUniverse univ;
    CRRCSim crrcsim;
    MainFrame frame;

    static final String CONFIGURATION_ROOT = System.getProperty("user.home") + "/.crrcsimeditor";
    static final String CONFIGURATION_PATH = CONFIGURATION_ROOT + "/configuration.xml";
    Properties configuration;

    public CRRCsimEditor() {
        File dir = new File(CONFIGURATION_ROOT);
        if (!dir.exists()) dir.mkdir();

        crrcsim = new CRRCSimFactory().create();
        configuration = new Properties();
        try {
            configuration.loadFromXML(new FileInputStream(CONFIGURATION_PATH));
        } catch (IOException ex) {
            //Config file doesn't exists
            logger.log(Level.INFO, "Config file doesn't exists");
        }

        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run(){
                try {
                    configuration.storeToXML(new FileOutputStream(CONFIGURATION_PATH), "Configuration file");
                } catch (Exception ex) {
                    logger.log(Level.FINE, null, ex);
                }
            }
        });
    }

    



    /**
     * At startup create and show the main frame of the application.
     */
    @Override protected void startup() {
//        try {
            frame = new MainFrame(this);
            
/*            Canvas3D canvas = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
            frame.jPanel1.setLayout(new BorderLayout());
            frame.jPanel1.setOpaque(false);
            frame.jPanel1.add("Center", canvas);

            univ = new SimpleUniverse(canvas);
            View view = canvas.getView();
            view.setProjectionPolicy(View.PARALLEL_PROJECTION);
            view.setScreenScalePolicy(View.SCALE_EXPLICIT);
            view.setScreenScale(0.004);
            univ.getViewingPlatform().setNominalViewingTransform();

            showRightView();

            Loader3DS loader3ds = new Loader3DS();
            Scene model3d = loader3ds.load(getClass().getClassLoader().getResource("model/EF2000.3ds"));
            BranchGroup scene = model3d.getSceneGroup();
            scene.compile();
            univ.addBranchGraph(scene);
 *
 */
            frame.setVisible(true);


            updateEnabledEditExportAsCRRCsimMenuItem();
 /*       } catch (FileNotFoundException ex) {
            logger.log(Level.FINE, null, ex);
        }
  *
  */
    }
/*
    public void showFrontView(){
        TransformGroup VpTG = univ.getViewingPlatform().getViewPlatformTransform();
        Transform3D Trfcamera = new Transform3D();

        Trfcamera.setRotation(new AxisAngle4f(new Vector3f(0f, 1f, 0f), (float)Math.PI/2 ));
        Trfcamera.setTranslation(new Vector3f(100f, 0f, 0f));
        VpTG.setTransform(Trfcamera);
    }

    public void showTopView(){
        TransformGroup VpTG = univ.getViewingPlatform().getViewPlatformTransform();
        Transform3D Trfcamera = new Transform3D();

        Trfcamera.setRotation(new AxisAngle4f(new Vector3f(-1f, 0f, 0f), (float)Math.PI/2 ));
        Trfcamera.setTranslation(new Vector3f(0f, 100f, 0f));
        VpTG.setTransform(Trfcamera);
    }

    public void showRightView(){
        TransformGroup VpTG = univ.getViewingPlatform().getViewPlatformTransform();
        Transform3D Trfcamera = new Transform3D();

        Trfcamera.setRotation(new AxisAngle4f(new Vector3f(0f, 0f, 0f), 0 ));
        Trfcamera.setTranslation(new Vector3f(0f, 0f, 100f));
        VpTG.setTransform(Trfcamera);
    }
*/

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
    public static void main(String[] args) throws IOException {
        LogManager.getLogManager().readConfiguration();
        launch(CRRCsimEditor.class, args);
    }

    public void exportAsAVL(Path avlFile) throws IOException {
        AVLS.avlToFile(this.crrcsim.getAvl(), avlFile, avlFile.getParent());
    }

    void saveAs(File file) throws IOException, JAXBException, InterruptedException, Exception {
        new CRRCSimRepository().storeToFile(file, crrcsim);
    }

    void open(File file) throws IOException, ClassNotFoundException, JAXBException {
        this.crrcsim = new CRRCSimRepository().restoreFromFile(file);
    }


    void openFile() {
        try {
                String path = this.configuration.getProperty("crrcsim.save", "~/");
                File file = this.frame.showOpenDialog(path, "CRRCsim editor file (*.crr)", "crr");
                this.configuration.setProperty("crrcsim.save",file.getAbsolutePath());
                this.open(file);
                this.frame.updateAVLTree();
        } catch (JAXBException ex) {
                logger.log(Level.FINE, null, ex);
        }catch (IOException ex) {
            logger.log(Level.FINE, null, ex);
        } catch (ClassNotFoundException ex) {
            logger.log(Level.FINE, null, ex);
        }
    }

    void saveFile() throws InterruptedException, Exception {
        try {
            String path = this.configuration.getProperty("crrcsim.save", "~/");
            File file = this.frame.showSaveDialog(path, "CRRCsim editor file (*.crr)", "crr");
            this.configuration.setProperty("crrcsim.save",file.getAbsolutePath());
            this.saveAs(file);
        }catch (JAXBException ex) {
             logger.log(Level.SEVERE, "Error serializing file", ex);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Error saving file", ex);
        }
    }

    void exportAsAVL() {
        try {
            String path = this.configuration.getProperty("crrcsim.save", "~/");
            File file = this.frame.showSaveDialog(path, "AVL file (*.avl)","avl");
            this.configuration.setProperty("crrcsim.save",file.getAbsolutePath());

            this.exportAsAVL(Paths.get(file.getPath()));
        } catch (IOException ex) {
            logger.log(Level.FINE, null, ex);
        }
    }

    void exportAsCRRCsim() {
        try {
            String path = this.configuration.getProperty("crrcsim.save", "~/");
            File file = this.frame.showSaveDialog(path, "CRRCsim file (*.xml)", "xml");
            this.configuration.setProperty("crrcsim.save",file.getAbsolutePath());
            this.exportAsCRRCsim(file, Paths.get(path));
        } catch (InterruptedException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    private void exportAsCRRCsim(File file, Path originPath) throws IOException, InterruptedException{
        try {
            this.crrcsim.calculate(this.configuration.getProperty("avl.path"), file.toPath().getParent());
            AVL avl = this.crrcsim.getAvl();

            FileOutputStream fos = new FileOutputStream(file);
            JAXBContext context = JAXBContext.newInstance(CRRCSim.class);
            Marshaller m = context.createMarshaller();
            m.setAdapter(new MetersConversor(new MultiUnit(avl.getLengthUnit(), avl.getMassUnit(), avl.getTimeUnit())));
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            m.marshal(this.crrcsim, fos);
            fos.close();
        } catch (Exception ex) {
ex.printStackTrace();
            logger.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    void setAvlExecutable() {
        String path = this.configuration.getProperty("avl.path", "~/");
        File file = this.frame.showOpenDialog(path, "AVL executable");
        this.configuration.setProperty("avl.path", file.getAbsolutePath());
        updateEnabledEditExportAsCRRCsimMenuItem();
        try {
            this.configuration.storeToXML(new FileOutputStream(CONFIGURATION_PATH), null);
        } catch (IOException ex) {
            logger.log(Level.FINE, null, ex);
        }
    }

    private void updateEnabledEditExportAsCRRCsimMenuItem() {
        this.frame.getFileExportAsCRRsimMenuItem().setEnabled(this.configuration.getProperty("avl.path")!=null);
    }


}
