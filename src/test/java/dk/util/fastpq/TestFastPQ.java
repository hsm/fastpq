package dk.util.fastpq;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class TestFastPQ {

    @Test
    public void testSomething() {
        int[] numbers = {
                -87, 73, 16, -90, -59, 0, 85, -25, 77, -24, 67, 44, 34,
                -26, -25, -40, -22, 1, 63, -37, 4, 60, -9, 68, -22, -54,
                9, 0, 89, 13, 28, -14, 68, -97, 95, -4, 26, -46, -7, -75,
                -86, 62, -22, -52, -25, 28, -90, -39, 80, -90, -31, -18,
                -11, 25, 50, -1, 75, 25, 89, 37, 49, 91, 10, -68, -49, 43,
                44, -36, -2, 60, 17, 98, -27, -74, 43, 36, -54, -93, -6, 17,
                -19, 65, 48, -39, 50, 62, 70, -68, -55, 11, -64, -51, -70, -66,
                63, -6, -72, -54, 5, 29, 34
        };
        FastPQ fastPQ = new FastPQ(4, 2, 2);
        for (int number : numbers) {
            fastPQ.insert(number);
            assertTrue(checkHeapProperty());
        }
    }

    private boolean checkHeapProperty() {
        return false;
    }

}
