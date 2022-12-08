package year2022.day01;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Part2 {
    public static void main(String[] args) {
        try (Scanner input = new Scanner(System.in)) {
            System.out.println(maxCalories(input).total());
        }
    }

    static Biggest maxCalories(Scanner input) {
        Biggest result = new Biggest(3);

        int sum = 0;
        while (input.hasNext()) {
            String line = input.nextLine();
            if (line.isEmpty()) {
                result.add(sum);
                sum = 0;
            } else {
                sum += Integer.parseInt(line);
            }
        }

        return result;
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