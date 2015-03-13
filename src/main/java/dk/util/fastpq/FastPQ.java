package dk.util.fastpq;

import java.util.*;

public class FastPQ {

    private PriorityQueue<Integer> insertHeap;
    private List<Integer> deletionBuffer = new ArrayList<>();
    private List<List<Integer>> groupBuffers = new ArrayList<>();
    private List<Group> groups = new ArrayList<>();

    private int maxHeapSize; // m
    private int maxDeletionsBufferSize; // m' << m
    private int maxNumberOfSlots; // k

    public FastPQ(int maxHeapSize, int maxDeletionBufferSize, int maxNumberOfSlots) {
        this.maxHeapSize = maxHeapSize;
        this.maxDeletionsBufferSize = maxDeletionBufferSize;
        this.maxNumberOfSlots = maxNumberOfSlots;

        insertHeap = new PriorityQueue<>(maxHeapSize);
        deletionBuffer = new ArrayList<>(maxDeletionBufferSize);
    }

    public void insert(int x) {
        insertHeap.add(x);
        if (insertHeap.size() == maxHeapSize) {
            rearrangeDeckChairs();
        }
    }

    private void rearrangeDeckChairs() {
        List<Integer> fromInsertHeap = new ArrayList<>(insertHeap);
        insertHeap.clear();

        List<Integer> fromDeletionBuffer = new ArrayList<>(deletionBuffer);
        deletionBuffer.clear();

        if (groupBuffers.isEmpty()) {
            addNewGroup();
        }

        List<Integer> firstGroupBuffer = groupBuffers.get(0);
        List<Integer> fromGroupBuffer = new ArrayList<>(firstGroupBuffer);
        firstGroupBuffer.clear();

        List<Integer> merged = merge(fromInsertHeap, fromDeletionBuffer, fromGroupBuffer);
        for (int i = 0; i < merged.size(); i++) {
            int integer = merged.get(i);
            if (i < maxDeletionsBufferSize) {
                deletionBuffer.add(integer);
            } else if (i < maxDeletionsBufferSize + maxHeapSize) {
                firstGroupBuffer.add(integer);
            } else { // deletion buffer and group buffer full
                List<Integer> emptySlot = groups.get(0).getNextEmptySlot();
                if (emptySlot == null) {
                    throw new RuntimeException("We need to overflow to next group...");
                } else {
                    emptySlot.addAll(merged.subList(i, merged.size()));
                    break;
                }
            }
        }
    }

    private void addNewGroup() {
        groupBuffers.add(new ArrayList<>());
        groups.add(new Group(maxNumberOfSlots));
    }

    @SafeVarargs
    private final List<Integer> merge(List<Integer>... lists) {
        List<Integer> result = new ArrayList<>();
        for (List<Integer> list : lists) {
            result.addAll(list);
        }
        Collections.sort(result);
        return result;
    }


    public int deleteMin() {
        return insertHeap.remove();
    }

    public static void main(String[] args) {
        FastPQ fastPQ = new FastPQ(4, 2, 2);
        fastPQ.insert(5);
        fastPQ.insert(2);
        fastPQ.insert(4);
        fastPQ.insert(20);
        fastPQ.insert(12);
        fastPQ.insert(15);
        fastPQ.insert(17);
        fastPQ.insert(16);
        fastPQ.insert(-54);
        fastPQ.insert(12);


        for (int i = 0; i < 10; i++) {
            System.out.println(fastPQ.deleteMin());
        }



        // TODO
    }
}
