package year2022.day02;

// JEP 395: Records

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

public class Part2 {
    public static void main(String[] args) {
        try (Stream<String> lines = inputLines()) {
            System.out.println(expectedScore(lines));
        }
    }

    static int expectedScore(Stream<String> lines) {
        return lines
                .map(Round::fromString)
                .map(Round::score)
                .reduce(0, (x, y) -> x + y);
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
            return Arrays.stream(Choice.values())
                    .filter(choice -> choice.code.equals(code))
                    .findAny().orElseThrow();
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
            return Arrays.stream(Outcome.values())
                    .filter(outcome -> outcome.code.equals(code))
                    .findAny().orElseThrow();
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
            return Arrays.stream(Choice.values())
                    .filter(choice -> outcome(choice).equals(suggestedOutcome))
                    .findAny().orElseThrow();
        }

        int score() {
            return myChoice().score + suggestedOutcome.score;
        }
    }

    static Stream<String> inputLines() {
        return new BufferedReader(new InputStreamReader(System.in)).lines();
    }
}
