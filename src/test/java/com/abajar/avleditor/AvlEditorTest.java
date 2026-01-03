/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.abajar.avleditor;

import com.abajar.avleditor.crrcsim.CRRCSimRepository;
import com.abajar.avleditor.crrcsim.CRRCSim;
import java.nio.file.Files;
import java.nio.file.Paths;
import com.abajar.avleditor.avl.AVL;
import com.abajar.avleditor.avl.mass.Mass;
import com.abajar.avleditor.crrcsim.CRRCSimFactory;
import java.io.File;
import java.io.IOException;
import javax.xml.bind.JAXBException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import com.abajar.avleditor.avl.AVLGeometry;
import com.abajar.avleditor.avl.geometry.Surface;

/**
 *
 * @author hfreire
 */
public class AvlEditorTest {
    final File file = new File("test.crr");
    public AvlEditorTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws IOException {
        if (file.exists()) file.delete();
    }

    @After
    public void tearDown() throws IOException {
        if (file.exists()) file.delete();
    }

    @Test
    public void testMultipleSurfacesSaveAndLoad() throws Exception {
        // Create a new CRRCSim with multiple surfaces
        AVL avl = new AVL();
        AVLGeometry geometry = avl.getGeometry();

        // Geometry starts with 1 default surface
        assertEquals("Should start with 1 surface", 1, geometry.getSurfaces().size());
        geometry.getSurfaces().get(0).setName("Wing");

        // Add two more surfaces
        Surface hstab = geometry.createSurface();
        hstab.setName("Horizontal Stabilizer");

        Surface vstab = geometry.createSurface();
        vstab.setName("Vertical Stabilizer");

        assertEquals("Should have 3 surfaces before save", 3, geometry.getSurfaces().size());

        // Save
        CRRCSim crrcsim = new CRRCSimFactory().create(avl);
        new CRRCSimRepository().storeToFile(this.file, crrcsim);

        // Load
        CRRCSim loaded = new CRRCSimRepository().restoreFromFile(this.file);

        // Verify all surfaces were loaded
        assertEquals("Should have 3 surfaces after load", 3, loaded.getAvl().getGeometry().getSurfaces().size());
        assertEquals("Wing", loaded.getAvl().getGeometry().getSurfaces().get(0).getName());
        assertEquals("Horizontal Stabilizer", loaded.getAvl().getGeometry().getSurfaces().get(1).getName());
        assertEquals("Vertical Stabilizer", loaded.getAvl().getGeometry().getSurfaces().get(2).getName());
    }

}