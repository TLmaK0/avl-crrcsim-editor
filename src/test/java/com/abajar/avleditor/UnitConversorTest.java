/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.abajar.avleditor;

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
        assertEquals(expResult, result, 0.00001);
    }

    @Test
    public void testConvertInToMeters() {
        System.out.println("convertToMeters");
        float quantity = 10.0F;
        String lengthUnit = "oz";
        UnitConversor instance = new UnitConversor();
        float expResult = 0.283495F;
        float result = instance.convertToKilograms(quantity, lengthUnit);
        assertEquals(expResult, result, 0.00001);
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
        assertEquals(expResult, result, 0.00001);
    }

    /**
     * Test of convertToKilogramsSquareMeters method, of class UnitConversor.
     */
    @Test
    public void testConvertToKilogramsSquareMeters(){
        System.out.println("convertToKilogramsSquareMeters");
        float quantity = 10.0F;
        String lengthUnit = "in";
        String massUnit = "oz";

        UnitConversor instance = new UnitConversor();
        float expResult = 0.000182899F;
        float result = instance.convertToKilogramsSquareMeters(quantity, massUnit, lengthUnit);
        assertEquals(expResult, result, 0.00001);
    }

    /**
     * Test of convertToKilogramsMeters method, of class UnitConversor.
     */
    @Test
    public void testConvertToKilogramsMeters(){
        System.out.println("convertToKilogramsSquareMeters");
        float quantity = 10.0F;
        String lengthUnit = "in";
        String massUnit = "oz";

        UnitConversor instance = new UnitConversor();
        float expResult = 0.00720077887f;
        float result = instance.convertToKilogramsMeters(quantity, massUnit, lengthUnit);
        assertEquals(expResult, result, 0.00001);
    }


}