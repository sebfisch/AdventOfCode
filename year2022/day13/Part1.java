package year2022.day13;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Part1 {
    public static void main(String[] args) {
        try (Scanner input = new Scanner(System.in)) {
            System.out.println(sumOfIndices(input));
        }
    }

    static int sumOfIndices(Scanner input) {
        input.useDelimiter(""); // one character at a time

        int sum = 0;
        int index = 1;
        while (input.hasNext()) {
            Packets packets = Packets.parse(input);
            if (packets.left.compareTo(packets.right) <= 0) {
                sum += index;
            }
            index++;
        }

        return sum;
    }

    record Packets(PacketData left, PacketData right) {
        static Packets parse(Scanner input) {
            PacketData left = PacketData.parse(input);
            input.skip("\\R"); // newline

            PacketData right = PacketData.parse(input);
            input.skip("\\R");

            if (input.hasNext("\\R")) {
                input.skip("\\R");
            }

            return new Packets(left, right);
        }
    }

    sealed interface PacketData extends Comparable<PacketData> {
        static PacketData parse(Scanner input) {
            return input.hasNextInt() ? IntData.parse(input) : ListData.parse(input);
        }

        record IntData(int value) implements PacketData {
            public int compareTo(PacketData data) {
                if (data instanceof IntData that) {
                    return Integer.compare(this.value, that.value);
                }
                return new ListData(List.of(this)).compareTo(data);
            }

            static IntData parse(Scanner input) {
                int result = 0;
                while (input.hasNextInt()) {
                    result *= 10;
                    result += input.nextInt();
                }
                return new IntData(result);
            }
        }

        record ListData(List<PacketData> elements) implements PacketData {
            public int compareTo(PacketData data) {
                if (data instanceof ListData that) {
                    return compareLexicographically(this.elements.iterator(), that.elements.iterator());
                }
                return this.compareTo(new ListData(List.of(data)));
            }

            static ListData parse(Scanner input) {
                List<PacketData> elements = new LinkedList<>();

                if (!input.hasNext("\\[")) {
                    throw new IllegalArgumentException(input.next());
                }

                input.skip("\\[");
                while (!input.hasNext("\\]")) {
                    elements.add(PacketData.parse(input));
                    if (!input.hasNext("\\]")) {
                        input.skip(",");
                    }
                }
                input.skip("\\]");

                return new ListData(Collections.unmodifiableList(elements));
            }
        }
    }

    static <T extends Comparable<T>> int compareLexicographically(Iterator<T> left, Iterator<T> right) {
        while (left.hasNext() && right.hasNext()) {
            int elementComparison = left.next().compareTo(right.next());
            if (elementComparison != 0) {
                return elementComparison;
            }
        }
        if (left.hasNext()) {
            return 1;
        }
        if (right.hasNext()) {
            return -1;
        }
        return 0;
    }
}
