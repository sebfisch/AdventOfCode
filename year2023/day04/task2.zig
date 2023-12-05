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
    id: usize,
    winning: Numbers,
    mine: Numbers,

    fn fromString(str: []const u8) !Card {
        var parts =
            std.mem.splitSequence(u8, str, ": ");

        var info =
            std.mem.splitSequence(u8, parts.next().?, " ");
        _ = info.next(); // skip "Card"
        var id: usize = 0;
        while (info.next()) |num| {
            if (num.len > 0) { // skip multiple spaces
                id = try std.fmt.parseInt(usize, num, 10);
                break;
            }
        }

        var sets =
            std.mem.splitSequence(u8, parts.next().?, " | ");
        const winning = try Numbers.fromString(sets.next().?);
        const mine = try Numbers.fromString(sets.next().?);

        return Card{
            .id = id,
            .winning = winning,
            .mine = mine,
        };
    }

    fn matchingCount(self: Card) u4 { // mutates self
        return @intCast(self.winning.set.intersectWith(self.mine.set).count());
    }
};

const Pile = struct {
    cards: std.AutoHashMap(usize, Card),
    counts: std.AutoHashMap(usize, usize),

    fn init(allocator: std.mem.Allocator) !Pile {
        return Pile{
            .cards = std.AutoHashMap(usize, Card).init(allocator),
            .counts = std.AutoHashMap(usize, usize).init(allocator),
        };
    }

    fn deinit(self: *Pile) void {
        self.cards.deinit();
        self.counts.deinit();
    }

    fn add(self: *Pile, card: Card) !void {
        try self.cards.put(card.id, card);
        try self.counts.put(card.id, 1);
    }

    fn totalCardCount(self: *Pile) usize {
        var total: usize = 0;
        var counts = self.counts.valueIterator();
        while (counts.next()) |count| {
            total += count.*;
        }
        return total;
    }

    fn winCopies(self: *Pile) !void {
        for (1..self.cards.count()) |id| {
            var card = self.cards.get(id).?;
            var count = self.counts.get(id).?;
            while (count > 0) : (count -= 1) {
                var offset: u4 = card.matchingCount();
                while (offset > 0) : (offset -= 1) {
                    const otherCardId = card.id + offset;
                    if (self.counts.get(otherCardId)) |otherCount| {
                        try self.counts.put(otherCardId, otherCount + 1);
                    }
                }
            }
        }
    }
};

pub fn main() !void {
    var pile = try Pile.init(std.heap.page_allocator);
    defer pile.deinit();

    var buf: [1024]u8 = undefined;
    while (try stdin.reader().readUntilDelimiterOrEof(&buf, '\n')) |line| {
        try pile.add(try Card.fromString(line));
    }

    try pile.winCopies();

    _ = try stdout.writer().print("{d}\n", .{pile.totalCardCount()});
}
