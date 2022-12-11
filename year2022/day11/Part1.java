package year2022.day11;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Part1 {
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
        static final int NUMBER_OF_ROUNDS = 20;

        private List<Monkey> monkeys;
        private Map<Integer, Integer> inspectionCounts;

        KeepAway(List<Monkey> monkeys) {
            this.monkeys = monkeys;
            this.inspectionCounts = new HashMap<>();
        }

        int monkeyBusiness() {
            List<Integer> counts = new ArrayList<>(inspectionCounts.values());
            Collections.sort(counts);

            int fst = counts.get(counts.size() - 1);
            int snd = counts.get(counts.size() - 2);

            return fst * snd;
        }

        void play() {
            for (int round = 1; round <= NUMBER_OF_ROUNDS; round++) {
                for (int monkey = 0; monkey < monkeys.size(); monkey++) {
                    inspectionCounts.merge(monkey, playTurn(monkey), (x, y) -> x + y);
                }
            }
        }

        int playTurn(int monkeyIndex) {
            Monkey monkey = monkeys.get(monkeyIndex);
            int inspectionCount = 0;
            while (!monkey.items().isEmpty()) {
                inspectionCount++;
                int oldWorryLevel = monkey.items().removeFirst();
                int newWorryLevel = monkey.operation().value(oldWorryLevel);
                newWorryLevel /= 3;
                int nextMonkey = monkey.nextMonkey(newWorryLevel);
                monkeys.get(nextMonkey).items().addLast(newWorryLevel);
            }
            return inspectionCount;
        }
    }

    record Monkey(Deque<Integer> items, Exp operation, int divisor, int ifTrue, int ifFalse) {
        int nextMonkey(int worryLevel) {
            return worryLevel % divisor == 0 ? ifTrue : ifFalse;
        }

        static Monkey parse(Scanner input) {
            input.nextLine(); // skip over monkey number

            Deque<Integer> items = parseStartingItems(input);
            Exp operation = parseOperation(input);
            int divisor = parseNumberAtWord(3, input);
            int ifTrue = parseNumberAtWord(5, input);
            int ifFalse = parseNumberAtWord(5, input);

            if (input.hasNext()) {
                input.nextLine(); // skip over empty line
            }

            return new Monkey(items, operation, divisor, ifTrue, ifFalse);
        }

        static Deque<Integer> parseStartingItems(Scanner input) {
            String[] parts = input.nextLine().split(": ");
            return new LinkedList<>(Arrays.stream(parts[1].split(", "))
                    .map(Integer::parseInt)
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
        record Num(int value) implements Exp {
        }

        record Old() implements Exp {
        }

        record Plus(Exp fst, Exp snd) implements Exp {
        }

        record Times(Exp fst, Exp snd) implements Exp {
        }

        default int value(int old) {
            return switch (this) {
                case Num(int value) -> value;
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
                    default -> new Num(Integer.parseInt(words[0]));
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
