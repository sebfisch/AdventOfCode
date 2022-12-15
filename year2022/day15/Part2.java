package year2022.day15;

// JEP 395: Records

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class Part2 {
    static final int MAX_XY = 4000000;

    public static void main(String[] args) {
        try (Stream<String> lines = inputLines()) {
            List<Sensor> sensors = lines
                    .map(Sensor::fromString)
                    .toList();
            Position free = uncoveredPosition(sensors);
            System.out.println((long) MAX_XY * free.x + free.y);
        }
    }

    static Position uncoveredPosition(List<Sensor> sensors) {
        for (int row = 0; row <= MAX_XY; row++) {
            final int y = row;
            List<Interval> sortedIntervals = sensors.stream()
                    .map(sensor -> sensor.coveredInRow(y))
                    .flatMap(Optional::stream)
                    .sorted()
                    .toList();

            int rightmostCovered = -1;
            for (Interval covered : sortedIntervals) {
                if (covered.lowerBound > rightmostCovered + 1) {
                    return new Position(rightmostCovered + 1, y);
                } else {
                    rightmostCovered = Math.max(rightmostCovered, covered.upperBound);
                }
            }
        }

        throw new IllegalStateException("found no uncovered position");
    }

    record Sensor(Position position, Position closestBeacon) {
        Optional<Interval> coveredInRow(int y) {
            int yDistance = Math.abs(y - position.y);

            if (range() < yDistance) {
                return Optional.empty();
            }

            if (range() == yDistance) {
                return Optional.of(new Interval(position.x, position.x));
            }

            int xDistance = range() - yDistance;
            int lowerBound = Math.max(0, position.x - xDistance);
            int upperBound = Math.min(MAX_XY, position.x + xDistance);

            if (lowerBound <= upperBound) {
                return Optional.of(new Interval(lowerBound, upperBound));
            } else {
                return Optional.empty();
            }
        }

        int range() {
            return position.manhattanDistance(closestBeacon);
        }

        static Sensor fromString(String line) {
            String[] parts = line.split("[:,=]");
            Position position = new Position(
                    Integer.parseInt(parts[1]),
                    Integer.parseInt(parts[3]));
            Position closestBeacon = new Position(
                    Integer.parseInt(parts[5]),
                    Integer.parseInt(parts[7]));
            return new Sensor(position, closestBeacon);
        }
    }

    record Interval(int lowerBound, int upperBound) implements Comparable<Interval> {
        public int compareTo(Interval that) {
            return Comparator.comparingInt(Interval::lowerBound).thenComparing(
                    Comparator.comparingInt(Interval::upperBound))
                    .compare(this, that);
        }
    }

    record Position(int x, int y) {
        int manhattanDistance(Position that) {
            return Math.abs(this.x - that.x) + Math.abs(this.y - that.y);
        }
    }

    static Stream<String> inputLines() {
        return new BufferedReader(new InputStreamReader(System.in)).lines();
    }
}
