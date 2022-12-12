package year2022.day12;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
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

            PriorityQueue<PathElem> frontier = new PriorityQueue<>(Comparator.comparing(PathElem::priority));
            frontier.add(new PathElem(end, 0));

            PathElem current = null;
            while (!frontier.isEmpty()) {
                current = frontier.remove();

                if (current.position.equals(start)) {
                    break;
                }

                for (Position predecessor : predecessors(current.position)) {
                    int distance = distances.get(current.position) + 1;
                    if (!distances.keySet().contains(predecessor) || distance < distances.get(predecessor)) {
                        distances.put(predecessor, distance);
                        frontier.add(new PathElem(predecessor, distance));
                    }
                }
            }

            return distances.get(current.position);
        }
    }

    record PathElem(Position position, int priority) {
    }

    static Stream<String> inputLines() {
        return new BufferedReader(new InputStreamReader(System.in)).lines();
    }
}
