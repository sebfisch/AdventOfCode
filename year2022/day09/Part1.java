package year2022.day09;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public class Part1 {
    public static void main(String[] args) {
        try (Stream<String> lines = inputLines()) {
            System.out.println(tailVisitedCount(lines));
        }
    }

    static int tailVisitedCount(Stream<String> lines) {
        Set<Position> visitedByTail = new HashSet<>();
        visitedByTail.add(Position.START);

        Rope rope = new Rope();
        lines.map(Movement::fromString).forEachOrdered(move -> {
            visitedByTail.addAll(rope.stepwiseTailPositions(move));
        });

        return visitedByTail.size();
    }

    enum Direction {
        LEFT("L"), RIGHT("R"), UP("U"), DOWN("D");

        private String label;

        Direction(String label) {
            this.label = label;
        }

        static Direction fromString(String label) {
            return Arrays.stream(Direction.values())
                    .filter(dir -> dir.label.equals(label))
                    .findAny().orElseThrow();
        }
    }

    record Position(int x, int y) {
        static final Position START = new Position(0, 0);

        Position moved(Direction dir) {
            return switch (dir) {
                case LEFT -> new Position(x - 1, y);
                case RIGHT -> new Position(x + 1, y);
                case UP -> new Position(x, y - 1);
                case DOWN -> new Position(x, y + 1);
            };
        }

        boolean isInSameRowAs(Position that) {
            return this.y == that.y;
        }

        boolean isInSameColAs(Position that) {
            return this.x == that.x;
        }

        boolean isAdjacentTo(Position that) {
            return Math.abs(this.x - that.x) <= 1 && Math.abs(this.y - that.y) <= 1;
        }
    }

    record Movement(Direction direction, int steps) {
        static Movement fromString(String string) {
            String[] parts = string.split(" ");

            return new Movement(
                    Direction.fromString(parts[0]),
                    Integer.parseInt(parts[1]));
        }
    }

    static class Rope {
        private Position head;
        private Position tail;

        Rope() {
            head = Position.START;
            tail = head;
        }

        Set<Position> stepwiseTailPositions(Movement move) {
            Set<Position> result = new HashSet<>();

            for (int ignored = 0; ignored < move.steps; ignored++) {
                pullHead(move.direction);
                result.add(tail);
            }

            return result;
        }

        void pullHead(Direction dir) {
            head = head.moved(dir);
            tail = pullNext(head, tail);
        }

        static Position pullNext(Position fst, Position snd) {
            if (fst.isAdjacentTo(snd)) {
                return snd;
            }

            // at least one of the following conditions is true (maybe both)

            if (!fst.isInSameColAs(snd)) {
                snd = snd.moved(fst.x < snd.x ? Direction.LEFT : Direction.RIGHT);
            }

            if (!fst.isInSameRowAs(snd)) {
                snd = snd.moved(fst.y < snd.y ? Direction.UP : Direction.DOWN);
            }

            return snd;
        }
    }

    static Stream<String> inputLines() {
        return new BufferedReader(new InputStreamReader(System.in)).lines();
    }
}
