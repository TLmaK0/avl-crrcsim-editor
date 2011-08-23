/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.abajar.crrcsimeditor;

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

    public CRRCsimEditorTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testOpen() throws IOException, JAXBException, ClassNotFoundException {
        System.out.println("showGeoEditor");

        CRRCsimEditor instance = new CRRCsimEditor();
        instance.avl.getGeometry().getMasses().add(new Mass());
        instance.saveAs(new File("test.crr"));
        instance.open(new File("test.crr"));

        assertEquals(instance.avl.getGeometry().getMasses().size(), 1);
    }

}