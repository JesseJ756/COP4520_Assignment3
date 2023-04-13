Problem 1: The Birthday Presents Party

How to run through terminal.

- Navigate to the src->Problem1 directory
- Run "javac BirthdayPresentParty.java" then "java BirthdayPresentParty"

- Output to terminal shows the count of all presents removed and the total time to complete the task

For this problem, the solution takes 3 different files:

- BirthdayPresentParty.java stores the main function that initiallizes the linked list and starts the threads(servant)
- BPPThread.java which adds and removes the present ID to the linked list for a range given the servant
- ConcurrentLinkedList.java which has all the methods to initialize, add, remove, and determines whether an ID is contained in the linked list.

When adding to the linked list, the servant will lock the head of the linked list first then move "hand-over-hand" through the list, locking and unlocking the nodes, until they find a node whos value is greater than the ID trying to be added. Then the node is inserted and the precceding node is linked to the new one and the new one adds the current node as its next node.

For deleting, since any value could be taken out from the list, I decided to remove the second to last node. Since each servant alternates between adding and removing a node, the list would be short and after running it through a couple of times, it seems that it didnt create too much of a bottleneck.

One thing that could have gone wrong with how the servants solved the problem is that their removal of a ID wasnt thread safe. From the stories perspective it would be like a servant looking into a bag and writing a thank you note while the ID is still in the bag. Another servant views the same ID and writes a thank you card causing two servant to do the same action for one ID. To improve this strategy, a servant should take the ID completly out of the back by locking the ID so that no other servant can obtain the same one.

---

Problem 2: Atmospheric Temperature Reading Module

How to run through terminal.

- Navigate to the src->Problem2 directory
- Run "javac AtmTempReading.java" then "java AtmTempReading"

For the implementation of solving this problem, I used the PriorityBlockingQueue to store the highest, lowest, and largest interval values. Using this type of queue, I used it as a max and min heap to keep the largest and lowest values sorted. As for efficiency, the time complexity to insert into a heap is O(log n) and inserting n values is O(nlogn) so it is fairly efficient.
Since this is a blocked queue, whenever a value is inserted or taken, the other threads are blocked so the heap will remain corrupt free and therefor the queue will remain correct even with 8 threads.
In the real scenario, where a reading is taken every minute, there is plenty of time for all the threads to insert their values. Testing the program shows that on average, inserting 8 values at a time for a total of 480 recordings, only takes on average 150 milliseconds.
