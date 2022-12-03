package aoc.year2022.day03;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

class Part1 {
    public static void main(String[] args) {
        try (Stream<String> lines = inputLines()) {
            int totalPriorities = lines
                    .map(Rucksack::fromString)
                    .map(Rucksack::uniqueDuplicateItem)
                    .map(Part1::priority)
                    .reduce(0, (x, y) -> x + y);
            System.out.println(totalPriorities);
        }
    }

    private static Stream<String> inputLines() {
        return new BufferedReader(new InputStreamReader(System.in)).lines();
    }

    private static int priority(char item) {
        if (Character.isLowerCase(item)) {
            return (int) item - (int) 'a' + 1;
        }
        return (int) item - (int) 'A' + 27;
    }

    record Rucksack(Set<Character> fstCompartment, Set<Character> sndCompartment) {
        static Rucksack fromString(String items) {
            int count = items.length();
            return new Rucksack(
                    chars(items.substring(0, count / 2)),
                    chars(items.substring(count / 2)));
        }

        Character uniqueDuplicateItem() {
            Set<Character> intersection = new HashSet<>(fstCompartment);
            intersection.retainAll(sndCompartment);
            return intersection.iterator().next();
        }

        private static Set<Character> chars(String string) {
            Set<Character> result = new HashSet<>();
            for (int index = 0; index < string.length(); index++) {
                result.add(string.charAt(index));
            }
            return result;
        }
    }
}