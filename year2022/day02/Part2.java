package year2022.day02;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

public class Part2 {
    public static void main(String[] args) {
        try (Stream<String> lines = inputLines()) {
            int expectedScore = lines
                    .map(Round::fromString)
                    .map(Round::score)
                    .reduce(0, (x, y) -> x + y);
            System.out.println(expectedScore);
        }
    }

    private static Stream<String> inputLines() {
        return new BufferedReader(new InputStreamReader(System.in)).lines();
    }

    enum Choice {
        ROCK("A", 1), PAPER("B", 2), SCISSORS("C", 3);

        final String code;
        final int score;

        Choice(String code, int score) {
            this.code = code;
            this.score = score;
        }

        static Choice fromString(String code) {
            for (Choice choice : Choice.values()) {
                if (choice.code.equals(code)) {
                    return choice;
                }
            }
            throw new IllegalArgumentException("invalid code %s".formatted(code));
        }

        static final Map<Choice, Choice> BEATS = Map.of(
                Choice.ROCK, Choice.SCISSORS,
                Choice.PAPER, Choice.ROCK,
                Choice.SCISSORS, Choice.PAPER);

        boolean beats(Choice that) {
            return Objects.equals(that, BEATS.get(this));
        }
    }

    enum Outcome {
        LOSS("X", 0), DRAW("Y", 3), WIN("Z", 6);

        final String code;
        final int score;

        Outcome(String code, int score) {
            this.code = code;
            this.score = score;
        }

        static Outcome fromString(String code) {
            for (Outcome outcome : Outcome.values()) {
                if (outcome.code.equals(code)) {
                    return outcome;
                }
            }
            throw new IllegalArgumentException("invalid code %s".formatted(code));
        }
    }

    record Round(Choice theirChoice, Outcome suggestedOutcome) {
        static Round fromString(String line) {
            String[] codes = line.split(" ");
            return new Round(Choice.fromString(codes[0]), Outcome.fromString(codes[1]));
        }

        Outcome outcome(Choice myChoice) {
            if (Objects.equals(theirChoice, myChoice)) {
                return Outcome.DRAW;
            }
            return myChoice.beats(theirChoice) ? Outcome.WIN : Outcome.LOSS;
        }

        Choice myChoice() {
            for (Choice choice : Choice.values()) {
                if (outcome(choice).equals(suggestedOutcome)) {
                    return choice;
                }
            }
            return null;
        }

        int score() {
            return myChoice().score + suggestedOutcome.score;
        }
    }
}
