const std = @import("std");

pub fn main() !void {
    var stdin = std.io.bufferedReader(std.io.getStdIn().reader());
    var stdout = std.io.getStdOut();

    var sum: u64 = 0;
    var buf: [4096]u8 = undefined;
    while (try stdin.reader().readUntilDelimiterOrEof(&buf, '\n')) |line| {
        var fst: ?usize = null;
        var lst: ?usize = null;
        for (line, 0..) |char, i| {
            const digit: ?usize = readDigit(line[i..], char);
            if (digit != null) {
                fst = fst orelse digit.?;
                lst = digit.?;
            }
        }
        sum += 10 * fst.? + lst.?;
    }
    _ = try stdout.writer().print("{d}\n", .{sum});
}

fn readDigit(s: []u8, c: u8) ?usize {
    const written_digits =
        [_][]const u8{ "one", "two", "three", "four", "five", "six", "seven", "eight", "nine" };

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
