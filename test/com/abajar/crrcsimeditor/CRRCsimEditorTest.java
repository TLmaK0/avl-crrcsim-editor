/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.abajar.crrcsimeditor;

import java.nio.file.Path;
import java.nio.file.Files;
import com.abajar.crrcsimeditor.avl.mass.Mass;
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
        Files.deleteIfExists(file.toPath());
    }

    @After
    public void tearDown() throws IOException {
        Files.deleteIfExists(file.toPath());
    }

    @Test
    public void testOpen() throws IOException, JAXBException, ClassNotFoundException {
        System.out.println("showGeoEditor");

        CRRCsimEditor instance = new CRRCsimEditor();
        instance.avl.getGeometry().getMasses().add(new Mass());
        instance.saveAs(this.file);
        instance.open(this.file);
        assertEquals(instance.avl.getGeometry().getMasses().size(), 1);
    }

}