package year2022.day03;

import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Part2 {
    public static void main(String[] args) {
        try (Scanner input = new Scanner(System.in)) {
            System.out.println(totalPriorities(input));
        }
    }

    static int totalPriorities(Scanner input) {
        int totalPriorities = 0;
        while (input.hasNext()) {
            char badge = uniqueCommonChar(input.nextLine(), input.nextLine(), input.nextLine());
            totalPriorities += priority(badge);
        }
        return totalPriorities;
    }

    static Character uniqueCommonChar(String firstLine, String... moreLines) {
        Set<Character> intersection = chars(firstLine);
        for (String line : moreLines) {
            intersection.retainAll(chars(line));
        }
        return intersection.iterator().next();
    }

    static Set<Character> chars(String string) {
        return IntStream
                .range(0, string.length())
                .mapToObj(string::charAt)
                .collect(Collectors.toSet());
    }

    static int priority(char item) {
        if (Character.isLowerCase(item)) {
            return (int) item - (int) 'a' + 1;
        }
        return (int) item - (int) 'A' + 27;
    }
}
