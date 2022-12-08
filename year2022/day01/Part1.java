package year2022.day01;

import java.util.Scanner;

class Part1 {
    public static void main(String[] args) {
        try (Scanner input = new Scanner(System.in)) {
            System.out.println(maxCalories(input));
        }
    }

    static int maxCalories(Scanner input) {
        int result = 0;

        int sum = 0;
        while (input.hasNext()) {
            String line = input.nextLine();
            if (line.isEmpty()) {
                result = Math.max(result, sum);
                sum = 0;
            } else {
                sum += Integer.parseInt(line);
            }
        }

        return result;
    }
}
