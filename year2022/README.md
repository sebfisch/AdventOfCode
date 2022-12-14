# Java solutions for Advent of Code 2022

 1. [Calorie Counting](https://adventofcode.com/2022/day/1): Reads input lines using a [Scanner] (closed by a [try-with-resources](https://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html) statement) implementing an [online selection algorithm](https://en.m.wikipedia.org/wiki/Selection_algorithm#Online_selection_algorithm).
 1. [Rock Paper Scissors](https://adventofcode.com/2022/day/2): Reads input lines using the [Stream] API and represent aspects of the game with [enum] and [record] types.
 1. [Rucksack Reorganization](https://adventofcode.com/2022/day/3): Implements set intersection with the [collections framework].
 1. [Camp Cleanup](https://adventofcode.com/2022/day/4): Implements set intersection and inclusion with the [collections framework].
 1. [Supply Stacks](https://adventofcode.com/2022/day/5): Builds and manipulates stacks with the [Deque](https://download.java.net/java/early_access/panama/docs/api/java.base/java/util/Deque.html) interface and defines a hand-written parser for an interesting input format.
 1. [Tuning Trouble](https://adventofcode.com/2022/day/6): Implements a [circular buffer](https://en.wikipedia.org/wiki/Circular_buffer) and checks its contents for duplicates with the [collections framework].
 1. [No Space Left On Device](https://adventofcode.com/2022/day/7): Accumulates file sizes using [`merge` on maps](https://docs.oracle.com/en/java/javase/19/docs/api/java.base/java/util/Map.html#merge(K,V,java.util.function.BiFunction)). 
 1. [Treetop Tree House](https://adventofcode.com/2022/day/8): Models directions and positions using [enum] and [record] types, constructs streams using a [Stream.Builder](https://docs.oracle.com/en/java/javase/19/docs/api/java.base/java/util/stream/Stream.Builder.html), and searches in a loop with a [`break` statement](https://docs.oracle.com/javase/tutorial/java/nutsandbolts/branch.html).
 1. [Rope Bridge](https://adventofcode.com/2022/day/9): Models a Snake-like simulation using [enum] and [record] types and implements a clever way to compute movements.
 1. [Cathode-Ray Tube](https://adventofcode.com/2022/day/10): Models machine code as [algebraic datatypes](https://sebfisch.github.io/java-data/) implemented using [record] types and [sealed classes](https://openjdk.org/jeps/409) with [default methods](https://docs.oracle.com/javase/tutorial/java/IandI/defaultmethods.html), processes instructions using [pattern matching](https://openjdk.org/jeps/427) with [record patterns](https://openjdk.org/jeps/405) in [switch expressions](https://openjdk.org/jeps/361), and represents a terminal using a [StringBuilder](https://docs.oracle.com/en/java/javase/19/docs/api/java.base/java/lang/StringBuilder.html).
 1. [Monkey in the Middle](https://adventofcode.com/2022/day/11): Another example of algebraic datatypes and pattern matching simulating a game involving [modular arithmetic](https://en.wikipedia.org/wiki/Modular_arithmetic).
 1. [Hill Climbing Algorithm](https://adventofcode.com/2022/day/12): Implements a [path finding](https://www.redblobgames.com/pathfinding/) algorithm with a [Queue](https://docs.oracle.com/en/java/javase/19/docs/api/java.base/java/util/Queue.html).
 1. [Distress Signal](https://adventofcode.com/2022/day/13): Parses nested data using predictive [recursive descent](https://en.wikipedia.org/wiki/Recursive_descent_parser) based on a [Scanner], compares generated data lexicographically with [pattern matching for `instanceof`](https://openjdk.org/jeps/394), and processes it using the [Stream] API.
 1. [Regolith Reservoir](https://adventofcode.com/2022/day/14): Simulates falling objects and manages their positions using the [collections framework].

[collections framework]: https://docs.oracle.com/javase/tutorial/collections/index.html
[enum]: https://docs.oracle.com/javase/tutorial/java/javaOO/enum.html
[record]: https://openjdk.org/jeps/395
[Scanner]: https://docs.oracle.com/en/java/javase/19/docs/api/java.base/java/util/Scanner.html
[Stream]: https://docs.oracle.com/en/java/javase/19/docs/api/java.base/java/util/stream/package-summary.html

## Import Counts

| Count | Import |
| ----: | ------ |
| 18 | [java.util.List](https://docs.oracle.com/en/java/javase/19/docs/api/java.base/java/util/List.html) |
| 18 | [java.util.stream.Stream](https://docs.oracle.com/en/java/javase/19/docs/api/java.base/java/util/stream/Stream.html) |
| 16 | [java.io.InputStreamReader](https://docs.oracle.com/en/java/javase/19/docs/api/java.base/java/io/InputStreamReader.html) |
| 14 | [java.io.BufferedReader](https://docs.oracle.com/en/java/javase/19/docs/api/java.base/java/io/BufferedReader.html) |
| 13 | [java.util.Scanner](https://docs.oracle.com/en/java/javase/19/docs/api/java.base/java/util/Scanner.html) |
| 11 | [java.util.LinkedList](https://docs.oracle.com/en/java/javase/19/docs/api/java.base/java/util/LinkedList.html) |
| 10 | [java.util.Set](https://docs.oracle.com/en/java/javase/19/docs/api/java.base/java/util/Set.html) |
| 9 | [java.util.ArrayList](https://docs.oracle.com/en/java/javase/19/docs/api/java.base/java/util/ArrayList.html) |
| 8 | [java.util.Map](https://docs.oracle.com/en/java/javase/19/docs/api/java.base/java/util/Map.html) |
| 7 | [java.util.Collections](https://docs.oracle.com/en/java/javase/19/docs/api/java.base/java/util/Collections.html) |
| 7 | [java.util.HashSet](https://docs.oracle.com/en/java/javase/19/docs/api/java.base/java/util/HashSet.html) |
| 7 | [java.util.stream.Collectors](https://docs.oracle.com/en/java/javase/19/docs/api/java.base/java/util/stream/Collectors.html) |
| 7 | [java.util.stream.IntStream](https://docs.oracle.com/en/java/javase/19/docs/api/java.base/java/util/stream/IntStream.html) |
| 6 | [java.util.Arrays](https://docs.oracle.com/en/java/javase/19/docs/api/java.base/java/util/Arrays.html) |
| 6 | [java.util.HashMap](https://docs.oracle.com/en/java/javase/19/docs/api/java.base/java/util/HashMap.html) |
| 4 | [java.util.Collection](https://docs.oracle.com/en/java/javase/19/docs/api/java.base/java/util/Collection.html) |
| 4 | [java.util.Comparator](https://docs.oracle.com/en/java/javase/19/docs/api/java.base/java/util/Comparator.html) |
| 2 | [java.io.IOException](https://docs.oracle.com/en/java/javase/19/docs/api/java.base/java/io/IOException.html) |
| 2 | [java.io.Reader](https://docs.oracle.com/en/java/javase/19/docs/api/java.base/java/io/Reader.html) |
| 2 | [java.util.Deque](https://docs.oracle.com/en/java/javase/19/docs/api/java.base/java/util/Deque.html) |
| 2 | [java.util.Iterator](https://docs.oracle.com/en/java/javase/19/docs/api/java.base/java/util/Iterator.html) |
| 2 | [java.util.Objects](https://docs.oracle.com/en/java/javase/19/docs/api/java.base/java/util/Objects.html) |
| 2 | [java.util.Queue](https://docs.oracle.com/en/java/javase/19/docs/api/java.base/java/util/Queue.html) |
