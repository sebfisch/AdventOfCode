package aoc.y2022.day03;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class Part1 {
    public static void main(String[] args) {
        try (Stream<String> lines = inputLines()) {
            lines
                    .map(Rucksack::fromString)
                    .map(Rucksack::duplicateItem)
                    .map(Rucksack::priority)
                    .reduce(0, (x, y) -> x + y);
        }
    }

    private static Stream<String> inputLines() {
        return new BufferedReader(new InputStreamReader(System.in)).lines();
    }

    record Rucksack(Set<Character> fstCompartment, Set<Character> sndCompartment) {
        static Rucksack fromString(String items) {
            int count = items.length();
            return new Rucksack(
                    chars(items.substring(0, count / 2)),
                    chars(items.substring(count / 2)));
        }

        private static Set<Character> chars(String string) {
            Stream.Builder<Character> builder = Stream.builder();
            for (int index = 0; index < string.length(); index++) {
                builder.add(string.charAt(index));
            }
            return builder.build().collect(Collectors.toSet());
        }
    }
}