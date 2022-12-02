package aoc.y2022.day01;

import java.util.Scanner;

class Part01 {
    public static void main(String[] args) {
        try (Scanner input = new Scanner(System.in)) {
            int maxCalories = 0;
            int totalCalories = 0;
            while (input.hasNext()) {
                String line = input.nextLine();
                if (line.isEmpty()) {
                    maxCalories = Math.max(maxCalories, totalCalories);
                    totalCalories = 0;
                } else {
                    totalCalories += Integer.parseInt(line);
                }
            }
            System.out.println(maxCalories);
        }
    }
}
