package year2022.day03;

// JEP 395: Records

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

class Part1 {
    public static void main(String[] args) {
        try (Stream<String> lines = inputLines()) {
            System.out.println(totalPriorities(lines));
        }
    }

    static int totalPriorities(Stream<String> lines) {
        return lines
                .map(Rucksack::fromString)
                .map(Rucksack::uniqueDuplicateItem)
                .map(Part1::priority)
                .reduce(0, (x, y) -> x + y);
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

        static Set<Character> chars(String string) {
            return IntStream
                    .range(0, string.length())
                    .mapToObj(string::charAt)
                    .collect(Collectors.toSet());
        }
    }

    static int priority(char item) {
        if (Character.isLowerCase(item)) {
            return (int) item - (int) 'a' + 1;
        }
        return (int) item - (int) 'A' + 27;
    }

    static Stream<String> inputLines() {
        return new BufferedReader(new InputStreamReader(System.in)).lines();
    }
}