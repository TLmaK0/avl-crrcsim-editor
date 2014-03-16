/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.abajar.crrcsimeditor.avl.connectivity;

import com.abajar.crrcsimeditor.avl.runcase.Configuration;
import com.abajar.crrcsimeditor.avl.runcase.AvlCalculation;
import com.abajar.crrcsimeditor.avl.AVL;
import com.abajar.crrcsimeditor.avl.AVLGeometry;
import com.abajar.crrcsimeditor.avl.runcase.StabilityDerivatives;
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
        String fileCrrTest="./sample/aerosonde/aerosonde.crr";
        String fileTest = "./sample/aerosonde/aerosonde.avl";
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

        AvlRunner instance = new AvlRunner("avl","sample/aerosonde","aerosonde.avl");

        int elevatorPosition = avl.getElevatorPosition();
        int aileronPosition = avl.getRudderPosition();
        int rudderPosition = avl.getAileronPosition();
        
        AvlCalculation runCase = instance.getCalculation(elevatorPosition, rudderPosition, aileronPosition);
        Configuration config = runCase.getConfiguration();
        StabilityDerivatives std = runCase.getStabilityDerivatives();
        
        assertTrue(config.getBref() != 0);
        assertTrue(config.getSref() != 0);
        assertTrue(config.getCref() != 0);
        assertTrue(config.getVelocity() != 0);
        assertTrue(config.getAlpha() == 0);
        assertTrue(config.getCmtot() != 0);
        assertTrue(config.getCLtot() != 0);
        assertTrue(config.getCDvis() == 0);
        
        assertTrue(std.getCma() != 0);
        assertTrue(std.getCmq() != 0);
        assertTrue(std.getCLa() != 0);
        assertTrue(std.getCmd()[elevatorPosition] != 0);
        assertTrue(std.getCLq() != 0);
        assertTrue(std.getCLd()[elevatorPosition] != 0);
        assertTrue(std.getCYb() != 0);
        assertTrue(std.getCYp() != 0);
        assertTrue(std.getCYr() != 0);
        assertTrue(std.getCYd()[rudderPosition] != 0);
        assertTrue(std.getCYd()[aileronPosition] != 0);
        assertTrue(std.getClb() != 0);
        assertTrue(std.getClp() != 0);
        assertTrue(std.getClr() != 0);
        assertTrue(std.getCld()[rudderPosition] != 0);
        assertTrue(std.getCld()[aileronPosition] != 0);
        assertTrue(std.getCnb() != 0);
        assertTrue(std.getCnp() != 0);
        assertTrue(std.getCnr() != 0);
        assertTrue(std.getCnd()[rudderPosition] != 0);
        assertTrue(std.getCnd()[aileronPosition] != 0);


    }



}