# Java solutions for Advent of Code 2022

 1. Reads input lines using a [Scanner](https://docs.oracle.com/en/java/javase/19/docs/api/java.base/java/util/Scanner.html) (closed by a [try-with-resources](https://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html) statement) implementing an [online selection algorithm](https://en.m.wikipedia.org/wiki/Selection_algorithm#Online_selection_algorithm).
 1. Uses the [Stream](https://docs.oracle.com/en/java/javase/19/docs/api/java.base/java/util/stream/package-summary.html) API to read input lines and defines [enum](https://docs.oracle.com/javase/tutorial/java/javaOO/enum.html) and [record](https://openjdk.org/jeps/395) types to represent aspects of the game rock, paper, scissors.
 1. Uses operations from the [collections framework] to implement set intersection.
 1. Uses operations from the [collections framework] to implement set intersection and inclusion.
 1. Uses the [Dequeue](https://download.java.net/java/early_access/panama/docs/api/java.base/java/util/Deque.html) interface to build and manipulate stacks and a hand-written parser for an interesting input format.
 1. Uses the [collections framework] to implement a [circular buffer](https://en.wikipedia.org/wiki/Circular_buffer) and check its contents for duplicates.
 1. Handles lines from a terminal session in a loop with [`continue` statements](https://docs.oracle.com/javase/tutorial/java/nutsandbolts/branch.html) and accumulates file sizes using [`merge` on maps](https://docs.oracle.com/en/java/javase/19/docs/api/java.base/java/util/Map.html#merge(K,V,java.util.function.BiFunction)). 

[collections framework]: https://docs.oracle.com/javase/tutorial/collections/index.html
