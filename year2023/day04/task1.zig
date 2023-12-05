const std = @import("std");

const stdout = std.io.getStdOut();
var stdin = std.io.bufferedReader(std.io.getStdIn().reader());

const Numbers = struct {
    set: std.bit_set.StaticBitSet(100),

    fn fromString(str: []const u8) !Numbers {
        var result = Numbers{
            .set = std.bit_set.StaticBitSet(100).initEmpty(),
        };
        var nums = std.mem.splitSequence(u8, str, " ");
        while (nums.next()) |num| {
            if (num.len > 0) { // skip multiple spaces
                result.set.set(try std.fmt.parseInt(usize, num, 10));
            }
        }
        return result;
    }
};

const Card = struct {
    winning: Numbers,
    mine: Numbers,

    fn fromString(str: []const u8) !Card {
        var parts = std.mem.splitSequence(u8, str, " | ");
        const winning = try Numbers.fromString(parts.next().?);
        const mine = try Numbers.fromString(parts.next().?);
        return Card{
            .winning = winning,
            .mine = mine,
        };
    }

    fn pointValue(self: *Card) usize { // mutates self
        const matching: u4 = @intCast(self.winning.set.intersectWith(self.mine.set).count());
        const one: u32 = 1;
        return if (matching == 0) 0 else (one << (matching - 1));
    }
};

pub fn main() !void {
    var points: usize = 0;

    var buf: [1024]u8 = undefined;
    while (try stdin.reader().readUntilDelimiterOrEof(&buf, '\n')) |line| {
        var parts =
            std.mem.splitSequence(u8, line, ": ");
        _ = parts.next(); // skip the first part
        var card = try Card.fromString(parts.next().?);
        points += card.pointValue();
    }

    _ = try stdout.writer().print("{d}\n", .{points});
}
