/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.abajar.crrcsimeditor;

import com.abajar.crrcsimeditor.crrcsim.CRRCSimRepository;
import com.abajar.crrcsimeditor.crrcsim.CRRCSim;
import java.nio.file.Files;
import java.nio.file.Paths;
import com.abajar.crrcsimeditor.avl.AVL;
import com.abajar.crrcsimeditor.avl.mass.Mass;
import com.abajar.crrcsimeditor.crrcsim.CRRCSimFactory;
import java.io.File;
import java.io.IOException;
import javax.xml.bind.JAXBException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author hfreire
 */
public class CRRCsimEditorTest {
    final File file = new File("test.crr");
    public CRRCsimEditorTest() {
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
    public void testOpen() throws IOException, JAXBException, ClassNotFoundException, InterruptedException, Exception {
        CRRCSim crrcsim = new CRRCSimRepository().restoreFromFile(new File("./sample/aerosonde/aerosonde.crr"));
        new CRRCSimRepository().storeToFile(this.file, crrcsim);
        crrcsim = new CRRCSimRepository().restoreFromFile(this.file);
        assertEquals(0.254, crrcsim.getAvl().getGeometry().getSurfaces().get(0).getSections().get(0).getChord(), 0.0001);
    }

}