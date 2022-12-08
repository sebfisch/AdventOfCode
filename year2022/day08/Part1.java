package year2022.day08;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Part1 {
    public static void main(String[] args) {
        try (Stream<String> lines = inputLines()) {
            Forest forest = new Forest(lines.toList());
            Set<Position> visible = new HashSet<>();

            for (Direction direction : Direction.values()) {
                addVisibleFrom(forest, visible, direction);
            }

            System.out.println(visible.size());
        }
    }

    static void addVisibleFrom(Forest forest, Set<Position> visible, Direction direction) {
        forest.edge(direction).forEach(blocking -> {
            visible.add(blocking);
            Position current = blocking.next(direction.opposite());
            while (forest.height(blocking) < 9 && forest.contains(current)) {
                if (forest.height(current) > forest.height(blocking)) {
                    visible.add(current);
                    blocking = current;
                } else {
                    current = current.next(direction.opposite());
                }
            }
        });
    }

    enum Direction {
        NORTH, EAST, SOUTH, WEST;

        Direction opposite() {
            return switch (this) {
                case NORTH -> Direction.SOUTH;
                case EAST -> Direction.WEST;
                case SOUTH -> Direction.NORTH;
                case WEST -> Direction.EAST;
            };
        }
    }

    record Position(int x, int y) {
        Position next(Direction direction) {
            return switch (direction) {
                case NORTH -> new Position(x, y - 1);
                case EAST -> new Position(x + 1, y);
                case SOUTH -> new Position(x, y + 1);
                case WEST -> new Position(x - 1, y);
            };
        }
    }

    static class Forest {
        private List<String> trees;

        Forest(List<String> trees) {
            this.trees = trees;
        }

        int width() {
            return trees.get(0).length();
        }

        int length() {
            return trees.size();
        }

        int height(Position pos) {
            return Integer.parseInt(Character.toString(trees.get(pos.y).charAt(pos.x)));
        }

        boolean contains(Position pos) {
            return 0 <= pos.x && pos.x < width() && 0 <= pos.y && pos.y < length();
        }

        Stream<Position> edge(Direction direction) {
            return switch (direction) {
                case NORTH -> IntStream
                        .range(0, width())
                        .mapToObj(x -> new Position(x, 0));
                case EAST -> IntStream
                        .range(0, length())
                        .mapToObj(y -> new Position(width() - 1, y));
                case SOUTH -> IntStream
                        .range(0, width())
                        .mapToObj(x -> new Position(x, length() - 1));
                case WEST -> IntStream.range(0, length())
                        .mapToObj(y -> new Position(0, y));
            };
        }
    }

    static Stream<String> inputLines() {
        return new BufferedReader(new InputStreamReader(System.in)).lines();
    }
}