pub fn main() !void {
    var grid = try Grid.read();
    defer grid.deinit();

    var loop = try grid.loop();
    defer loop.deinit();

    var countInside: usize = 0;
    var x: usize = 0;
    var y: usize = 0;
    while (y <= grid.bottom_right.y) : (y += 1) {
        while (x <= grid.bottom_right.x) : (x += 1) {
            var pos = Position{ .x = x, .y = y };
            if (loop.contains(pos)) {
                continue;
            }
            // use even-odd rule to determine if inside or outside
            var inside = false;
            while (pos.x > 0 and pos.y > 0) {
                if (loop.contains(pos) and loop.get(pos).?.isCrossing()) {
                    inside = !inside;
                }
                pos.x -= 1;
                pos.y -= 1;
            }
            if (loop.contains(pos) and loop.get(pos).?.isCrossing()) {
                inside = !inside;
            }
            if (inside) {
                countInside += 1;
            }
        }
        x = 0;
    }

    try stdout.writer().print("{}\n", .{countInside});
}

const Direction = enum {
    north,
    east,
    south,
    west,

    fn flip(self: Direction) Direction {
        return switch (self) {
            .north => .south,
            .east => .west,
            .south => .north,
            .west => .east,
        };
    }
};

const Pipe = struct {
    fst: Direction,
    snd: Direction,

    fn fromChar(char: u8) Pipe {
        return switch (char) {
            124 => Pipe{ .fst = .north, .snd = .south },
            45 => Pipe{ .fst = .east, .snd = .west },
            'L' => Pipe{ .fst = .north, .snd = .east },
            'J' => Pipe{ .fst = .north, .snd = .west },
            '7' => Pipe{ .fst = .south, .snd = .west },
            'F' => Pipe{ .fst = .south, .snd = .east },
            else => unreachable,
        };
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

    fn isCrossing(self: Pipe) bool { // a diagonal path to the top left corner
        return (self.fst != .south or self.snd != .west) and // not 7
            (self.fst != .north or self.snd != .east); // not L
    }
};

const Position = struct {
    x: usize,
    y: usize,

    fn eql(self: Position, other: Position) bool {
        return self.x == other.x and self.y == other.y;
    }

    fn move(self: Position, dir: Direction) Position {
        return switch (dir) {
            .north => Position{ .x = self.x, .y = self.y - 1 },
            .east => Position{ .x = self.x + 1, .y = self.y },
            .south => Position{ .x = self.x, .y = self.y + 1 },
            .west => Position{ .x = self.x - 1, .y = self.y },
        };
    }
};

const Grid = struct {
    starting_position: Position,
    bottom_right: Position,
    pipes: std.AutoHashMap(Position, Pipe),

    fn init() Grid {
        var grid: Grid = undefined;
        grid.bottom_right = Position{ .x = 0, .y = 0 };
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
                grid.bottom_right.x = @max(grid.bottom_right.x, pos.x);
            }
            row += 1;
        }
        grid.bottom_right.y = row - 1;

        return grid;
    }

    fn startConnectsTo(self: *Grid, dir: Direction) bool {
        const next = self.starting_position.move(dir);
        return self.pipes.contains(next) and self.pipes.get(next).?.connectsTo(dir.flip());
    }

    fn initSearch(self: *Grid, next: *Position, dir: *Direction) !void {
        if (self.starting_position.y > 0 and self.startConnectsTo(.north)) {
            next.* = self.starting_position.move(.north);
            dir.* = .south;

            // add correct pipe for starting position

            if (self.startConnectsTo(.east)) {
                try self.pipes.put(
                    self.starting_position,
                    Pipe{ .fst = .north, .snd = .east },
                );
                return;
            }

            if (self.starting_position.x > 0 and self.startConnectsTo(.west)) {
                try self.pipes.put(
                    self.starting_position,
                    Pipe{ .fst = .north, .snd = .west },
                );
                return;
            }

            try self.pipes.put(
                self.starting_position,
                Pipe{ .fst = .north, .snd = .south },
            );
            return;
        }

        if (self.startConnectsTo(.east)) {
            next.* = self.starting_position.move(.east);
            dir.* = .west;

            // add correct pipe for starting position

            if (self.starting_position.x > 0 and self.startConnectsTo(.west)) {
                try self.pipes.put(
                    self.starting_position,
                    Pipe{ .fst = .east, .snd = .west },
                );
                return;
            }

            try self.pipes.put(
                self.starting_position,
                Pipe{ .fst = .south, .snd = .east },
            );
            return;
        }

        // start connects to exactly two pipes, in this case south and west
        next.* = self.starting_position.move(.south);
        dir.* = .north;
        try self.pipes.put(
            self.starting_position,
            Pipe{ .fst = .south, .snd = .west },
        );
    }

    fn loop(self: *Grid) !std.AutoHashMap(Position, Pipe) {
        var result = std.AutoHashMap(Position, Pipe).init(allocator);
        errdefer result.deinit();

        var next: Position = undefined;
        var fromDir: Direction = undefined;

        try self.initSearch(&next, &fromDir);
        try result.put(next, self.pipes.get(next).?);

        while (!self.starting_position.eql(next)) {
            const toDir = self.pipes.get(next).?.follow(fromDir);
            next = next.move(toDir);
            fromDir = toDir.flip();
            try result.put(next, self.pipes.get(next).?);
        }

        return result;
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
