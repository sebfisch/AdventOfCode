const std = @import("std");

const stdout = std.io.getStdOut();
var stdin = std.io.bufferedReader(std.io.getStdIn().reader());

const Cubes = struct {
    red: u8 = 0,
    green: u8 = 0,
    blue: u8 = 0,

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

    fn isPossibleWith(self: Cubes, bag: Cubes) bool {
        return self.red <= bag.red and self.green <= bag.green and self.blue <= bag.blue;
    }
};

const Game = struct {
    id: usize = 0,
    cubes: std.ArrayList(Cubes),

    fn init(allocator: std.mem.Allocator) Game {
        return Game{
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

    fn isPossibleWith(self: *Game, bag: Cubes) bool {
        for (self.cubes.items) |cube| {
            if (!cube.isPossibleWith(bag)) {
                return false;
            }
        }
        return true;
    }
};

const sampleBag = Cubes{
    .red = 12,
    .green = 13,
    .blue = 14,
};

pub fn main() !void {
    var idSum: usize = 0;

    var buf: [1024]u8 = undefined;
    while (try stdin.reader().readUntilDelimiterOrEof(&buf, '\n')) |line| {
        const game = try Game.fromString(line);
        defer game.deinit();
        if (game.isPossibleWith(sampleBag)) {
            idSum += game.id;
        }
    }

    _ = try stdout.writer().print("{d}\n", .{idSum});
}
