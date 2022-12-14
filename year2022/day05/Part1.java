package year2022.day05;

// JEP 395: Records

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Part1 {
    public static void main(String[] args) {
        try (Scanner input = new Scanner(System.in)) {
            Arrangement arrangement = Arrangement.parse(input);

            while (input.hasNext()) {
                arrangement.moveOneAtATime(Step.fromString(input.nextLine()));
            }

            System.out.println(arrangement.topCrates());
        }
    }

    record Step(int count, int source, int target) {
        static Step fromString(String line) {
            String[] words = line.split(" ");
            return new Step(
                    Integer.parseInt(words[1]),
                    Integer.parseInt(words[3]),
                    Integer.parseInt(words[5]));
        }
    }

    static class CrateStack extends LinkedList<Character> {
    }

    record Arrangement(List<CrateStack> stacks) {
        String topCrates() {
            return stacks.stream()
                    .map(CrateStack::getLast)
                    .map(String::valueOf)
                    .collect(Collectors.joining());
        }

        void moveOneAtATime(Step step) {
            for (int ignored = 0; ignored < step.count; ignored++) {
                stacks.get(step.target - 1).addLast(stacks.get(step.source - 1).removeLast());
            }
        }

        static Arrangement parse(Scanner input) {
            List<String> lines = linesUntilEmpty(input);
            int stackCount = numberOfStacks(lines); // removes last line
            List<CrateStack> stacks = emptyStacks(stackCount);

            for (String line : lines) {
                addCrates(stackCount, stacks, line);
            }

            return new Arrangement(stacks);
        }

        static List<String> linesUntilEmpty(Scanner input) {
            String line = input.nextLine();

            // skip initial empty lines
            while (line.trim().isEmpty()) {
                line = input.nextLine();
            }

            List<String> lines = new ArrayList<>();
            while (!line.trim().isEmpty()) {
                lines.add(line);
                line = input.nextLine();
            }

            return lines;
        }

        static int numberOfStacks(List<String> lines) {
            String[] stackIds = lines.remove(lines.size() - 1).trim().split(" ");
            return Integer.parseInt(stackIds[stackIds.length - 1]);
        }

        static List<CrateStack> emptyStacks(int stackCount) {
            return Stream.generate(CrateStack::new).limit(stackCount).toList();
        }

        static void addCrates(int stackCount, List<CrateStack> stacks, String line) {
            for (int stackIndex = 0; stackIndex < stackCount; stackIndex++) {
                char crateId = line.charAt(1 + 4 * stackIndex);
                if (!Character.isWhitespace(crateId)) {
                    stacks.get(stackIndex).addFirst(crateId);
                }
            }
        }
    }
}
