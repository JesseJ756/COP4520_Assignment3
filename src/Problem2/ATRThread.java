import java.util.concurrent.PriorityBlockingQueue;

public class ATRThread implements Runnable {

    private int[] tempReadings;
    private PriorityBlockingQueue<Integer> maxTempMin;
    private PriorityBlockingQueue<Integer> minTempMin;
    private PriorityBlockingQueue<Integer> maxTemp10Min;

    public ATRThread(int[] tempReadings, PriorityBlockingQueue<Integer> maxTempMin,
            PriorityBlockingQueue<Integer> minTempMin, PriorityBlockingQueue<Integer> maxTemp10Min) {
        this.tempReadings = tempReadings;
        this.maxTempMin = maxTempMin;
        this.minTempMin = minTempMin;
        this.maxTemp10Min = maxTemp10Min;
    }

    @Override
    public void run() {

        int recordTime = tempReadings.length;

        for (int i = 0; i < recordTime; i++) {
            maxTempMin.add(tempReadings[i]);
            minTempMin.add(tempReadings[i]);

            if (i >= 9) {
                // System.out.println("abs(" + tempReadings[i] + " - (" + tempReadings[i - 9] +
                // ")) = " + (Math.abs(tempReadings[i] - tempReadings[i - 9])));
                maxTemp10Min.add(Math.abs(tempReadings[i] - tempReadings[i - 9]));
            }

            try {
                Thread.sleep(500);
                // System.out.println("maxTempMin:" + maxTempMin);
                // System.out.println("minTempMin:" + minTempMin);
                // System.out.println("maxTemp10Min:" + maxTemp10Min);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
