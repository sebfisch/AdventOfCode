package year2022.day07;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Part2 {
    private static final int TOTAL_SPACE = 70000000;
    private static final int REQUIRED_SPACE = 30000000;

    public static void main(String[] args) {
        try (Scanner input = new Scanner(System.in)) {
            Map<String, Integer> dirSizes = new HashMap<>();
            LinkedList<String> currentPath = new LinkedList<>();
            while (input.hasNext()) {
                String line = input.nextLine();
                if (line.startsWith("$ cd")) {
                    String dir = line.split(" ")[2];
                    if ("..".equals(dir)) {
                        currentPath.removeLast();
                    } else {
                        currentPath.addLast(dir);
                    }
                    continue;
                }
                if (line.startsWith("$ ls")) {
                    continue;
                }
                if (!line.startsWith("dir")) {
                    for (String dir : fullPaths(currentPath)) {
                        int size = Integer.parseInt(line.split(" ")[0]);
                        dirSizes.merge(dir, size, (x, y) -> x + y);
                    }
                }
            }
            int freeSpace = TOTAL_SPACE - dirSizes.get("/");
            int spaceToFreeUp = REQUIRED_SPACE - freeSpace;
            int smallestBigEnoughSpace = dirSizes.values().stream()
                    .filter(size -> size >= spaceToFreeUp)
                    .min(Comparator.naturalOrder())
                    .orElseThrow();
            System.out.println(smallestBigEnoughSpace);
        }
    }

    private static List<String> fullPaths(List<String> path) {
        List<String> result = new ArrayList<>();
        for (int count = 1; count <= path.size(); count++) {
            result.add(path
                    .subList(0, count)
                    .stream()
                    .collect(Collectors.joining("/")));
        }
        return result;
    }
}
