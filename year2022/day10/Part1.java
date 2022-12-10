package year2022.day10;

import java.util.Scanner;
import java.util.Set;

public class Part1 {
    static final Set<Integer> RELEVANT_CYCLES = Set.of(
            20, 60, 100, 140, 180, 220);

    public static void main(String[] args) {
        try (Scanner input = new Scanner(System.in)) {
            CPU cpu = new CPU(RELEVANT_CYCLES);
            cpu.execute(input);
            System.out.println(cpu.signalStrength());
        }
    }

    static class CPU {
        private final Set<Integer> relevantCycles;
        private int signalStrength;
        private int xRegister;
        private int cycle;
        private int delay;

        CPU(Set<Integer> relevantCycles) {
            this.relevantCycles = relevantCycles;
            this.signalStrength = 0;
            this.xRegister = 1;
            this.cycle = 0;
            this.delay = 0;
        }

        int signalStrength() {
            return signalStrength;
        }

        void execute(Scanner instructions) {
            Instruction instruction = new Instruction.NOOP();
            while (delay > 0 || instructions.hasNext()) {
                cycle++;

                if (delay == 0) {
                    finishExecution(instruction);
                    instruction = Instruction.fromString(instructions.nextLine());
                    beginExecution(instruction);
                }

                if (relevantCycles.contains(cycle)) {
                    signalStrength += cycle * xRegister;
                }

                delay--;
            }
        }

        void beginExecution(Instruction instruction) {
            delay = instruction.delay();
        }

        void finishExecution(Instruction instruction) {
            switch (instruction) {
                case Instruction.NOOP() -> {
                }
                case Instruction.ADDX(int n) -> {
                    xRegister += n;
                }
            }
        }
    }

    sealed interface Instruction {
        static Instruction fromString(String string) {
            String[] words = string.split(" ");
            return switch (words[0]) {
                case "noop" -> new NOOP();
                case "addx" -> new ADDX(Integer.parseInt(words[1]));
                default -> throw new IllegalArgumentException(
                        "invalid instruction: %s".formatted(string));
            };
        }

        default int delay() {
            return switch (this) {
                case NOOP() -> 1;
                case ADDX(int n) -> 2;
            };
        }

        record NOOP() implements Instruction {
        }

        record ADDX(int n) implements Instruction {
        }
    }
}
