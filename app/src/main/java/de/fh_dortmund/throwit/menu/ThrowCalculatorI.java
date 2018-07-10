package de.fh_dortmund.throwit.menu;

public interface ThrowCalculatorI {

    /**
     *
     * @param acceleration event.values from Accelerometer (x,y,z acceleration dependant on device orientation)
     * @param timestamp timestamp relative to button press for start throw
     * @return False iif Throw is completed
     */
    boolean add(double[] acceleration, Long timestamp);

    /**
     *
     * @return Height calculated from all added values
     */
    double calculateHeight();
}
