package com.jason;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;

/**
 * Test for PrimeNumberChecker.
 * JUnit 4 has introduced a new feature called parameterized tests.
 * Parameterized tests allow a developer to run the same test over and over again using different values.
 * There are five steps that you need to follow to create a parameterized test.
 */
@RunWith(Parameterized.class)
public class TestJunit9 {
    private Integer inputNumber;
    private Boolean expectedResult;
    private PrimeNumberChecker primeNumberChecker;

    @Parameterized.Parameters
    public static Collection primeNumbers(){
        return Arrays.asList(new Object[][]{
                {2, true},
                {6, false},
                {19, true},
                {22, false},
                {23, true}
        });
    }

    // Each parameter should be placed as an argument here
    // Every time runner triggers, it will pass the arguments

    // from parameters we defined in primeNumbers() method
    public TestJunit9(Integer inputNumber, Boolean expectedResult){
        this.inputNumber = inputNumber;
        this.expectedResult = expectedResult;
    }

    @Before
    public void initialize(){
        primeNumberChecker = new PrimeNumberChecker();
    }

    @Test
    public void validate() {
        System.out.println("Parameterized Number is : " + inputNumber);
        assertEquals(expectedResult, primeNumberChecker.validate(inputNumber));
    }
}