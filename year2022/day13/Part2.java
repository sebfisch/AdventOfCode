package year2022.day13;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

public class Part2 {
    public static void main(String[] args) {
        try (Stream<String> lines = inputLines()) {
            System.out.println(decoderKey(lines));
        }
    }

    static int decoderKey(Stream<String> lines) {
        List<PacketData> dividerPackets = Stream.of("[[2]]", "[[6]]")
                .map(PacketData::fromString)
                .toList();

        Stream<PacketData> inputPackets = lines
                .filter(line -> !line.isBlank())
                .map(PacketData::fromString);

        List<PacketData> sortedPackets = Stream
            .concat(dividerPackets.stream(), inputPackets)
            .sorted()
            .toList();

        return dividerPackets.stream()
            .map(sortedPackets::indexOf)
            .map(i -> i+1)
            .reduce(1, (x,y) -> x*y);
    }

    static Stream<String> inputLines() {
        return new BufferedReader(new InputStreamReader(System.in)).lines();
    }

    sealed interface PacketData extends Comparable<PacketData> {
        static PacketData fromString(String string) {
            Scanner input = new Scanner(string);
            input.useDelimiter("");
            return PacketData.parse(input);
        }

        // from here on identical to part 1

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
