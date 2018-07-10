package de.fh_dortmund.throwit.menu.calculations;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;
import org.apache.commons.math3.util.Pair;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import de.fh_dortmund.throwit.menu.ThrowCalculatorI;

/**
 * Calc1: LinAcc, FFT in 16er steps
 * @author Bijan Riesenberg
 */
public class ThrowCalculator implements ThrowCalculatorI {


    private List<Double> lastNValues;
    private List<Pair<Double, Long>> accel;
    //private ThrowFilter tf;


    private static final int AVGVALUESSAVED = 50;
    private static final int DFTSIZE= 16; //DFTSIZE ∊ 2^n da FFT sonst nicht funktioniert!


    public ThrowCalculator() {
        lastNValues = new LinkedList<>();
        accel = new LinkedList<>();
      //  tf = new ThrowFilter();
    }


    public boolean add(double[] acceleration, Long timestamp) {
        //tf.getKf().predict();
        //tf.getKf().correct(acceleration);

        //double[] stateEstimate = tf.getKf().getStateEstimation(); //Vector containing a_x, a_y, a_z, a'_x, ..., a''_z
        //Log.i("State Estimate: ",""+stateEstimate);
        //we want acceleration in upwards/downwards direction which is at at position 2 (a_z)
        accel.add(new Pair<>(acceleration[2], timestamp));
        return checkStop(acceleration[2]);
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


    private boolean checkStop(double verticalAcceleration) {
        lastNValues.add(0,verticalAcceleration);
        if(lastNValues.size() < AVGVALUESSAVED)
            return true;
        if(lastNValues.size() > AVGVALUESSAVED)
            lastNValues.remove(AVGVALUESSAVED);
        return !(lastNValues.size() == AVGVALUESSAVED &&
                    median(lastNValues) < 0 &&
                    System.nanoTime()-accel.get(0).getSecond() > 1000000000);
    }


    private double median(List<Double> values){
        if(values == null || values.isEmpty())
            return 0;
        Collections.sort(values);
        return values.get(values.size()/2);
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
