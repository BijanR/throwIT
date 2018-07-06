package de.fh_dortmund.throwit.menu.calculations;

import org.apache.commons.math3.filter.ProcessModel;
import org.apache.commons.math3.filter.ProcessModel;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.DiagonalMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

/**
 * @author Bijan Riesenberg
 */
class ThrowProcess implements ProcessModel {
    private long timeDelta;
    private double TIMEDELTAINTEGRATED = 0.5d*Math.pow(timeDelta,2);

    public ThrowProcess() {}

    public ThrowProcess(long timeDelta) {
        this.timeDelta = timeDelta;
    }

    @Override
    public RealMatrix getStateTransitionMatrix() {
        return new Array2DRowRealMatrix(new double[][] {
                {1,0,0,timeDelta,0,0,TIMEDELTAINTEGRATED,0,0},
                {0,1,0,0,timeDelta,0,0,TIMEDELTAINTEGRATED,0},
                {0,0,1,0,0,timeDelta,0,0,TIMEDELTAINTEGRATED},
                {0,0,0,1,0,0,timeDelta,0,0},
                {0,0,0,0,1,0,0,timeDelta,0},
                {0,0,0,0,0,1,0,0,timeDelta},
                {0,0,0,0,0,0,1,0,0},
                {0,0,0,0,0,0,0,1,0},
                {0,0,0,0,0,0,0,0,1}
        });
    }

    @Override
    public RealMatrix getControlMatrix() {
        return null;
    }

    // Variance ~ 0.05
    @Override
    public RealMatrix getProcessNoise() {
        return new DiagonalMatrix(new double[] { 0.05d,0.05d,0.05d,0.00d,0.00d,0.00d,0.0d,0.0d,0.0d });
    }

    @Override
    public RealVector getInitialStateEstimate() {
        return new ArrayRealVector(new double[]{0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d}) {
        };
    }

    @Override
    public RealMatrix getInitialErrorCovariance() {
        return null;
    }

    public long getTimeDelta() {
        return timeDelta;
    }

    public void setTimeDelta(long timeDelta) {
        this.timeDelta = timeDelta;
    }
}
