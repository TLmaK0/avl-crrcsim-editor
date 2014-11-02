/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.abajar.crrcsimeditor;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Hugo
 */
public class UnitConversorTest {

    public UnitConversorTest() {
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
    public void testConvertCmToMeters() {
        System.out.println("convertToMeters");
        float quantity = 10.0F;
        String lengthUnit = "cm";
        UnitConversor instance = new UnitConversor();
        float expResult = 0.1F;
        float result = instance.convertToMeters(quantity, lengthUnit);
        assertEquals(expResult, result, 0.000001);
    }

    @Test
    public void testConvertInToMeters() {
        System.out.println("convertToMeters");
        float quantity = 10.0F;
        String lengthUnit = "in";
        UnitConversor instance = new UnitConversor();
        float expResult = 0.254F;
        float result = instance.convertToMeters(quantity, lengthUnit);
        assertEquals(expResult, result, 0.000001);
    }

    /**
     * Test of convertToSquareMeters method, of class UnitConversor.
     */
    @Test
    public void testConvertSqCmToSquareMeters() {
        System.out.println("convertToSquareMeters");
        float quantity = 10.0F;
        String lengthUnit = "cm";
        UnitConversor instance = new UnitConversor();
        float expResult = 0.001F;
        float result = instance.convertToSquareMeters(quantity, lengthUnit);
        assertEquals(expResult, result, 0.000001);
    }

        /**
     * Test of convertToSquareMeters method, of class UnitConversor.
     */
    @Test
    public void testConvertSqInToSquareMeters() {
        System.out.println("convertToSquareMeters");
        float quantity = 10.0F;
        String lengthUnit = "in";
        UnitConversor instance = new UnitConversor();
        float expResult = 0.0064516F;
        float result = instance.convertToSquareMeters(quantity, lengthUnit);
        assertEquals(expResult, result, 0.000001);
    }

}