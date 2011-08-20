/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.abajar.crrcsimeditor.avl.connectivity;

import com.abajar.crrcsimeditor.avl.AVL;
import java.io.File;
import java.io.FileOutputStream;
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
public class AvlRunnerTest {

    public AvlRunnerTest() {
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

    /**
     * Test of calculate method, of class AvlRunner.
     */
    @Test
    public void testCalculate() throws Exception {
        System.out.println("calculate");
        String fileTest = "test.avl";
        File file = new File(fileTest);
        AVL avl = new AVL();
        FileOutputStream fos = new FileOutputStream(file);
        avl.getGeometry().writeAVLData(fos);
        fos.close();

        String fileMassPath = file.getPath().replace(".avl", ".mass");
        File fileMass = new File(fileMassPath);
        fos = new FileOutputStream(fileMass);
        avl.getGeometry().writeAVLMassData(fos);
        fos.close();

        AvlRunner instance = new AvlRunner("C://Programs//simulation//aviation//avl//bin/avl","test.avl");
        instance.calculate();
        //instance.getStabilityDerivatives();
        instance.close();
    }



}