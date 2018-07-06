package de.fh_dortmund.throwit.menu;

import org.apache.commons.math3.filter.KalmanFilter;
import org.apache.commons.math3.filter.MeasurementModel;
import org.apache.commons.math3.filter.ProcessModel;

public class ThrowFilter {
    private KalmanFilter kf;

    public ThrowFilter() {
        ProcessModel processModel = new ThrowProcess();
        MeasurementModel measurementModel = new ThrowMeasurement();
        kf = new KalmanFilter(processModel,measurementModel);
    }

    public void iterate(double[] newest_measure) {
        kf.predict();
        kf.correct(newest_measure);
    }

    public KalmanFilter getKf() {
        return kf;
    }

    public void setKf(KalmanFilter kf) {
        this.kf = kf;
    }
}
