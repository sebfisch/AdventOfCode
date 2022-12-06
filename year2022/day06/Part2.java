package year2022.day06;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class Part2 {
    private static final int MARKER_SIZE = 14;

    // from here on identical to part 1

    public static void main(String[] args) {
        try (Reader input = new InputStreamReader(System.in)) {
            long numberOfCharsRead = 0;
            RingBuffer<Integer> buffer = new RingBuffer<>(MARKER_SIZE);
            int nextInt;
            while ((nextInt = input.read()) != -1) {
                buffer.add(nextInt);
                numberOfCharsRead++;
                if (Set.copyOf(buffer.contents()).size() == MARKER_SIZE) {
                    System.out.println(numberOfCharsRead);
                    break;
                }
            }
        } catch (IOException e) {
        }
    }

    static class RingBuffer<E> {
        private final int capacity;
        private final List<E> contents;
        private int nextIndex;

        RingBuffer(int capacity) {
            this.capacity = capacity;
            contents = new ArrayList<>(capacity);
            nextIndex = 0;
        }

        Collection<E> contents() {
            return Collections.unmodifiableCollection(contents);
        }

        void add(E elem) {
            if (contents.size() < capacity) {
                contents.add(elem);
            } else {
                contents.set(nextIndex, elem);
            }
            nextIndex = (nextIndex + 1) % capacity;
        }
    }
}
