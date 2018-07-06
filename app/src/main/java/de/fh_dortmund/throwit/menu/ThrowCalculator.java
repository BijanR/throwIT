package de.fh_dortmund.throwit.menu;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;
import org.apache.commons.math3.util.Pair;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Bijan Riesenberg
 */
public class ThrowCalculator {
    private List<Pair<Double, Long>> accel;
    private static final int DFTSIZE= 16; //DFTSIZE ∊ 2^n da FFT sonst nicht funktioniert!
    private ThrowFilter tf;

    public ThrowCalculator() {
        accel = new LinkedList<>();
        tf = new ThrowFilter();
    }

    public boolean add(Double acceleration, Long timestamp) {

        tf.getKf().predict();
        tf.getKf().correct(new double[] {acceleration});
        double[] stateEstimate = tf.getKf().getStateEstimation(); //Vector containing a_x, a_y, a_z, a'_x, ..., a''_z

        //we want acceleration in upwards/downwards direction which is at at position 2 (a_z)
        return accel.add(new Pair<>(stateEstimate[2], timestamp));
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
