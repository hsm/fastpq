package dk.util.fastpq;

import java.util.ArrayList;
import java.util.List;

public class Group {
    private List<List<Integer>> slots = new ArrayList<>();
    private int firstEmpty = 0;

    public Group(int numberOfSlots) {
        for (int i = 0; i < numberOfSlots; i++) {
            slots.add(new ArrayList<>());
        }
    }

    public List<Integer> getNextEmptySlot() {
        return firstEmpty >= slots.size() ? null : slots.get(firstEmpty++);
    }
}
