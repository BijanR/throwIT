package de.fh_dortmund.throwit.menu;

import org.apache.commons.math3.filter.MeasurementModel;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

public class ThrowMeasurement implements MeasurementModel{

    @Override
    public RealMatrix getMeasurementMatrix() {
        return new Array2DRowRealMatrix(new double[][] {
                {1,0,0,0,0,0,0,0,0},
                {0,1,0,0,0,0,0,0,0},
                {0,0,1,0,0,0,0,0,0}
        });
    }

    @Override
    public RealMatrix getMeasurementNoise() {
        return new Array2DRowRealMatrix(new double[] { 0.05d,0.05d,0.05d,0.05d,0.05d,0.05d,0.05d,0.05d,0.05d });
    }
}
