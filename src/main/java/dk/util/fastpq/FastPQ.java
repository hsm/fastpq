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
                Group firstGroup = groups.get(0);
                List<Integer> tail = merged.subList(i, merged.size());
                if (firstGroup.hasEmptySlots()) {
                    firstGroup.getNextEmptySlot().addAll(tail);
                } else {
                    int lastGroupTouchedIndex = pushRight(tail, 0);
                    transferGroupBuffersToFirstGroup(lastGroupTouchedIndex);
                }
                break;
            }
        }
    }

    private void transferGroupBuffersToFirstGroup(int toGroupBufferIndexInclusive) {
        Group firstGroup = groups.get(0);
        for (int i = 0; i <= toGroupBufferIndexInclusive; i++) {
            List<Integer> groupBuffer = groupBuffers.get(i);
            if (!groupBuffer.isEmpty()) {
                if (!firstGroup.hasEmptySlots()) {
                    pushRight(new ArrayList<>(), 0);
                }
                firstGroup.getNextEmptySlot().addAll(groupBuffer);
                groupBuffer.clear();

            }
        }
    }

    private void addNewGroup() {
        groupBuffers.add(new ArrayList<>());
        groups.add(new Group(maxNumberOfSlots));
    }

    @SafeVarargs
    private final List<Integer> merge(List<Integer>... lists) {
        // TODO implement merge
        List<Integer> result = new ArrayList<>();
        for (List<Integer> list : lists) {
            result.addAll(list);
        }
        Collections.sort(result);
        return result;
    }

    private int pushRight(List<Integer> list, int fromGroupIndex) {
        int toGroupIndex = fromGroupIndex + 1;
        if (toGroupIndex >= groupBuffers.size()) {
            assert (toGroupIndex == groupBuffers.size());
            addNewGroup();
        }
        Group fromGroup = groups.get(fromGroupIndex);
        List<Integer> payload = fromGroup.getMerged();
        assert (payload.size() <= maxHeapSize * Math.pow(maxNumberOfSlots, toGroupIndex));
        assert (!fromGroup.hasEmptySlots());
        fromGroup.clear();
        if (!list.isEmpty()) {
            fromGroup.getNextEmptySlot().addAll(list);
        }
        Group toGroup = groups.get(toGroupIndex);
        if (toGroup.hasEmptySlots()) {
            toGroup.getNextEmptySlot().addAll(payload);
        } else {
            return pushRight(payload, toGroupIndex);
        }
        return toGroupIndex;
    }

    public int deleteMin() {
        return insertHeap.remove();
    }
}
