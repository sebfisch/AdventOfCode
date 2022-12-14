package year2022.day14;

// JEP 395: Records

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class Part2 {
    public static void main(String[] args) {
        try (Stream<String> lines = inputLines()) {
            Cave cave = new Cave();
            lines.forEach(line -> addBlocksAlongLines(cave, line.split(" -> ")));
            System.out.println(cave.countFittingBlocksFallingFrom(new Position(500, 0)));
        }
    }

    static class Cave {
        Set<Position> blocked;
        int maxY;

        Cave() {
            this.blocked = new HashSet<>();
            this.maxY = -1;
        }

        boolean isFree(Position pos) {
            return !blocked.contains(pos) && !isOnFloor(pos);
        }

        boolean isOnFloor(Position pos) {
            return pos.y == maxY() + 2;
        }

        Position fallen(Position block) {
            Position pos = fallenOneStep(block);
            while (pos != null) {
                block = pos;
                pos = fallenOneStep(block);
            }
            return block;
        }

        int countFittingBlocksFallingFrom(Position start) {
            int count = 0;

            Position end = fallen(start);
            while (end != start) {
                count++;
                blocked.add(end);
                end = fallen(start);
            }

            return count + 1;
        }

        // from here on identical to part 1

        int maxY() {
            if (maxY < 0) {
                maxY = blocked.stream().map(Position::y).max(Comparator.naturalOrder()).orElseThrow();
            }
            return maxY;
        }

        void blockPositions(Collection<Position> positions) {
            this.blocked.addAll(positions);
        }

        static final List<Position> FALLING_DIRECTIONS = List.of(
                new Position(0, 1),
                new Position(-1, 1),
                new Position(1, 1));

        // null means block has fallen all the way
        Position fallenOneStep(Position block) {
            return FALLING_DIRECTIONS.stream()
                    .map(block::plus)
                    .filter(this::isFree)
                    .findFirst()
                    .orElse(null);
        }
    }

    static void addBlocksAlongLines(Cave cave, String[] points) {
        List<Position> path = new LinkedList<>();
        for (String point : points) {
            String[] coordinates = point.split(",");
            path.add(new Position(Integer.parseInt(coordinates[0]), Integer.parseInt(coordinates[1])));
        }
        for (int pathIndex = 1; pathIndex < path.size(); pathIndex++) {
            cave.blockPositions(line(path.get(pathIndex - 1), path.get(pathIndex)).toList());
        }
    }

    static Stream<Position> line(Position from, Position to) {
        Position step = new Position((int) Math.signum(to.x - from.x), (int) Math.signum(to.y - from.y));
        return Stream.concat(
                Stream.iterate(from, pos -> !pos.equals(to), step::plus),
                Stream.of(to));
    }

    record Position(int x, int y) {
        Position plus(Position that) {
            return new Position(this.x + that.x, this.y + that.y);
        }
    }

    static Stream<String> inputLines() {
        return new BufferedReader(new InputStreamReader(System.in)).lines();
    }
}
