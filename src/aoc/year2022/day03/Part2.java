package aoc.year2022.day03;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

class Part2 {
    public static void main(String[] args) {
        try (Scanner input = new Scanner(System.in)) {
            int totalPriorities = 0;
            while (input.hasNext()) {
                char badge = uniqueCommonChar(input.nextLine(), input.nextLine(), input.nextLine());
                totalPriorities += priority(badge);
            }
            System.out.println(totalPriorities);
        }
    }

    private static Character uniqueCommonChar(String firstLine, String... moreLines) {
        Set<Character> result = chars(firstLine);
        for (String line : moreLines) {
            result.retainAll(chars(line));
        }
        return result.iterator().next();
    }

    private static Set<Character> chars(String string) {
        Set<Character> result = new HashSet<>();
        for (int index = 0; index < string.length(); index++) {
            result.add(string.charAt(index));
        }
        return result;
    }

    private static int priority(char item) {
        if (Character.isLowerCase(item)) {
            return (int) item - (int) 'a' + 1;
        }
        return (int) item - (int) 'A' + 27;
    }
}