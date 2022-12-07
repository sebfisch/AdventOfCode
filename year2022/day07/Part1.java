package year2022.day07;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public class Part1 {
    private static final int MAX_SIZE = 100000;

    public static void main(String[] args) {
        try (Scanner input = new Scanner(System.in)) {
            Map<String, Integer> dirSizes = new HashMap<>();
            Deque<String> currentPath = new ArrayDeque<>();
            currentPath.add("/");
            while (input.hasNext()) {
                String line = input.nextLine();
                if (line.startsWith("$ cd")) {
                    String dir = line.split(" ")[2];
                    if ("..".equals(dir)) {
                        currentPath.removeLast();
                    } else {
                        currentPath.addLast(dir);
                    }
                    if (currentPath.size() != Set.copyOf(currentPath).size()) {
                        throw new IllegalStateException(
                                currentPath.stream().collect(Collectors.joining("/")));
                    }
                    continue;
                }
                if (line.startsWith("$ ls")) {
                    continue;
                }
                if (!line.startsWith("dir")) {
                    for (String dir : currentPath) {
                        int size = Integer.parseInt(line.split(" ")[0]);
                        dirSizes.compute(dir, (key, val) -> size + (val == null ? 0 : val));
                    }
                }
            }
            for (String dir : dirSizes.keySet()) {
                System.out.println(dir);
            }
            int sumOfSmallSizes = dirSizes.values().stream()
                    .filter(size -> size <= MAX_SIZE)
                    .reduce(0, (x, y) -> x + y);
            System.out.println(sumOfSmallSizes);
        }
    }
}
