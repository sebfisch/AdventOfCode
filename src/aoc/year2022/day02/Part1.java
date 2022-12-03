package aoc.year2022.day02;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

class Part1 {
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
        ROCK(1), PAPER(2), SCISSORS(3);

        final int score;

        Choice(int score) {
            this.score = score;
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
        WIN(6), DRAW(3), LOSS(0);

        final int score;

        Outcome(int score) {
            this.score = score;
        }
    }

    record Round(Choice their, Choice my) {
        static final Map<String, Choice> CHOICES = Map.of(
                "A", Choice.ROCK,
                "B", Choice.PAPER,
                "C", Choice.SCISSORS,
                "X", Choice.ROCK,
                "Y", Choice.PAPER,
                "Z", Choice.SCISSORS);

        static Round fromString(String line) {
            String[] codes = line.split(" ");
            return new Round(CHOICES.get(codes[0]), CHOICES.get(codes[1]));
        }

        Outcome outcome() {
            if (Objects.equals(their, my)) {
                return Outcome.DRAW;
            }
            return my.beats(their) ? Outcome.WIN : Outcome.LOSS;
        }

        int score() {
            return my.score + outcome().score;
        }
    }
}
