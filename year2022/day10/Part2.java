package year2022.day10;

// JEP 361: Switch Expressions
// JEP 395: Records
// JEP 405: Record Patterns (Preview)
// JEP 409: Sealed Classes
// JEP 427: Pattern Matching for switch (Third Preview)

import java.util.Scanner;

public class Part2 {
    static final int CRT_WIDTH = 40;

    public static void main(String[] args) {
        try (Scanner input = new Scanner(System.in)) {
            CRT crt = new CRT(CRT_WIDTH);
            CPU cpu = new CPU(crt);
            cpu.execute(input);
            System.out.println(crt.image());
        }
    }

    static class CRT {
        public final int width;
        private int cursor;
        private StringBuilder buffer;

        CRT(int width) {
            this.width = width;
            this.cursor = 0;
            this.buffer = new StringBuilder();
        }

        void drawPixel(boolean lit) {
            if (cursor == width) {
                buffer.append("\n");
                cursor = 0;
            }
            buffer.append(lit ? "#" : ".");
            cursor++;
        }

        String image() {
            return buffer.toString();
        }
    }

    static class CPU {
        private CRT crt;
        private int xRegister;
        private int cycle;
        private int delay;

        CPU(CRT crt) {
            this.crt = crt;
            this.xRegister = 1;
            this.cycle = 0;
            this.delay = 0;
        }

        boolean spriteIsVisible() {
            int pixel = (cycle - 1) % crt.width;
            return xRegister - 1 <= pixel && pixel <= xRegister + 1;
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

                crt.drawPixel(spriteIsVisible());

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
        record NOOP() implements Instruction {
        }

        record ADDX(int n) implements Instruction {
        }

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
    }
}
