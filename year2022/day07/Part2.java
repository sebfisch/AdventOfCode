package year2022.day07;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Part2 {
    private static final int TOTAL_SPACE = 70000000;
    private static final int REQUIRED_SPACE = 30000000;

    public static void main(String[] args) {
        try (Scanner input = new Scanner(System.in)) {
            System.out.println(mostAppropriateDirSize(input));
        }
    }

    static int mostAppropriateDirSize(Scanner input) {
        Map<String, Integer> sizes = directorySizes(input);

        int freeSpace = TOTAL_SPACE - sizes.get("/");
        int spaceToFreeUp = REQUIRED_SPACE - freeSpace;

        return sizes.values().stream()
                .filter(size -> size >= spaceToFreeUp)
                .min(Comparator.naturalOrder())
                .orElseThrow();
    }

    // from here on identical to part 1

    static Map<String, Integer> directorySizes(Scanner input) {
        Map<String, Integer> result = new HashMap<>();
        LinkedList<String> currentPath = new LinkedList<>();

        while (input.hasNext()) {
            processLine(result, currentPath, input.nextLine());
        }

        return result;
    }

    static void processLine(Map<String, Integer> sizes, LinkedList<String> path, String line) {
        if (line.startsWith("$ cd")) {
            String dir = line.split(" ")[2];
            if ("..".equals(dir)) {
                path.removeLast();
            } else {
                path.addLast(dir);
            }
            return;
        }

        if (line.startsWith("$ ls")) {
            return;
        }

        if (!line.startsWith("dir")) {
            int size = Integer.parseInt(line.split(" ")[0]);
            fullPaths(path).forEach(dir -> sizes.merge(dir, size, (x, y) -> x + y));
        }
    }

    static Stream<String> fullPaths(List<String> path) {
        return IntStream
                .range(1, path.size() + 1)
                .mapToObj(count -> path.subList(0, count))
                .map(List::stream)
                .map(s -> s.collect(Collectors.joining("/")));
    }
}
