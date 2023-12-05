const std = @import("std");

const stdout = std.io.getStdOut();
var stdin = std.io.bufferedReader(std.io.getStdIn().reader());
var buf: [4096]u8 = undefined;

fn readLine() !?[]const u8 {
    return try stdin.reader().readUntilDelimiterOrEof(&buf, '\n');
}

const RangeConversion = struct {
    destinationRangeStart: usize,
    sourceRangeStart: usize,
    rangeLength: usize,

    fn fromString(str: []const u8) !RangeConversion {
        var nums = std.mem.splitSequence(u8, str, " ");
        return RangeConversion{
            .destinationRangeStart = try std.fmt.parseInt(usize, nums.next().?, 10),
            .sourceRangeStart = try std.fmt.parseInt(usize, nums.next().?, 10),
            .rangeLength = try std.fmt.parseInt(usize, nums.next().?, 10),
        };
    }

    fn hasSourceValue(self: RangeConversion, value: usize) bool {
        return self.sourceRangeStart <= value and value < (self.sourceRangeStart + self.rangeLength);
    }

    fn destinationValue(self: RangeConversion, sourceValue: usize) usize {
        return self.destinationRangeStart + (sourceValue - self.sourceRangeStart);
    }
};

const ConversionMap = struct {
    conversions: std.ArrayList(RangeConversion),

    fn init(allocator: std.mem.Allocator) ConversionMap {
        return ConversionMap{ .conversions = std.ArrayList(RangeConversion).init(allocator) };
    }

    fn deinit(self: ConversionMap) void {
        self.conversions.deinit();
    }

    fn read(allocator: std.mem.Allocator) !?ConversionMap {
        const name = try readLine();
        if (name == null) { // no more lines to read
            return null;
        }
        var result = ConversionMap.init(allocator);
        errdefer result.deinit();
        while (try readLine()) |line| {
            if (line.len == 0) {
                break;
            }
            try result.conversions.append(try RangeConversion.fromString(line));
        }
        return result;
    }

    fn destinationValue(self: ConversionMap, sourceValue: usize) usize {
        for (self.conversions.items) |conversion| {
            if (conversion.hasSourceValue(sourceValue)) {
                return conversion.destinationValue(sourceValue);
            }
        }
        return sourceValue;
    }
};

const Almanac = struct {
    seeds: std.ArrayList(usize),
    maps: std.ArrayList(ConversionMap),

    fn init(allocator: std.mem.Allocator) Almanac {
        return Almanac{
            .seeds = std.ArrayList(usize).init(allocator),
            .maps = std.ArrayList(ConversionMap).init(allocator),
        };
    }

    fn deinit(self: Almanac) void {
        self.seeds.deinit();
        for (self.maps.items) |map| {
            map.deinit();
        }
        self.maps.deinit();
    }

    fn read(allocator: std.mem.Allocator) !Almanac {
        var result = Almanac.init(allocator);
        errdefer result.deinit();

        try result.readSeeds();
        while (try ConversionMap.read(allocator)) |map| {
            try result.maps.append(map);
        }

        return result;
    }

    fn readSeeds(self: *Almanac) !void {
        const line = try readLine();
        var parts =
            std.mem.splitSequence(u8, line.?, ": ");
        _ = parts.next(); // skip "seeds"
        var nums =
            std.mem.splitSequence(u8, parts.next().?, " ");
        while (nums.next()) |num| {
            try self.seeds.append(try std.fmt.parseInt(usize, num, 10));
        }
        _ = try readLine(); // skip empty line
    }

    fn destinationValue(self: Almanac, sourceValue: usize) usize {
        var result = sourceValue;
        for (self.maps.items) |map| {
            result = map.destinationValue(result);
        }
        return result;
    }

    fn minSeedDestination(self: Almanac) usize {
        var result = self.destinationValue(self.seeds.items[0]);
        for (self.seeds.items[1..]) |seed| {
            const value = self.destinationValue(seed);
            if (value < result) {
                result = value;
            }
        }
        return result;
    }
};

pub fn main() !void {
    var almanac = try Almanac.read(std.heap.page_allocator);
    defer almanac.deinit();

    try stdout.writer().print("{d}\n", .{almanac.minSeedDestination()});
}
