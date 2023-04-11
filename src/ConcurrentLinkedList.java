import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConcurrentLinkedList<T> {
    private Node head;
    ReentrantLock lock;
    private int MAXVALUE = Integer.MAX_VALUE;

    public ConcurrentLinkedList() {
        this.head = new Node(-1);
        head.nextNode = new Node(MAXVALUE);
    }

    private static class Node {
        private int presentValue;
        private Node nextNode = null;
        private final Lock lock = new ReentrantLock();

        public Node(int presentValue) {
            this.presentValue = presentValue;
        }

        public int getPresentValue() {
            return presentValue;
        }

        public Node getNextNode() {
            return nextNode;
        }

        public void setNextNode(Node nextNode) {
            this.nextNode = nextNode;
        }

        public void lock() {
            lock.lock();
        }

        public void unlock() {
            lock.unlock();
        }
    }

    public boolean add(int presentValue) {
        Node newNext = new Node(presentValue);

        head.lock();
        Node pred = head;

        // System.out.println("linking new value: " + presentValue);
        try {
            Node curr = pred.nextNode;
            curr.lock();

            try {
                while (curr.getPresentValue() < presentValue) {
                    // System.out.println("pred value: " + pred.presentValue);
                    pred.unlock();
                    pred = curr;
                    curr = curr.nextNode;
                    curr.lock();
                }

                newNext.setNextNode(curr);
                pred.setNextNode(newNext);

                return true;
            } finally {
                curr.unlock();
            }
        } finally {
            pred.unlock();
        }
    }

    public int remove() {
        head.lock();
        Node pred = head;

        int removedVal = -1;

        try {
            Node curr = pred.nextNode;
            curr.lock();
            Node tempPeekNode = curr.nextNode;
            try {
                while (tempPeekNode.presentValue != MAXVALUE) {
                    pred.unlock();
                    pred = curr;
                    curr = curr.nextNode;
                    curr.lock();
                    tempPeekNode = curr.nextNode;
                }

                // System.out.println("removing value: " + curr.presentValue);
                removedVal = curr.presentValue;

                pred.setNextNode(tempPeekNode);
            } finally {
                curr.unlock();
            }

            return removedVal;
        } finally {
            pred.unlock();
        }
    }

    public boolean contains(int lookupVal) {
        head.lock();
        Node pred = head;
        Node curr = pred.nextNode;
        curr.lock();

        try {
            try {
                while (curr.presentValue < lookupVal) {
                    pred.unlock();
                    pred = curr;
                    curr = curr.nextNode;
                    curr.lock();
                }

                // System.out.println("contains curr value: " + curr.presentValue);
                if (curr.presentValue == lookupVal) {
                    return true;
                }
                return false;

            } finally {
                curr.unlock();
            }
        } finally {
            pred.unlock();
        }
    }

}
