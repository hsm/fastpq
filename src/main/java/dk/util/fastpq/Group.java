package dk.util.fastpq;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
        return hasEmptySlots() ? slots.get(firstEmpty++) : null;
    }

    public boolean hasEmptySlots() {
        return firstEmpty < slots.size();
    }

    public List<Integer> getMerged() {
        // TODO implement merge
        List<Integer> result = new ArrayList<>();
        for (List<Integer> slot : slots) {
            result.addAll(slot);
        }
        Collections.sort(result);
        return result;
    }

    public void clear() {
        for (List<Integer> slot : slots) {
            slot.clear();
        }
        firstEmpty = 0;
    }
}
