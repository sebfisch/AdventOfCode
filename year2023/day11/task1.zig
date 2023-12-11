pub fn main() !void {
    var galaxies = std.ArrayList(Galaxy).init(allocator);
    defer galaxies.deinit();

    var rowCount: usize = 0;
    var colCount: usize = 0;

    while (try readLine()) |line| {
        for (line, 0..) |char, col| {
            if (char == '#') {
                try galaxies.append(Galaxy.init(col, rowCount));
            }
        }

        rowCount += 1;
        colCount = @max(colCount, line.len);
    }

    var emptySpace = try EmptySpace.init(rowCount, colCount);
    defer emptySpace.deinit();
    emptySpace.unset(galaxies.items);

    var sum_of_path_lengths: usize = 0;
    var fstIndex: usize = 0;
    while (fstIndex < galaxies.items.len) : (fstIndex += 1) {
        var sndIndex: usize = fstIndex + 1;
        while (sndIndex < galaxies.items.len) : (sndIndex += 1) {
            const fst = galaxies.items[fstIndex];
            const snd = galaxies.items[sndIndex];
            const len = fst.shortestPathLength(snd, &emptySpace);

            sum_of_path_lengths += len;
        }
    }

    try stdout.writer().print("{}\n", .{sum_of_path_lengths});
}

const Galaxy = struct {
    x: usize,
    y: usize,

    fn init(x: usize, y: usize) Galaxy {
        return Galaxy{ .x = x, .y = y };
    }

    fn shortestPathLength(self: Galaxy, other: Galaxy, empty: *EmptySpace) usize {
        const minCol = @min(self.x, other.x);
        const maxCol = @max(self.x, other.x);
        const col_dist = maxCol - minCol + empty.colsBetween(minCol, maxCol);

        const minRow = @min(self.y, other.y);
        const maxRow = @max(self.y, other.y);
        const row_dist = maxRow - minRow + empty.rowsBetween(minRow, maxRow);

        return col_dist + row_dist;
    }
};

const EmptySpace = struct {
    rows: []bool,
    cols: []bool,

    fn init(rowCount: usize, colCount: usize) !EmptySpace {
        const rows = try allocator.alloc(bool, rowCount);
        errdefer allocator.free(rows);
        var rowIndex: usize = 0;
        while (rowIndex < rowCount) : (rowIndex += 1) {
            rows[rowIndex] = true;
        }

        const cols = try allocator.alloc(bool, colCount);
        errdefer allocator.free(cols);
        var colIndex: usize = 0;
        while (colIndex < colCount) : (colIndex += 1) {
            cols[colIndex] = true;
        }

        return EmptySpace{ .rows = rows, .cols = cols };
    }

    fn deinit(self: *EmptySpace) void {
        allocator.free(self.rows);
        allocator.free(self.cols);
    }

    fn unset(self: *EmptySpace, galaxies: []Galaxy) void {
        for (galaxies) |galaxy| {
            self.rows[galaxy.y] = false;
            self.cols[galaxy.x] = false;
        }
    }

    fn rowsBetween(self: *EmptySpace, start: usize, end: usize) usize {
        var count: usize = 0;
        for (self.rows[start..end]) |row| {
            if (row) {
                count += 1;
            }
        }
        return count;
    }

    fn colsBetween(self: *EmptySpace, start: usize, end: usize) usize {
        var count: usize = 0;
        for (self.cols[start..end]) |col| {
            if (col) {
                count += 1;
            }
        }
        return count;
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
