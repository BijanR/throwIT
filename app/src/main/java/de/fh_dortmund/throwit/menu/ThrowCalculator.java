package de.fh_dortmund.throwit.menu;

import android.util.Log;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.integration.RombergIntegrator;
import org.apache.commons.math3.analysis.integration.UnivariateIntegrator;
import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.interpolation.UnivariateInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;
import org.apache.commons.math3.util.Pair;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Bijan Riesenberg
 */
public class ThrowCalculator {
    private List<Pair<Double, Long>> accel;
    private static final int DFTSIZE= 16; //DFTSIZE ∊ 2^n da FFT sonst nicht funktioniert!

    public ThrowCalculator() {
        accel = new LinkedList<>();
    }

    public boolean add(Double acceleration, Long timestamp) {
        return accel.add(new Pair<>(acceleration, timestamp));
    }

    /**
     * Integrale sind zu schwer, darum FFT
     * Lang lebe Fourier!
     * @return Höhe berechnet nach inverser FFT der FFT der vertikalen Beschleunigung elementweise geteilt durch Signalrate
     */
    public double calculateHeight() {
            if(accel.size() < DFTSIZE)
                return 0.0;

            double result = 0.0;
            for(int k = 0; k<accel.size()/DFTSIZE;k++){

                // Daten auslesen
                double[] time = new double[DFTSIZE];
                double[] acceleration = new double[DFTSIZE];


                int count = 0;
                for(Pair<Double, Long> p: accel.subList(DFTSIZE*k,DFTSIZE*k+16)) {
                    time[count] = p.getSecond();
                    acceleration[count] = p.getFirst();
                    count++;
                }


                FastFourierTransformer fft = new FastFourierTransformer(DftNormalization.STANDARD);
                Complex[] accelFrequency = fft.transform(acceleration, TransformType.FORWARD);


                double frequencyRate = calculateSamplingRate(time);
                for(int i = 0; i < accelFrequency.length; i++)
                    accelFrequency[i] = accelFrequency[i].divide(Math.pow(frequencyRate,2));

                Complex[] displacement = fft.transform(accelFrequency, TransformType.INVERSE);
                for(Complex d: displacement)
                    result += d.abs();
            }

        return result;

    }

    /**
     * @param time Array mit Timestamps in ns
     * @return Frequenzrate Messungen/Zeit
     */
    public double calculateSamplingRate(double[] time) {
        assert time != null && time.length >= DFTSIZE;
        return time.length/((time[time.length-1]-time[0])/1000000000);
    }

}
