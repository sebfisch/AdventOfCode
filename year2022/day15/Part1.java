package year2022.day15;

// JEP 395: Records

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Part1 {
    static final int ROW = 2000000;

    public static void main(String[] args) {
        try (Stream<String> lines = inputLines()) {
            Stream<Sensor> sensors = lines.map(Sensor::fromString);
            int noBeacon = sensors
                    .flatMap(Sensor::relevantExcludedPositions)
                    .collect(Collectors.toSet())
                    .size();
            System.out.println(noBeacon);
        }
    }

    record Position(int x, int y) {
        Stream<Position> relevantSurroundingsUpTo(int distance) {
            int yDistance = Math.abs(ROW - this.y);

            if (yDistance > distance) {
                return Stream.of();
            }

            if (yDistance == distance) {
                return Stream.of(new Position(this.x, ROW));
            }

            int xDistance = distance - yDistance;
            return IntStream
                    .range(this.x - xDistance, this.x + xDistance + 1)
                    .mapToObj(x -> new Position(x, ROW));
        }

        Position plus(Position that) {
            return new Position(this.x + that.x, this.y + that.y);
        }

        int manhattanDistance(Position that) {
            return Math.abs(this.x - that.x) + Math.abs(this.y - that.y);
        }
    }

    record Sensor(Position position, Position closestBeacon) {
        Stream<Position> relevantExcludedPositions() {
            return position
                    .relevantSurroundingsUpTo(position.manhattanDistance(closestBeacon))
                    .filter(pos -> !pos.equals(closestBeacon));
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

    static Stream<String> inputLines() {
        return new BufferedReader(new InputStreamReader(System.in)).lines();
    }
}
