package year2022.day04;

// JEP 395: Records

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Part2 {
    public static void main(String[] args) {
        try (Stream<String> lines = inputLines()) {
            System.out.println(mostInNeedOfConsideration(lines));
        }
    }

    static long mostInNeedOfConsideration(Stream<String> lines) {
        return lines
                .map(SectionAssignmentPair::fromString)
                .filter(SectionAssignmentPair::overlaps)
                .count();
    }

    record SectionAssignmentPair(Set<Integer> fst, Set<Integer> snd) {
        static SectionAssignmentPair fromString(String line) {
            String[] assignments = line.split(",");
            return new SectionAssignmentPair(range(assignments[0]), range(assignments[1]));
        }

        boolean overlaps() {
            return !intersection(fst, snd).isEmpty();
        }

        static Set<Integer> range(String description) {
            String[] bounds = description.split("-");
            return IntStream
                    .range(Integer.parseInt(bounds[0]), Integer.parseInt(bounds[1]) + 1)
                    .boxed()
                    .collect(Collectors.toSet());
        }

        static <T> Set<T> intersection(Set<T> fstSet, Set<T> sndSet) {
            Set<T> result = new HashSet<>(fstSet);
            result.retainAll(sndSet);
            return result;
        }
    }

    static Stream<String> inputLines() {
        return new BufferedReader(new InputStreamReader(System.in)).lines();
    }
}
