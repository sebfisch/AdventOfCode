package aoc.y2022.day01;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Part2 {
    public static void main(String[] args) {
        try (Scanner input = new Scanner(System.in)) {
            Biggest maxCalories = new Biggest(3);
            int totalCalories = 0;
            while (input.hasNext()) {
                String line = input.nextLine();
                if (line.isEmpty()) {
                    maxCalories.add(totalCalories);
                    totalCalories = 0;
                } else {
                    totalCalories += Integer.parseInt(line);
                }
            }
            System.out.println(maxCalories.total());
        }
    }
}

class Biggest {
    final int count;
    final List<Integer> numbers = new ArrayList<>();

    Biggest(int count) {
        this.count = count;
    }

    void add(int number) {
        numbers.add(number);
        Collections.sort(numbers);
        if (numbers.size() > count) {
            numbers.remove(0);
        }
    }

    int total() {
        return numbers.stream().reduce(0, (x, y) -> x + y);
    }
}