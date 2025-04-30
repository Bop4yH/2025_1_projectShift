package ru.shift.model;

import java.util.*;

public class RandomGenerator {
    private RandomGenerator() {
    }

    //fast and fair
    public static Iterator<Integer> generateOrderedUniqueRandomNumbersExcluding(int amount, int range, int excluded) {
        TreeSet<Integer> uniqueRandomNumbers = new TreeSet<>();
        List<Integer> availableNumbers = new ArrayList<>();

        for (int i = 0; i < range; i++) {
            if (i != excluded) {
                availableNumbers.add(i);
            }
        }

        Collections.shuffle(availableNumbers);
        for (int i = 0; i < amount; i++) {
            uniqueRandomNumbers.add(availableNumbers.get(i));
        }

        return uniqueRandomNumbers.iterator();
    }
}
