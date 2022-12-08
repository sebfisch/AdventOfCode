package year2022.day08;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Part2 {
    public static void main(String[] args) {
        try (Stream<String> lines = inputLines()) {
            System.out.println(highestScenicScore(lines));
        }
    }

    static int highestScenicScore(Stream<String> lines) {
        Forest forest = new Forest(lines.toList());

        return forest
                .treePositions()
                .map(forest::scenicScore)
                .max(Comparator.naturalOrder())
                .orElseThrow();
    }

    enum Direction {
        NORTH, EAST, SOUTH, WEST
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

        Stream<Position> treePositions() {
            return IntStream
                    .range(0, length())
                    .mapToObj(y -> IntStream
                            .range(0, width())
                            .mapToObj(x -> new Position(x, y)))
                    .flatMap(ps -> ps);
        }

        int scenicScore(Position here) {
            int result = 1;

            for (Direction direction : Direction.values()) {
                result *= viewingDistance(here, direction);
            }

            return result;
        }

        int viewingDistance(Position here, Direction direction) {
            int result = 0;
            Position visible = here.next(direction);
            while (contains(visible)) {
                result++;
                if (height(here) <= height(visible)) {
                    break;
                }
                visible = visible.next(direction);
            }
            return result;
        }
    }

    static Stream<String> inputLines() {
        return new BufferedReader(new InputStreamReader(System.in)).lines();
    }
}
