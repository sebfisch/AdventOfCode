const std = @import("std");

pub fn main() !void {
    var almanac = try Almanac.read();
    defer almanac.deinit();

    try stdout.writer().print("{d}\n", .{try almanac.minSeedDestination()});
}

const Almanac = struct {
    seeds: Ranges,
    maps: std.ArrayList(ConversionMap),

    fn deinit(self: Almanac) void {
        self.seeds.deinit();
        for (self.maps.items) |map| {
            map.deinit();
        }
        self.maps.deinit();
    }

    fn read() !Almanac {
        var result = Almanac{
            .seeds = undefined,
            .maps = std.ArrayList(ConversionMap).init(allocator),
        };
        errdefer result.deinit();

        if (try readLine()) |line| {
            result.seeds = try Ranges.fromString(skip(line, ": "));
            _ = try readLine(); // skip empty line
        }

        while (try ConversionMap.read()) |map| {
            try result.maps.append(map);
        }

        return result;
    }

    fn minSeedDestination(self: *Almanac) !usize {
        for (self.maps.items) |map| {
            const dest = try map.applyAll(self.seeds);
            self.seeds.deinit();
            self.seeds = dest;
        }

        return self.seeds.sorted.items[0].min;
    }
};

const Range = struct {
    min: usize,
    max: usize,

    fn contains(self: Range, other: Range) bool {
        return self.min <= other.min and other.max <= self.max;
    }

    fn precedes(self: Range, other: Range) bool { // without touching
        return self.max + 1 < other.min;
    }

    fn overlaps(self: Range, other: Range) bool {
        return self.min <= other.max and other.min <= self.max;
    }

    fn add(self: Range, delta: usize) Range {
        return Range{
            .min = self.min + delta,
            .max = self.max + delta,
        };
    }

    fn subtract(self: Range, delta: usize) Range {
        return Range{
            .min = self.min - delta,
            .max = self.max - delta,
        };
    }

    fn unify(self: Range, other: Range) Range { // only if touching
        std.debug.assert(!self.precedes(other) or !other.precedes(self));

        var result = self;
        if (other.min < result.min) {
            result.min = other.min;
        }
        if (other.max > result.max) {
            result.max = other.max;
        }

        return result;
    }

    fn intersect(self: Range, other: Range) Range { // only if overlapping
        std.debug.assert(self.overlaps(other));

        var result = self;
        if (other.min > result.min) {
            result.min = other.min;
        }
        if (other.max < result.max) {
            result.max = other.max;
        }

        return result;
    }
};

const RangeConversion = struct {
    destRangeMin: usize,
    sourceRange: Range,

    fn fromString(str: []const u8) !RangeConversion {
        var nums = split(str, " ");
        const destRangeMin = try parseDecimal(usize, nums.next().?);
        const srcMin = try parseDecimal(usize, nums.next().?);
        const srcLen = try parseDecimal(usize, nums.next().?);
        return RangeConversion{
            .destRangeMin = destRangeMin,
            .sourceRange = Range{ .min = srcMin, .max = srcMin + srcLen - 1 },
        };
    }

    fn apply(self: RangeConversion, arg: Range) Range {
        std.debug.assert(self.sourceRange.contains(arg));

        var result: Range = undefined;
        if (self.destRangeMin < self.sourceRange.min) {
            result = arg.subtract(self.sourceRange.min - self.destRangeMin);
        } else {
            result = arg.add(self.destRangeMin - self.sourceRange.min);
        }
        return result;
    }
};

