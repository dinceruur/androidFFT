package com.github.dinceruur.androidfft.DataProcess;

import org.apache.commons.math3.complex.Complex;

public class MathOperators {

    double[] squareRootOfComplex(Complex[] input){
        double[] output = new double[input.length];
        for(int i = 0; i < input.length; i++){
            output[i] = (Math.sqrt(Math.pow(input[i].getReal(),2) + Math.pow(input[i].getImaginary(),2)))/(input.length/2);
        }
        return output;
    }
}
