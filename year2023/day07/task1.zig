pub fn main() !void {
    var bids = std.ArrayList(Bid).init(allocator);
    defer bids.deinit();

    while (try readLine()) |line| {
        try bids.append(try Bid.fromString(line));
    }

    std.sort.heap(Bid, bids.items, {}, lowerHand);

    var winnings: u64 = 0;
    var rank: u64 = 1;
    while (rank <= bids.items.len) : (rank += 1) {
        const bid = bids.items[rank - 1];
        winnings += bid.amount * rank;
    }

    try stdout.writer().print("{}\n", .{winnings});
}

fn lowerHand(ctx: void, left: Bid, right: Bid) bool {
    _ = ctx;
    return Hand.lower(left.hand, right.hand);
}

const Bid = struct {
    hand: Hand,
    amount: u64,

    fn fromString(str: []const u8) !Bid {
        var parts = split(str, " ");
        return Bid{
            .hand = Hand.fromString(parts.next().?),
            .amount = try parseDecimal(u64, parts.next().?),
        };
    }
};

const Hand = struct {
    cards: [5]Card,
    hand_type: HandType,

    fn lower(left: Hand, right: Hand) bool {
        if (left.hand_type != right.hand_type) {
            return HandType.lower(left.hand_type, right.hand_type);
        }

        for (left.cards, right.cards) |left_card, right_card| {
            if (left_card != right_card) {
                return Card.lower(left_card, right_card);
            }
        }

        return false;
    }

    fn fromString(str: []const u8) Hand {
        std.debug.assert(str.len == 5);
        var cards: [5]Card = undefined;
        var index: usize = 0;
        while (index < str.len) : (index += 1) {
            cards[index] = Card.fromChar(str[index]);
        }

        return Hand{
            .cards = cards,
            .hand_type = HandType.of(cards),
        };
    }
};

const HandType = enum(u8) {
    high_card,
    one_pair,
    two_pair,
    three_of_a_kind,
    full_house,
    four_of_a_kind,
    five_of_a_kind,

    fn lower(left: HandType, right: HandType) bool {
        return @intFromEnum(left) < @intFromEnum(right);
    }

    fn of(cards: [5]Card) HandType {
        var counts: [13]u8 = [_]u8{0} ** 13;
        for (cards) |card| {
            counts[@intFromEnum(card)] += 1;
        }

        var max: u8 = 1;
        var has_two_pair: bool = false;
        for (counts) |count| {
            if (count >= 2 and max >= 2) {
                has_two_pair = true;
            }
            max = @max(max, count);
        }

        if (max == 5) {
            return .five_of_a_kind;
        } else if (max == 4) {
            return .four_of_a_kind;
        } else if (max == 3 and has_two_pair) {
            return .full_house;
        } else if (max == 3) {
            return .three_of_a_kind;
        } else if (has_two_pair) {
            return .two_pair;
        } else if (max == 2) {
            return .one_pair;
        } else {
            return .high_card;
        }
    }
};

const Card = enum {
    two,
    three,
    four,
    five,
    six,
    seven,
    eight,
    nine,
    ten,
    jack,
    queen,
    king,
    ace,

    fn lower(left: Card, right: Card) bool {
        return @intFromEnum(left) < @intFromEnum(right);
    }

    fn fromChar(char: u8) Card {
        switch (char) {
            '2' => return .two,
            '3' => return .three,
            '4' => return .four,
            '5' => return .five,
            '6' => return .six,
            '7' => return .seven,
            '8' => return .eight,
            '9' => return .nine,
            'T' => return .ten,
            'J' => return .jack,
            'Q' => return .queen,
            'K' => return .king,
            'A' => return .ace,
            else => unreachable,
        }
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

fn split(
    str: []const u8,
    delimiter: []const u8,
) std.mem.SplitIterator(u8, .sequence) {
    return std.mem.splitSequence(u8, str, delimiter);
}

fn parseDecimal(comptime T: type, str: []const u8) !T {
    return try std.fmt.parseInt(T, str, 10);
}
