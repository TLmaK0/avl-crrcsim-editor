/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.abajar.crrcsimeditor.avl.connectivity;

import com.abajar.crrcsimeditor.avl.runcase.AvlCalculation;
import com.abajar.crrcsimeditor.avl.AVL;
import com.abajar.crrcsimeditor.avl.AVLGeometry;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
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
        String fileCrrTest="./sample/asond/asond.crr";
        String fileTest = "./sample/asond/asondGenerated.avl";
        File file = new File(fileTest);
        AVL avl = new AVL();
        FileInputStream fis = new FileInputStream(fileCrrTest);
        JAXBContext context = JAXBContext.newInstance(AVLGeometry.class);
        Unmarshaller u = context.createUnmarshaller();
        avl.setGeometry((AVLGeometry)u.unmarshal(fis));
        fis.close();

        FileOutputStream fos = new FileOutputStream(file);
        avl.getGeometry().writeAVLData(fos);
        fos.close();

        String fileMassPath = file.getPath().replace(".avl", ".mass");
        File fileMass = new File(fileMassPath);
        fos = new FileOutputStream(fileMass);
        avl.getGeometry().writeAVLMassData(fos);
        fos.close();

        AvlRunner instance = new AvlRunner("avl","sample/asond","asond.avl");
        AvlCalculation runCase = instance.getCalculation(avl.getElevatorPosition(), avl.getRudderPosition(), avl.getAileronPosition());
        assertTrue(runCase.getConfiguration().getBref() != 0);
        assertTrue(runCase.getConfiguration().getSref() != 0);
        assertTrue(runCase.getConfiguration().getCref() != 0);
        assertTrue(runCase.getConfiguration().getVelocity() != 0);
        assertTrue(runCase.getConfiguration().getAlpha() != 0);
        assertTrue(runCase.getConfiguration().getCmtot() != 0);
        assertTrue(runCase.getConfiguration().getCLtot() != 0);
        assertTrue(runCase.getConfiguration().getCDvis() == 0);
        
        assertTrue(runCase.getStabilityDerivatives().getCma() != 0);
        assertTrue(runCase.getStabilityDerivatives().getCmq() != 0);
        assertTrue(runCase.getStabilityDerivatives().getCLa() != 0);
        assertTrue(runCase.getStabilityDerivatives().getCmd()[avl.getElevatorPosition()] != 0);
        assertTrue(runCase.getStabilityDerivatives().getCLq() != 0);
        assertTrue(runCase.getStabilityDerivatives().getCLd()[avl.getElevatorPosition()] != 0);
        assertTrue(runCase.getStabilityDerivatives().getCYb() != 0);
        assertTrue(runCase.getStabilityDerivatives().getCYp() != 0);
        assertTrue(runCase.getStabilityDerivatives().getCYr() != 0);
        assertTrue(runCase.getStabilityDerivatives().getCYd()[avl.getRudderPosition()] != 0);
        assertTrue(runCase.getStabilityDerivatives().getCYd()[avl.getAileronPosition()] == 0);
        assertTrue(runCase.getStabilityDerivatives().getClb() != 0);
        assertTrue(runCase.getStabilityDerivatives().getClp() != 0);
        assertTrue(runCase.getStabilityDerivatives().getClr() != 0);
        assertTrue(runCase.getStabilityDerivatives().getCld()[avl.getRudderPosition()] != 0);
        assertTrue(runCase.getStabilityDerivatives().getCld()[avl.getAileronPosition()] != 0);
        assertTrue(runCase.getStabilityDerivatives().getCnb() != 0);
        assertTrue(runCase.getStabilityDerivatives().getCnp() != 0);
        assertTrue(runCase.getStabilityDerivatives().getCnr() != 0);
        assertTrue(runCase.getStabilityDerivatives().getCnd()[avl.getRudderPosition()] != 0);
        assertTrue(runCase.getStabilityDerivatives().getCnd()[avl.getAileronPosition()] == 0);


    }



}