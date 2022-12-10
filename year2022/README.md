# Java solutions for Advent of Code 2022

 1. Reads input lines using a [Scanner](https://docs.oracle.com/en/java/javase/19/docs/api/java.base/java/util/Scanner.html) (closed by a [try-with-resources](https://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html) statement) implementing an [online selection algorithm](https://en.m.wikipedia.org/wiki/Selection_algorithm#Online_selection_algorithm).
 1. Reads input lines using the [Stream](https://docs.oracle.com/en/java/javase/19/docs/api/java.base/java/util/stream/package-summary.html) API and represent aspects of the game rock, paper, scissors with [enum] and [record] types.
 1. Implements set intersection with the [collections framework].
 1. Implements set intersection and inclusion with the [collections framework].
 1. Builds and manipulates stacks with the [Dequeue](https://download.java.net/java/early_access/panama/docs/api/java.base/java/util/Deque.html) interface and defines a hand-written parser for an interesting input format.
 1. Implements a [circular buffer](https://en.wikipedia.org/wiki/Circular_buffer) and checks its contents for duplicates with the [collections framework].
 1. Accumulates file sizes using [`merge` on maps](https://docs.oracle.com/en/java/javase/19/docs/api/java.base/java/util/Map.html#merge(K,V,java.util.function.BiFunction)). 
 1. Models directions and positions using [enum] and [record] types, constructs streams using a [stream builder](https://docs.oracle.com/en/java/javase/19/docs/api/java.base/java/util/stream/Stream.Builder.html), and searches in a loop with a [`break` statement](https://docs.oracle.com/javase/tutorial/java/nutsandbolts/branch.html).
 1. Models a Snake-like simulation using [enum] and [record] types and implements a clever way to compute movements.
 1. Models machine code as [algebraic datatypes](https://sebfisch.github.io/java-data/) implemented using [record] types and [sealed classes](https://openjdk.org/jeps/409) with [default methods](https://docs.oracle.com/javase/tutorial/java/IandI/defaultmethods.html), processes instructions using [pattern matching](https://openjdk.org/jeps/427) with [record patterns](https://openjdk.org/jeps/405) in [switch expressions](https://openjdk.org/jeps/361), and represents a terminal using a [string buffer](https://docs.oracle.com/en/java/javase/19/docs/api/java.base/java/lang/StringBuffer.html).

[collections framework]: https://docs.oracle.com/javase/tutorial/collections/index.html
[enum]: https://docs.oracle.com/javase/tutorial/java/javaOO/enum.html
[record]: https://openjdk.org/jeps/395
