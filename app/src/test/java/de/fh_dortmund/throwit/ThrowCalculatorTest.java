package de.fh_dortmund.throwit;


import org.apache.commons.math3.util.Pair;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import de.fh_dortmund.throwit.menu.calculations.ThrowCalculator;

/**
 * Zeitschritte von 0.1 Sekunden für generierte Messwerte.
 * @author Bijan
 */
public class ThrowCalculatorTest {


    @Test
    public void throwCalculator_NoValues_ReturnZero() {
        assertTrue(new ThrowCalculator().calculateHeight() == 0.0);
    }


    @Test
    public void throwCalculator_OneValue() {
        ThrowCalculator tc = new ThrowCalculator();
        tc.add(new double[]{0,0,2,2.1}, 0L);
        assertTrue(tc.calculateHeight() == 0.0);
    }


    @Test
    public void throwCalculator_addTest() {
        ThrowCalculator tc = new ThrowCalculator();
        assertTrue(tc.add(new double[]{0,0,2,1.2},0L));
    }


    @Test
    public void throwCalculator_16Values() {
        ThrowCalculator tc = new ThrowCalculator();
        // Die Werte sind groß weil wir in Nanosekunden messen!
        tc.add(new double[]{0,0,2.4}, 0L);
        tc.add(new double[]{0,0,2.5}, 100000000L);
        tc.add(new double[]{0,0,2.6}, 200000000L);
        tc.add(new double[]{0,0,2.5}, 300000000L);
        tc.add(new double[]{0,0,2.4}, 400000000L);
        tc.add(new double[]{0,0,2.3}, 500000000L);
        tc.add(new double[]{0,0,2.2}, 600000000L);
        tc.add(new double[]{0,0,2.1}, 700000000L);
        tc.add(new double[]{0,0,2.0}, 800000000L);
        tc.add(new double[]{0,0,1.9}, 900000000L);
        tc.add(new double[]{0,0,1.7}, 1000000000L);
        tc.add(new double[]{0,0,1.7}, 1100000000L);
        tc.add(new double[]{0,0,1.7}, 1200000000L);
        tc.add(new double[]{0,0,1.7}, 1300000000L);
        tc.add(new double[]{0,0,1.7}, 1400000000L);
        tc.add(new double[]{0,0,1.7}, 1500000000L);
        double height = tc.calculateHeight();
        System.out.println("16 fixed Values: "+height);
        assertEquals(0.3, height, 0.01);
    }


    @Test
    public void throwCalculator_43Values() {
        ThrowCalculator tc = new ThrowCalculator();
        List<Pair<Double[],Long>> tmpList = generateTestData(43);
        for(Pair<double[], Long> p: tmpList)
            tc.add(p.getFirst(),p.getSecond());
        double height = tc.calculateHeight();
        System.out.println("43Values: "+height);
        assertEquals(1, height, 2.0);
    }


    @Test
    public void throwCalculator_100Values() {
        ThrowCalculator tc = new ThrowCalculator();
        List<Pair<Double,Long>> tmpList = generateTestData(100);
        for(Pair<Double, Long> p: tmpList)
            tc.add(p.getFirst(),p.getSecond());
        double height = tc.calculateHeight();
        System.out.println("100Values: "+height);
        assertEquals(1, height, 5.0);
    }


    @Test
    public void throwCalculator_10234Values() {
        ThrowCalculator tc = new ThrowCalculator();
        List<Pair<Double,Long>> tmpList = generateTestData(10234);
        for(Pair<Double, Long> p: tmpList)
            tc.add(p.getFirst(),p.getSecond());
        double height = tc.calculateHeight();
        System.out.println("10234Values: "+height);
        assertEquals(220, height, 15.0);
    }


    private List<Pair<Double,Long>> generateTestData(int k) {
        List<Pair<Double,Long>> result = new LinkedList<>();
        for(int i = 0; i < k; i++) {
            result.add(new Pair<>(Math.random()*5-0.001,i*100000000L));
        }
        return result;
    }
}
