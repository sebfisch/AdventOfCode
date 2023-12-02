const std = @import("std");

const stdout = std.io.getStdOut();
var stdin = std.io.bufferedReader(std.io.getStdIn().reader());

const Cubes = struct {
    red: usize = 0,
    green: usize = 0,
    blue: usize = 0,

    fn fromString(str: []const u8) !Cubes {
        var cubes = Cubes{};
        var parts = std.mem.splitSequence(u8, str, ", ");
        while (parts.next()) |part| {
            var words = std.mem.splitSequence(u8, part, " ");
            const count = try std.fmt.parseInt(u8, words.next().?, 10);
            const color = words.next().?;
            if (std.mem.eql(u8, color, "red")) {
                cubes.red = count;
            } else if (std.mem.eql(u8, color, "green")) {
                cubes.green = count;
            } else if (std.mem.eql(u8, color, "blue")) {
                cubes.blue = count;
            }
        }
        return cubes;
    }

    fn power(self: Cubes) usize {
        return self.red * self.green * self.blue;
    }
};

const Game = struct {
    allocator: std.mem.Allocator,
    id: usize = 0,
    cubes: std.ArrayList(Cubes),

    fn init(allocator: std.mem.Allocator) Game {
        return Game{
            .allocator = allocator,
            .cubes = std.ArrayList(Cubes).init(allocator),
        };
    }

    fn deinit(self: *Game) void {
        self.cubes.deinit();
    }

    fn fromString(str: []const u8) !*Game {
        var game = Game.init(std.heap.page_allocator);
        errdefer game.deinit();

        var parts = std.mem.splitSequence(u8, str, ": ");

        var gameId = std.mem.splitSequence(u8, parts.next().?, " ");
        _ = gameId.next(); // skip "Game"
        game.id = try std.fmt.parseInt(u8, gameId.next().?, 10);

        var cubesStrings = std.mem.splitSequence(u8, parts.next().?, "; ");
        while (cubesStrings.next()) |cubesString| {
            try game.cubes.append(try Cubes.fromString(cubesString));
        }

        return &game;
    }

    fn minPossibleBag(self: *Game) Cubes {
        var bag = Cubes{};
        for (self.cubes.items) |cube| {
            if (cube.red > bag.red) {
                bag.red = cube.red;
            }
            if (cube.green > bag.green) {
                bag.green = cube.green;
            }
            if (cube.blue > bag.blue) {
                bag.blue = cube.blue;
            }
        }
        return bag;
    }
};

const sampleBag = Cubes{
    .red = 12,
    .green = 13,
    .blue = 14,
};

pub fn main() !void {
    var powerSum: usize = 0;

    var buf: [1024]u8 = undefined;
    while (try stdin.reader().readUntilDelimiterOrEof(&buf, '\n')) |line| {
        const game = try Game.fromString(line);
        defer game.deinit();
        powerSum += game.minPossibleBag().power();
    }

    _ = try stdout.writer().print("{d}\n", .{powerSum});
}
