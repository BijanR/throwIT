package de.fh_dortmund.throwit;


import org.apache.commons.math3.util.Pair;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
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
        tc.add(new double[]{0,2,2.1}, 0L);
        assertTrue(tc.calculateHeight() == 0.0);
    }


    @Test
    public void throwCalculator_addTest() {
        ThrowCalculator tc = new ThrowCalculator();
        assertTrue(tc.add(new double[]{0,0,2.4},0L));
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
        assertEquals(0.3, height, 0.1);
    }


    @Test
    public void throwCalculator_43Values() {
        ThrowCalculator tc = new ThrowCalculator();
        List<Pair<double[],Long>> tmpList = generateTestData(43,5);
        for(Pair<double[], Long> p: tmpList)
            tc.add(p.getFirst(),p.getSecond());
        double height = tc.calculateHeight();
        System.out.println("43Values: "+height);
        assertEquals(1, height, 2.0);
    }


    @Test
    public void throwCalculator_100Values() {
        ThrowCalculator tc = new ThrowCalculator();
        List<Pair<double[],Long>> tmpList = generateTestData(100,5);
        for(Pair<double[], Long> p: tmpList)
            tc.add(p.getFirst(),p.getSecond());
        double height = tc.calculateHeight();
        System.out.println("100Values: "+height);
        assertEquals(1, height, 5.0);
    }




    @Test
    public void throwCalculator_100ValuesFaster() {
        ThrowCalculator tc = new ThrowCalculator();
        List<Pair<double[],Long>> tmpList = generateTestData(100,25);
        for(Pair<double[], Long> p: tmpList)
            tc.add(p.getFirst(),p.getSecond());
        double height = tc.calculateHeight();
        System.out.println("100FasterValues: "+height);
        List<Pair<double[],Long>> tmpList2 = generateTestData(100,5);
        ThrowCalculator tc2 = new ThrowCalculator();
        for(Pair<double[], Long> p: tmpList2)
            tc2.add(p.getFirst(),p.getSecond());
        double height2 = tc2.calculateHeight();
        System.out.println("100SlowerValues: "+height2);
        assertTrue(height > height2);
    }


    @Test
    public void throwCalculator_100ValuesInSpace() {
        ThrowCalculator tc = new ThrowCalculator();
        List<Pair<double[],Long>> tmpList = generateTestData(100,25);
        for(Pair<double[], Long> p: tmpList)
            tc.add(p.getFirst(),p.getSecond());

        // Drift with constant velocity for a while
        Long time = tmpList.get(tmpList.size()-1).getSecond();
        for(int i = 0; i<500; i++) {
            time += 100000000L;
            tc.add(new double[]{0,0,1},time);
        }
        double height = tc.calculateHeight();
        System.out.println("100FasterValuesSpace: "+height);

        List<Pair<double[],Long>> tmpList2 = generateTestData(100,5);
        ThrowCalculator tc2 = new ThrowCalculator();
        for(Pair<double[], Long> p: tmpList2)
            tc2.add(p.getFirst(),p.getSecond());

        Long time2 = tmpList2.get(tmpList2.size()-1).getSecond();
        for(int i = 0; i<100; i++) {
            time += 100000000L;
            tc.add(new double[]{0,0,1},time);
        }
        double height2 = tc2.calculateHeight();
        System.out.println("100SlowerValuesSpace: "+height2);
        assertTrue(height > height2);
    }

    @Test
    public void throwCalculator_10234Values() {
        ThrowCalculator tc = new ThrowCalculator();
        List<Pair<double[],Long>> tmpList = generateTestData(10234,5);
        for(Pair<double[], Long> p: tmpList)
            tc.add(p.getFirst(),p.getSecond());
        double height = tc.calculateHeight();
        System.out.println("10234Values: "+height);
        assertEquals(220, height, 15.0);
    }


    private List<Pair<double[],Long>> generateTestData(int k,int factor) {
        List<Pair<double[],Long>> result = new LinkedList<>();
        for(int i = 0; i < k; i++) {
            result.add(new Pair<>(new double[] {0,0,Math.random()*factor-0.001},i*100000000L));
        }
        return result;
    }
}
