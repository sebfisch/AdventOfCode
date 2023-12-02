const std = @import("std");

const stdout = std.io.getStdOut();
var stdin = std.io.bufferedReader(std.io.getStdIn().reader());

const written_digits =
    [_][]const u8{ "one", "two", "three", "four", "five", "six", "seven", "eight", "nine" };

pub fn main() !void {
    var sum: u64 = 0;
    var buf: [64]u8 = undefined;
    while (try stdin.reader().readUntilDelimiterOrEof(&buf, '\n')) |line| {
        sum += lineValue(line);
    }
    _ = try stdout.writer().print("{d}\n", .{sum});
}

fn lineValue(line: []u8) usize {
    var fst: ?usize = null;
    var lst: ?usize = null;
    for (line, 0..) |char, i| {
        const digit: ?usize = readDigit(line[i..], char);
        if (digit) |d| {
            fst = fst orelse d;
            lst = d;
        }
    }
    return 10 * fst.? + lst.?;
}

fn readDigit(s: []u8, c: u8) ?usize {
    if (std.ascii.isDigit(c)) {
        return c - '0';
    }
    for (written_digits, 1..) |written, digit| {
        if (std.mem.startsWith(u8, s, written)) {
            return digit;
        }
    }
    return null;
}
