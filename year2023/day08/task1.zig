pub fn main() !void {
    var instructions = std.ArrayList(Instruction).init(allocator);
    defer instructions.deinit();

    for ((try readLine()).?) |char| {
        switch (char) {
            'L' => try instructions.append(Instruction.left),
            'R' => try instructions.append(Instruction.right),
            else => unreachable,
        }
    }

    _ = try readLine(); // skip empty line

    var routes = try Routes.read();
    defer routes.deinit();

    var steps: usize = 0;
    var location = Location.fromString("AAA");
    const goal = Location.fromString("ZZZ");
    while (!location.eql(goal)) : (steps += 1) {
        location = routes.next(
            location,
            instructions.items[steps % instructions.items.len],
        );
    }

    try stdout.writer().print("{d}\n", .{steps});
}

const Instruction = enum { left, right };

const Location = struct {
    name: [3]u8,

    fn eql(self: Location, other: Location) bool {
        return self.name[0] == other.name[0] and
            self.name[1] == other.name[1] and
            self.name[2] == other.name[2];
    }

    fn fromString(str: []const u8) Location {
        return Location{ .name = .{ str[0], str[1], str[2] } };
    }
};

const Choice = struct {
    left: Location,
    right: Location,

    fn fromString(str: []const u8) Choice {
        var parts = split(str[1 .. str.len - 1], ", ");
        return Choice{
            .left = Location.fromString(parts.next().?),
            .right = Location.fromString(parts.next().?),
        };
    }
};

const Routes = struct {
    choices: std.AutoHashMap(Location, Choice),

    fn init() Routes {
        return Routes{ .choices = std.AutoHashMap(Location, Choice).init(allocator) };
    }

    fn deinit(self: *Routes) void {
        self.choices.deinit();
    }

    fn read() !Routes {
        var routes = Routes.init();
        errdefer routes.deinit();
        while (try readLine()) |line| {
            var parts = split(line, " = ");
            const location = Location.fromString(parts.next().?);
            const choice = Choice.fromString(parts.next().?);
            try routes.choices.put(location, choice);
        }
        return routes;
    }

    fn next(self: Routes, location: Location, instruction: Instruction) Location {
        const choice = self.choices.get(location).?;
        switch (instruction) {
            .left => return choice.left,
            .right => return choice.right,
        }
    }
};

const std = @import("std");

const stdout = std.io.getStdOut();
var stdin = std.io.bufferedReader(std.io.getStdIn().reader());
var buf: [4096]u8 = undefined;
const allocator = std.heap.page_allocator;

fn readLine() !?[]const u8 {
    const result = try stdin.reader().readUntilDelimiterOrEof(&buf, '\n');
    if (result) |line| {
        // std.debug.print("line: {any} - len: {any}\n", .{ line, line.len });
        return line;
    }
    return null;
}

fn split(
    str: []const u8,
    delimiter: []const u8,
) std.mem.SplitIterator(u8, .sequence) {
    return std.mem.splitSequence(u8, str, delimiter);
}
