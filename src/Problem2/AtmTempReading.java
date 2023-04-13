import java.util.Arrays;
import java.util.Collections;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.concurrent.PriorityBlockingQueue;

public class AtmTempReading {
    public static int SENSORNUM = 8;
    public static int MINTEMP = -100;
    public static int MAXTEMP = 70;
    public static int MAXTIME = 60;
    public static Random rand = new Random();
    public static volatile Thread[] threads = new Thread[SENSORNUM];

    public static volatile PriorityBlockingQueue<Integer> maxTempMin = new PriorityBlockingQueue<Integer>(5,
            Collections.reverseOrder());
    public static volatile PriorityBlockingQueue<Integer> minTempMin = new PriorityBlockingQueue<Integer>(5);
    public static volatile PriorityBlockingQueue<Integer> maxTemp10Min = new PriorityBlockingQueue<>(5,
            Collections.reverseOrder());

    /*
     * each thread keeps track of their own temp values in an array?
     * at every mintue store value in max/min heap
     * at the 10 min mark subtract 10th - 1st and get absolute value
     * - store in max heap
     * 
     */

    public static void main(String[] args) {
        int[][] tempReadings = new int[SENSORNUM][MAXTIME];

        System.out.println();

        for (int i = 0; i < SENSORNUM; i++) {
            for (int j = 0; j < MAXTIME; j++) {
                tempReadings[i][j] = rand.nextInt((MAXTEMP - MINTEMP) + 1) + MINTEMP;
            }
            // System.out.println("tempReadings[" + i + "]: " +
            // Arrays.toString(tempReadings[i]));
        }

        long startTime = System.currentTimeMillis();
        for (int i = 0; i < SENSORNUM; i++) {
            threads[i] = new Thread(new ATRThread(tempReadings[i], maxTempMin, minTempMin, maxTemp10Min));
            threads[i].start();
        }

        for (int j = 0; j < SENSORNUM; j++) {
            try {
                if (threads[j] != null) {
                    threads[j].join();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        long endTime = System.currentTimeMillis();
        long totTime = endTime - startTime;

        System.out.println();
        System.out.println("totTime: " + (totTime));
        System.out.println("totTime not including putting the threads to sleep: " + (totTime - (MAXTIME * 500)));
        System.out.println();

        try {
            System.out.print("Top 5 highest temperatures: ");
            for (int i = 0; i < 5; i++) {
                System.out.print(maxTempMin.take() + " ");
            }
            System.out.println();

            System.out.print("Top 5 lowest temperatures: ");
            for (int i = 0; i < 5; i++) {
                System.out.print(minTempMin.take() + " ");
            }
            System.out.println();

            System.out.println("Largest temperature difference: " + maxTemp10Min.take());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
