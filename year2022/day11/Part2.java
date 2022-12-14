package year2022.day11;

// JEP 361: Switch Expressions
// JEP 395: Records
// JEP 405: Record Patterns (Preview)
// JEP 409: Sealed Classes
// JEP 427: Pattern Matching for switch (Third Preview)

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Part2 {
    public static void main(String[] args) {
        try (Scanner input = new Scanner(System.in)) {
            List<Monkey> monkeys = new ArrayList<>();

            while (input.hasNext()) {
                monkeys.add(Monkey.parse(input));
            }

            KeepAway game = new KeepAway(monkeys);
            game.play();

            System.out.println(game.monkeyBusiness());
        }
    }

    static class KeepAway {
        static final int NUMBER_OF_ROUNDS = 10000;

        private List<Monkey> monkeys;
        private Map<Integer, Long> inspectionCounts;
        private long divisorProduct; // could use least common multiple too

        KeepAway(List<Monkey> monkeys) {
            this.monkeys = monkeys;
            this.inspectionCounts = new HashMap<>();
            this.divisorProduct = monkeys.stream()
                    .map(Monkey::divisor)
                    .reduce(1l, (x, y) -> x * y);
        }

        long monkeyBusiness() {
            List<Long> counts = new ArrayList<>(inspectionCounts.values());
            Collections.sort(counts);

            long fst = counts.get(counts.size() - 1);
            long snd = counts.get(counts.size() - 2);

            return fst * snd;
        }

        void play() {
            for (int round = 1; round <= NUMBER_OF_ROUNDS; round++) {
                for (int monkey = 0; monkey < monkeys.size(); monkey++) {
                    inspectionCounts.merge(monkey, playTurn(monkey), (x, y) -> x + y);
                }
            }
        }

        long playTurn(int monkeyIndex) {
            Monkey monkey = monkeys.get(monkeyIndex);
            long inspectionCount = 0;
            while (!monkey.items().isEmpty()) {
                inspectionCount++;
                long oldWorryLevel = monkey.items().removeFirst();
                long newWorryLevel = monkey.operation().value(oldWorryLevel);
                newWorryLevel %= divisorProduct;
                int nextMonkey = monkey.nextMonkey(newWorryLevel);
                monkeys.get(nextMonkey).items().addLast(newWorryLevel);
            }
            return inspectionCount;
        }
    }

    record Monkey(Deque<Long> items, Exp operation, long divisor, int ifTrue, int ifFalse) {
        int nextMonkey(long worryLevel) {
            return worryLevel % divisor == 0 ? ifTrue : ifFalse;
        }

        static Monkey parse(Scanner input) {
            input.nextLine(); // skip over monkey number

            Deque<Long> items = parseStartingItems(input);
            Exp operation = parseOperation(input);
            long divisor = (long) parseNumberAtWord(3, input);
            int ifTrue = parseNumberAtWord(5, input);
            int ifFalse = parseNumberAtWord(5, input);

            if (input.hasNext()) {
                input.nextLine(); // skip over empty line
            }

            return new Monkey(items, operation, divisor, ifTrue, ifFalse);
        }

        static Deque<Long> parseStartingItems(Scanner input) {
            String[] parts = input.nextLine().split(": ");
            return new LinkedList<>(Arrays.stream(parts[1].split(", "))
                    .map(Long::parseLong)
                    .toList());
        }

        private static Exp parseOperation(Scanner input) {
            return Exp.fromString(input.nextLine().split(" = ")[1]);
        }

        private static int parseNumberAtWord(int wordIndex, Scanner input) {
            return Integer.parseInt(input.nextLine().trim().split(" ")[wordIndex]);
        }
    }

    sealed interface Exp {
        record Num(long value) implements Exp {
        }

        record Old() implements Exp {
        }

        record Plus(Exp fst, Exp snd) implements Exp {
        }

        record Times(Exp fst, Exp snd) implements Exp {
        }

        default long value(long old) {
            return switch (this) {
                case Num(var value) -> value;
                case Old() -> old;
                case Plus(var fst,var snd) -> fst.value(old) + snd.value(old);
                case Times(var fst,var snd) -> fst.value(old) * snd.value(old);
            };
        }

        static Exp fromString(String string) {
            String[] words = string.split(" ");

            if (words.length == 1) {
                return switch (words[0]) {
                    case "old" -> new Old();
                    default -> new Num(Long.parseLong(words[0]));
                };
            }

            return switch (words[1]) {
                case "+" -> new Plus(Exp.fromString(words[0]), Exp.fromString(words[2]));
                case "*" -> new Times(Exp.fromString(words[0]), Exp.fromString(words[2]));
                default -> throw new IllegalArgumentException(
                        "invalid expression: %s".formatted(string));
            };
        }
    }
}
