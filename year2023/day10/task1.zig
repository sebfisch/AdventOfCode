pub fn main() !void {
    var grid = try Grid.read();
    defer grid.deinit();

    try stdout.writer().print("{}\n", .{grid.loopSize() / 2});
}

const Direction = enum {
    north,
    east,
    south,
    west,

    fn flip(self: Direction) Direction {
        switch (self) {
            Direction.north => return Direction.south,
            Direction.east => return Direction.west,
            Direction.south => return Direction.north,
            Direction.west => return Direction.east,
        }
    }
};

const Pipe = struct {
    fst: Direction,
    snd: Direction,

    fn fromChar(char: u8) Pipe {
        switch (char) {
            124 => return Pipe{ .fst = Direction.north, .snd = Direction.south },
            45 => return Pipe{ .fst = Direction.east, .snd = Direction.west },
            'L' => return Pipe{ .fst = Direction.north, .snd = Direction.east },
            'J' => return Pipe{ .fst = Direction.north, .snd = Direction.west },
            '7' => return Pipe{ .fst = Direction.south, .snd = Direction.west },
            'F' => return Pipe{ .fst = Direction.south, .snd = Direction.east },
            else => unreachable,
        }
    }

    fn connectsTo(self: Pipe, dir: Direction) bool {
        return self.fst == dir or self.snd == dir;
    }

    fn follow(self: Pipe, fromDir: Direction) Direction { // only if self connects to fromDir
        if (self.fst == fromDir) {
            return self.snd;
        }
        std.debug.assert(self.snd == fromDir);
        return self.fst;
    }
};

const Position = struct {
    x: usize,
    y: usize,

    fn eql(self: Position, other: Position) bool {
        return self.x == other.x and self.y == other.y;
    }

    fn move(self: Position, dir: Direction) Position {
        switch (dir) {
            Direction.north => return Position{ .x = self.x, .y = self.y - 1 },
            Direction.east => return Position{ .x = self.x + 1, .y = self.y },
            Direction.south => return Position{ .x = self.x, .y = self.y + 1 },
            Direction.west => return Position{ .x = self.x - 1, .y = self.y },
        }
    }
};

const Grid = struct {
    starting_position: Position,
    pipes: std.AutoHashMap(Position, Pipe),

    fn init() Grid {
        var grid: Grid = undefined;
        grid.pipes = std.AutoHashMap(Position, Pipe).init(allocator);
        return grid;
    }

    fn deinit(grid: *Grid) void {
        grid.pipes.deinit();
    }

    fn read() !Grid {
        var grid = Grid.init();
        errdefer grid.deinit();

        var row: usize = 0;
        while (try readLine()) |line| {
            for (line, 0..) |char, col| {
                if (char == '.') {
                    continue;
                }
                const pos = Position{ .x = col, .y = row };
                if (char == 'S') {
                    grid.starting_position = pos;
                    continue;
                }
                try grid.pipes.put(pos, Pipe.fromChar(char));
            }
            row += 1;
        }

        return grid;
    }

    fn startConnectsTo(self: *Grid, dir: Direction, next: *Position) bool { // overwrites next
        next.* = self.starting_position.move(dir);
        return self.pipes.contains(next.*) and self.pipes.get(next.*).?.connectsTo(dir.flip());
    }

    fn initSearch(self: *Grid, next: *Position, dir: *Direction) void {
        if (self.startConnectsTo(Direction.north, next)) {
            dir.* = Direction.south;
            return;
        }

        if (self.startConnectsTo(Direction.east, next)) {
            dir.* = Direction.west;
            return;
        }

        // start connects to exactly two pipes, in this case south and west
        next.* = self.starting_position.move(Direction.south);
        dir.* = Direction.north;
    }

    fn loopSize(self: *Grid) usize {
        var size: usize = 1;

        var next: Position = undefined;
        var fromDir: Direction = undefined;

        self.initSearch(&next, &fromDir);

        while (!self.starting_position.eql(next)) {
            const toDir = self.pipes.get(next).?.follow(fromDir);
            next = next.move(toDir);
            fromDir = toDir.flip();
            size += 1;
        }

        return size;
    }
};

const std = @import("std");

const stdout = std.io.getStdOut();
var stdin = std.io.bufferedReader(std.io.getStdIn().reader());
var buf: [4096]u8 = undefined;
const allocator = std.heap.page_allocator;

fn readLine() !?[]const u8 {
    return try stdin.reader().readUntilDelimiterOrEof(&buf, '\n');
}
