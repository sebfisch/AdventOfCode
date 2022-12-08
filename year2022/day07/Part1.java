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
            printSmallSum(input);
        }
    }

    static void printSmallSum(Scanner input) {
        Map<String, Integer> sizes = directorySizes(input);

        int sumOfSmallSizes = sizes.values().stream()
                .filter(size -> size <= MAX_SIZE)
                .reduce(0, (x, y) -> x + y);

        System.out.println(sumOfSmallSizes);
    }

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
            for (String dir : fullPaths(path)) {
                int size = Integer.parseInt(line.split(" ")[0]);
                sizes.merge(dir, size, (x, y) -> x + y);
            }
        }
    }

    static List<String> fullPaths(List<String> path) {
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
