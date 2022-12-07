package year2022.day07;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Part1 {
    private static final int MAX_SIZE = 100000;

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
            int sumOfSmallSizes = dirSizes.values().stream()
                    .filter(size -> size <= MAX_SIZE)
                    .reduce(0, (x, y) -> x + y);
            System.out.println(sumOfSmallSizes);
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
