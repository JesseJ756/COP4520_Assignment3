import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;

public class BirthdayPresentParty {
    public static int maxPresents = 500000, maxServants = 4;
    public static volatile AtomicInteger servantNum = new AtomicInteger(0);
    public static volatile AtomicIntegerArray atomicIntegerCurr = new AtomicIntegerArray(4);
    public static volatile AtomicIntegerArray atomicIntegerMax = new AtomicIntegerArray(4);
    public static volatile Thread[] threads = new Thread[maxServants];
    public static volatile AtomicInteger count = new AtomicInteger(0);

    public static volatile ConcurrentLinkedList<Integer> linkedList = new ConcurrentLinkedList<>();
    public static volatile List<Integer> list = new ArrayList<>();

    public static void main(String[] args) {

        // Create a shuffle a list that contains the numbers 0-500000
        for (int i = 0; i < maxPresents; i++) {
            list.add(i);
        }
        Collections.shuffle(list);

        // Splits the presents that each servant is inserting
        for (int i = 0; i < maxServants; i++) {
            atomicIntegerCurr.set(i, i * 125000);
            atomicIntegerMax.set(i, i * 125000 + 125000);
        }

        long startTime = System.currentTimeMillis();
        for (; servantNum.get() < maxServants; servantNum.incrementAndGet()) {
            int tempServantNum = servantNum.get();
            threads[servantNum.get()] = new Thread(new BPPThread(tempServantNum, atomicIntegerCurr.get(tempServantNum),
                    atomicIntegerMax.get(tempServantNum), list, linkedList, count));
            threads[servantNum.get()].start();
        }

        for (int j = 0; j < maxServants; j++) {
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

        // System.out.println("list: " + list);
        System.out.println("count: " + count.get());
        if (count.get() == maxPresents)
            System.out.println("All guests thanked!");
        else
            System.out.println("Oops missed a few guests");

        System.out.println("totTime: " + totTime);
    }
}