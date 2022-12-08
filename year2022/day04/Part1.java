package year2022.day04;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Part1 {
    public static void main(String[] args) {
        try (Stream<String> lines = inputLines()) {
            System.out.println(mostInNeedOfConsideration(lines));
        }
    }

    static long mostInNeedOfConsideration(Stream<String> lines) {
        return lines
                .map(SectionAssignmentPair::fromString)
                .filter(SectionAssignmentPair::overlapsCompletely)
                .count();
    }

    record SectionAssignmentPair(Set<Integer> fst, Set<Integer> snd) {
        static SectionAssignmentPair fromString(String line) {
            String[] assignments = line.split(",");
            return new SectionAssignmentPair(range(assignments[0]), range(assignments[1]));
        }

        boolean overlapsCompletely() {
            return isSubset(fst, snd) || isSubset(snd, fst);
        }

        static Set<Integer> range(String description) {
            String[] bounds = description.split("-");
            return IntStream
                    .range(Integer.parseInt(bounds[0]), Integer.parseInt(bounds[1]) + 1)
                    .boxed()
                    .collect(Collectors.toSet());
        }

        static <T> boolean isSubset(Set<T> smallSet, Set<T> largeSet) {
            return smallSet.stream().allMatch(largeSet::contains);
        }
    }

    static Stream<String> inputLines() {
        return new BufferedReader(new InputStreamReader(System.in)).lines();
    }
}