const Ranges = struct {
    sorted: std.ArrayList(Range),

    fn init() Ranges {
        return Ranges{ .sorted = std.ArrayList(Range).init(allocator) };
    }

    fn deinit(self: Ranges) void {
        self.sorted.deinit();
    }

    fn fromString(str: []const u8) !Ranges {
        var result = Ranges.init();
        errdefer result.deinit();
        var nums = split(str, " ");
        while (nums.next()) |num| {
            const min = try parseDecimal(usize, num);
            const length = try parseDecimal(usize, nums.next().?);
            try result.insert(Range{
                .min = min,
                .max = min + length - 1,
            });
        }
        return result;
    }

    fn insert(self: *Ranges, range: Range) !void {
        // if this is the first range, insert it and return
        if (self.sorted.items.len == 0) {
            _ = try self.sorted.append(range);
            return;
        }

        // find first range that is not before the given one, without touching
        var firstIndex: usize = 0;
        var first: Range = undefined;
        while (firstIndex < self.sorted.items.len) : (firstIndex += 1) {
            first = self.sorted.items[firstIndex];
            if (!first.precedes(range)) {
                break;
            }
        }

        // if no such range was found, insert at the end and return
        if (firstIndex == self.sorted.items.len) {
            _ = try self.sorted.append(range);
            return;
        }

        // if the given range is before the next one, insert it here and return
        if (range.precedes(first)) {
            _ = try self.sorted.insert(firstIndex, range);
            return;
        }

        // find first index of range that is after the given one, without touching
        var nextIndex = firstIndex;
        var next: Range = undefined;
        while (nextIndex < self.sorted.items.len) : (nextIndex += 1) {
            next = self.sorted.items[nextIndex];
            if (range.precedes(next)) {
                break;
            }
        }

        // compute merged range of given range with all ranges from first until before next
        const merged = range.unify(first).unify(self.sorted.items[nextIndex - 1]);

        // replace all ranges from first until last with merged range
        _ = try self.sorted.replaceRange(
            firstIndex,
            nextIndex - firstIndex,
            &.{merged},
        );
    }

    fn remove(self: *Ranges, range: Range) !void {
        // find first range that overlaps with the given one
        var firstIndex: usize = 0;
        var first: Range = undefined;
        while (firstIndex < self.sorted.items.len) : (firstIndex += 1) {
            first = self.sorted.items[firstIndex];
            if (first.overlaps(range)) {
                break;
            }
        }

        // if no such range was found, there is nothing to remove
        if (firstIndex == self.sorted.items.len) {
            return;
        }

        // find first range, starting from firstIndex, that does not overlap with the given one
        var nextIndex = firstIndex;
        var next: Range = undefined;
        while (nextIndex < self.sorted.items.len) : (nextIndex += 1) {
            next = self.sorted.items[nextIndex];
            if (!next.overlaps(range)) {
                break;
            }
        }

        var lastIndex = nextIndex - 1;

        // remove all ranges between first and last
        if (firstIndex + 1 < lastIndex) {
            _ = try self.sorted.replaceRange(
                firstIndex + 1,
                nextIndex - firstIndex - 2,
                &.{},
            );
            lastIndex = firstIndex + 1;
        }

        if (firstIndex < lastIndex) { // there are exactly two overlapping ranges
            var last = self.sorted.items[lastIndex];
            // range.min < last.min, because there is another overlapping range before last
            // if the given range contains the last overlapping range, remove it
            if (range.contains(last)) {
                _ = self.sorted.orderedRemove(lastIndex);
            } else { // range.max < last.max
                last.min = range.max + 1;
                self.sorted.items[lastIndex] = last;
            }
            lastIndex -= 1;
        }

        // now there is only one overlapping range left and firstIndex == lastIndex

        // if the given range contains the overlapping range, remove it
        if (range.contains(first)) {
            _ = self.sorted.orderedRemove(firstIndex);
            return;
        }

        if (range.min <= first.min) { // range.max < first.max
            first.min = range.max + 1;
            self.sorted.items[firstIndex] = first;
            return;
        }

        if (range.max >= first.max) { // first.min < range.min
            first.max = range.min - 1;
            self.sorted.items[firstIndex] = first;
            return;
        }

        // first.min < range.min and range.max < first.max
        // split first into two ranges
        var left = first;
        var right = first;
        left.max = range.min - 1;
        right.min = range.max + 1;
        _ = try self.sorted.replaceRange(
            firstIndex,
            1,
            &.{ left, right },
        );
    }
};

const ConversionMap = struct {
    conversions: std.ArrayList(RangeConversion),

    fn init() ConversionMap {
        return ConversionMap{ .conversions = std.ArrayList(RangeConversion).init(allocator) };
    }

    fn deinit(self: ConversionMap) void {
        self.conversions.deinit();
    }

    fn read() !?ConversionMap {
        const name = try readLine();
        if (name == null) { // no more lines to read
            return null;
        }
        var result = ConversionMap.init();
        errdefer result.deinit();
        while (try readLine()) |line| {
            if (line.len == 0) {
                break;
            }
            try result.conversions.append(try RangeConversion.fromString(line));
        }
        return result;
    }

    fn apply(self: ConversionMap, source: Range) !Ranges { // caller must deinit result
        var sources = Ranges.init();
        defer sources.deinit();
        try sources.insert(source);

        var results = Ranges.init();
        errdefer results.deinit();

        for (self.conversions.items) |conv| {
            var sourceIndex: usize = 0;
            while (sourceIndex < sources.sorted.items.len) : (sourceIndex += 1) {
                const src = sources.sorted.items[sourceIndex];
                if (src.overlaps(conv.sourceRange)) {
                    const arg = src.intersect(conv.sourceRange);
                    try sources.remove(arg);
                    try results.insert(conv.apply(arg));
                }
            }
        }

        // numbers that are not covered by any conversion are copied as-is
        for (sources.sorted.items) |src| {
            try results.insert(src);
        }

        return results;
    }

    fn applyAll(self: ConversionMap, source: Ranges) !Ranges { // caller must deinit result
        var result = Ranges.init();
        errdefer result.deinit();
        for (source.sorted.items) |sourceRange| {
            const destRanges = try self.apply(sourceRange);
            defer destRanges.deinit();
            for (destRanges.sorted.items) |destRange| {
                try result.insert(destRange);
            }
        }
        return result;
    }
};

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

fn skip(str: []const u8, delimiter: []const u8) []const u8 {
    var parts = split(str, delimiter);
    _ = parts.next();
    return parts.next().?;
}

fn parseDecimal(comptime T: type, str: []const u8) !T {
    return try std.fmt.parseInt(T, str, 10);
}
