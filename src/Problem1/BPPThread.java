import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class BPPThread implements Runnable {
    private int servantNum, minVal, maxVal;
    private List<Integer> list;
    private ConcurrentLinkedList<Integer> linkedList;
    private AtomicInteger count;

    public BPPThread(int servantNum, int minVal, int maxVal, List<Integer> list,
            ConcurrentLinkedList<Integer> linkedList, AtomicInteger count) {
        this.servantNum = servantNum;
        this.minVal = minVal;
        this.maxVal = maxVal;
        this.list = list;
        this.linkedList = linkedList;
        this.count = count;
    }

    @Override
    public void run() {
        // System.out.println("minVal.get: " + minVal);
        // System.out.println("maxVal.get: " + maxVal);

        for (int i = minVal; i < maxVal; i++) {
            int randomValue = list.get(i);
            // System.out.println("list.get(" + i + "): " + list.get(i));

            linkedList.add(randomValue);
            count.getAndIncrement();
            int removedVal = linkedList.remove();
            // System.out.println("removed Val: " + removedVal);
        }
    }
}