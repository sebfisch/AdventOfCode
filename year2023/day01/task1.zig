const std = @import("std");

const stdout = std.io.getStdOut();
var stdin = std.io.bufferedReader(std.io.getStdIn().reader());

pub fn main() !void {
    var sum: u64 = 0;
    var buf: [64]u8 = undefined;
    while (try stdin.reader().readUntilDelimiterOrEof(&buf, '\n')) |line| {
        var fst: ?u8 = null;
        var lst: ?u8 = null;
        for (line) |char| {
            if (std.ascii.isDigit(char)) {
                fst = fst orelse char - '0';
                lst = char - '0';
            }
        }
        sum += 10 * fst.? + lst.?;
    }
    _ = try stdout.writer().print("{d}\n", .{sum});
}
