package de.fh_dortmund.throwit.menu;

import org.apache.commons.math3.filter.KalmanFilter;
import org.apache.commons.math3.filter.MeasurementModel;
import org.apache.commons.math3.filter.ProcessModel;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.util.Pair;

import java.util.LinkedList;
import java.util.List;

public class BetterThrowCalculator {

    private List<Pair<Double, Long>> accel;
    private KalmanFilter kf;
    private static final int DFTSIZE= 16; //DFTSIZE ∊ 2^n da FFT sonst nicht funktioniert!

    public BetterThrowCalculator() {
        accel = new LinkedList<>();
        MeasurementModel measurementModel = new MeasurementModel() {
            @Override
            public RealMatrix getMeasurementMatrix() {
                return null;
            }

            @Override
            public RealMatrix getMeasurementNoise() {
                return null;
            }
        };

        ProcessModel processModel = new ProcessModel() {
            @Override
            public RealMatrix getStateTransitionMatrix() {
                return null;
            }

            @Override
            public RealMatrix getControlMatrix() {
                return null;
            }

            @Override
            public RealMatrix getProcessNoise() {
                return null;
            }

            @Override
            public RealVector getInitialStateEstimate() {
                return null;
            }

            @Override
            public RealMatrix getInitialErrorCovariance() {
                return null;
            }
        };

        kf = new KalmanFilter(processModel, measurementModel);


    }

    public boolean add(Double acceleration, Long timestamp) {
        return accel.add(new Pair<>(Math.max(0d,acceleration), timestamp));
    }


    public double calculateHeight() {

        return 0.0;
    }

}
