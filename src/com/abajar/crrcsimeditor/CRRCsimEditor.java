/*
 * CRRCsimEditor.java
 */

package com.abajar.crrcsimeditor;

import com.abajar.crrcsimeditor.avl.AVL;
import com.abajar.crrcsimeditor.avl.AVLGeometry;
import com.abajar.crrcsimeditor.avl.connectivity.AvlRunner;
import com.abajar.crrcsimeditor.avl.geometry.Control;
import com.abajar.crrcsimeditor.avl.geometry.Section;
import com.abajar.crrcsimeditor.avl.geometry.Surface;
import com.abajar.crrcsimeditor.avl.mass.Mass;
import com.abajar.crrcsimeditor.avl.mass.MassObject;
import com.abajar.crrcsimeditor.avl.runcase.AvlCalculation;
import com.abajar.crrcsimeditor.crrcsim.Aero;
import com.microcrowd.loader.java3d.max3ds.Loader3DS;
import com.sun.j3d.loaders.Scene;
import com.sun.j3d.utils.universe.SimpleUniverse;
import java.awt.BorderLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
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
import javax.xml.bind.Unmarshaller;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

/**
 * The main class of the application.
 */
public class CRRCsimEditor extends SingleFrameApplication {

    SimpleUniverse univ;
    AVL avl;
    MainFrame frame;
    GeometryEditor geoEditor;

    static final String CONFIGURATION_ROOT = ".crrcsimeditor";
    static final String CONFIGURATION_PATH = CONFIGURATION_ROOT + "/configuration.xml";
    Properties configuration;

    public CRRCsimEditor() {
        File dir = new File(CONFIGURATION_ROOT);
        if (!dir.exists()) dir.mkdir();

        avl = new AVL();
        configuration = new Properties();
        try {
            configuration.loadFromXML(new FileInputStream(CONFIGURATION_PATH));
        } catch (IOException ex) {
            //Config file doesn't exists
            Logger.getLogger(CRRCsimEditor.class.getName()).log(Level.INFO, "Config file doesn't exists");
        }
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

            showRightView();
            
            Loader3DS loader3ds = new Loader3DS();
            Scene model3d = loader3ds.load(getClass().getClassLoader().getResource("model/EF2000.3ds"));
            BranchGroup scene = model3d.getSceneGroup();
            scene.compile();
            univ.addBranchGraph(scene);
            frame.setVisible(true);

            geoEditor =  new GeometryEditor(this);

            updateEnabledEditExportAsCRRCsimMenuItem();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CRRCsimEditor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

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

    public Surface createSurfaceFor(AVLGeometry aVLGeometry) {
       Surface surface = new Surface();
       surface.setName("new surface");
       aVLGeometry.getSurfaces().add(surface);
       return surface;
    }

    public Section createSectionFor(Surface surface) {
       Section section  = new Section();
       surface.getSections().add(section);
       return section;
    }

    public Control createControlFor(Section section) {
        Control control = new Control();
        control.setName("new control");
        section.getControls().add(control);
        return control;
    }

    public Mass createMassFor(MassObject massObject) {
        Mass mass = new Mass();
        mass.setName(massObject.toString());
        massObject.getMasses().add(mass);
        return mass;
    }


    public void exportAsAVL(File file) throws IOException {
        FileOutputStream fos = new FileOutputStream(file);
        this.avl.getGeometry().writeAVLData(fos);
        fos.close();

        String fileMassPath = file.getPath().replace(".avl", ".mass");
        File fileMass = new File(fileMassPath);
        fos = new FileOutputStream(fileMass);
        this.avl.getGeometry().writeAVLMassData(fos);
        fos.close();
    }

    void saveAs(File file) throws IOException, JAXBException {
        FileOutputStream fos = new FileOutputStream(file);
        
        JAXBContext context = JAXBContext.newInstance(AVLGeometry.class);

        Marshaller m = context.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        m.marshal(this.avl.getGeometry(), fos);
        fos.close();
    }

    void open(File file) throws IOException, ClassNotFoundException, JAXBException {
        FileInputStream fis = new FileInputStream(file);
        JAXBContext context = JAXBContext.newInstance(AVLGeometry.class);

        Unmarshaller u = context.createUnmarshaller();

        this.avl.setGeometry((AVLGeometry)u.unmarshal(fis));
        fis.close();
    }

    public void showGeoEditor(){
        geoEditor.setVisible(true);
    }

    void showAvlEditor() {
        this.geoEditor.setVisible(true);
    }

    void openFile() {
        try {
                this.open(this.frame.showOpenDialog("CRRCsim editor file (*.crr)", "crr"));
                this.geoEditor.updateAVLTree();
        } catch (JAXBException ex) {
                Logger.getLogger(CRRCsimEditor.class.getName()).log(Level.SEVERE, null, ex);
        }catch (IOException ex) {
            Logger.getLogger(CRRCsimEditor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CRRCsimEditor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void saveFile() {
        try {
            this.saveAs(this.frame.showSaveDialog("CRRCsim editor file (*.crr)", "crr"));
        }catch (JAXBException ex) {
             Logger.getLogger(CRRCsimEditor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CRRCsimEditor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void exportAsAVL() {
        try {
            this.exportAsAVL(this.frame.showSaveDialog("AVL file (*.avl)","avl"));
        } catch (IOException ex) {
            Logger.getLogger(CRRCsimEditor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void exportAsCRRCsim() {
        try {
            this.exportAsCRRCsim(this.frame.showSaveDialog("CRRCsim file (*.xml)", "xml"));
        } catch (InterruptedException ex) {
            Logger.getLogger(CRRCsimEditor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CRRCsimEditor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void exportAsCRRCsim(File file) throws IOException, InterruptedException{
        try {
            String tmpBaseName = "crrcsimtmp";
            String fileNameTmp =  tmpBaseName + ".avl";
            String fileNameTmpMass =  tmpBaseName + ".mass";

            File avlExportedFile = new File(CONFIGURATION_ROOT + "/" + fileNameTmp);
            avlExportedFile.delete();

            File massExportedFile = new File(CONFIGURATION_ROOT + "/" + fileNameTmpMass);
            massExportedFile.delete();


            this.exportAsAVL(avlExportedFile);

            AvlRunner avlRunner = new AvlRunner(this.configuration.getProperty("avl.path"), CONFIGURATION_ROOT, fileNameTmp);
            AvlCalculation calculation = avlRunner.getCalculation();

            //TODO: Select correct elevator, rudder, aileron
            Aero aero = new Aero(calculation, 1, 2, 0);

            FileOutputStream fos = new FileOutputStream(file);
            JAXBContext context = JAXBContext.newInstance(Aero.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            m.marshal(aero, fos);
            fos.close();
        } catch (JAXBException ex) {
            Logger.getLogger(CRRCsimEditor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void setAvlExecutable() {
        File file = this.frame.showOpenDialog("AVL executable");
        this.configuration.setProperty("avl.path", file.getAbsolutePath());
        updateEnabledEditExportAsCRRCsimMenuItem();
        try {
            this.configuration.storeToXML(new FileOutputStream(CONFIGURATION_PATH), null);
        } catch (IOException ex) {
            Logger.getLogger(CRRCsimEditor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void updateEnabledEditExportAsCRRCsimMenuItem() {
        this.frame.getFileExportAsCRRsimMenuItem().setEnabled(this.configuration.getProperty("avl.path")!=null);
    }


}
