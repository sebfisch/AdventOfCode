package year2022.day12;

// JEP 395: Records

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Stream;

public class Part1 {
    public static void main(String[] args) {
        try (Stream<String> lines = inputLines()) {
            System.out.println(new HeightMap(lines.toList()).lengthOfShortestPath());
        }
    }

    record Position(int x, int y) {
    }

    static class HeightMap {
        private List<String> lines;

        HeightMap(List<String> lines) {
            this.lines = lines;
        }

        boolean contains(Position pos) {
            return 0 <= pos.x && pos.x < lines.get(0).length() &&
                    0 <= pos.y && pos.y < lines.size();
        }

        Position position(char chr) {
            for (int row = 0; row < lines.size(); row++) {
                String line = lines.get(row);
                for (int col = 0; col < line.length(); col++) {
                    if (line.charAt(col) == chr) {
                        return new Position(col, row);
                    }
                }
            }
            throw new IllegalArgumentException("could not find %s position".formatted(chr));
        }

        int height(Position pos) {
            char chr = lines.get(pos.y).charAt(pos.x);

            if (chr == 'S') {
                return 0;
            }

            if (chr == 'E') {
                return 'z' - 'a';
            }

            return chr - 'a';
        }

        List<Position> predecessors(Position here) {
            return Stream.of(
                    new Position(here.x - 1, here.y),
                    new Position(here.x, here.y - 1),
                    new Position(here.x + 1, here.y),
                    new Position(here.x, here.y + 1))
                    .filter(this::contains)
                    .filter(there -> height(here) - height(there) <= 1)
                    .toList();
        }

        int lengthOfShortestPath() {
            return lengthOfShortestPath(position('S'), position('E'));
        }

        int lengthOfShortestPath(Position start, Position end) {
            Map<Position, Integer> distances = new HashMap<>();
            distances.put(end, 0);

            // Dijkstra's algorithm would be overkill because each step costs the same.
            // A FIFO queue is used to implement breadth-first search.
            // Part 1 could benefit from A* because there is one start and end position,
            // but I prefer a solution that can be generalized in part 2.
            Queue<Position> frontier = new LinkedList<>();
            frontier.add(end);

            Position current = null;
            while (!frontier.isEmpty()) {
                current = frontier.remove();

                if (current.equals(start)) {
                    break;
                }

                for (Position predecessor : predecessors(current)) {
                    if (!distances.keySet().contains(predecessor)) {
                        distances.put(predecessor, distances.get(current) + 1);
                        frontier.add(predecessor);
                    }
                }
            }

            return distances.get(current);
        }
    }

    static Stream<String> inputLines() {
        return new BufferedReader(new InputStreamReader(System.in)).lines();
    }
}
