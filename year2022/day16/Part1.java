package year2022.day16;

// JEP 395: Records

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Part1 {
    public static void main(String[] args) {
        try (Stream<String> lines = inputLines()) {
            List<Valve> valves = lines
                    .map(Valve::fromString)
                    .toList();

            SearchState.init(valves).searchForMaxRelease();
            System.out.println(SearchState.maxRelease);
        }
    }

    record SearchState(
            Map<String, Valve> valves,
            String current,
            int minutesLeft,
            int eventualRelease,
            Set<String> opened,
            int closedRate,
            Set<String> idsSinceLastRelease) {

        static SearchState init(List<Valve> valves) {
            String current = "AA";
            int minutesLeft = 30;
            int eventualRelease = 0;
            Set<String> opened = Set.of();
            int closedRate = valves.stream()
                    .map(Valve::flowRate)
                    .reduce(0, (x, y) -> x + y);

            return new SearchState(
                    valves.stream().collect(Collectors.toMap(Valve::id, v -> v)),
                    current, minutesLeft,
                    eventualRelease, opened, closedRate, Set.of());
        }

        List<SearchState> next() {
            Valve valve = valves.get(current);
            int minutes = minutesLeft - 1;
            List<SearchState> result = new LinkedList<>();

            if (valve.flowRate > 0 && !opened.contains(current)) {
                Set<String> open = new HashSet<>(opened);
                open.add(current);

                SearchState release = new SearchState(
                        valves, current, minutes,
                        eventualRelease + valve.flowRate * minutes,
                        open,
                        closedRate - valve.flowRate, Set.of());

                result.add(release);
            }

            valve.tunnels.stream()
                    .map(next -> new SearchState(
                            valves, next, minutes,
                            eventualRelease, opened, closedRate,
                            add(idsSinceLastRelease, current)))
                    .forEach(result::add);

            return result;
        }

        int upperBound() {
            return eventualRelease + minutesLeft * closedRate;
        }

        static int maxRelease = 0;

        void searchForMaxRelease() {
            if (minutesLeft == 0 || upperBound() < maxRelease) {
                if (eventualRelease > maxRelease) {
                    maxRelease = eventualRelease;
                }
                return;
            }

            next().stream()
                    .filter(state -> !idsSinceLastRelease.contains(state.current))
                    .forEach(SearchState::searchForMaxRelease);
        }
    }

    record Valve(String id, int flowRate, List<String> tunnels) {
        static Valve fromString(String line) {
            Pattern pattern = Pattern.compile(
                    "Valve (..) has flow rate=(\\d+); tunnels? leads? to valves? (.+)");
            Matcher matcher = pattern.matcher(line);
            matcher.find();

            String id = matcher.group(1);
            int flowRate = Integer.parseInt(matcher.group(2));
            String[] tunnels = matcher.group(3).split(", ");

            return new Valve(id, flowRate, List.of(tunnels));
        }
    }

    static <T> Set<T> add(Set<T> elems, T elem) {
        Set<T> result = new HashSet<>(elems);
        result.add(elem);
        return result;
    }

    static Stream<String> inputLines() {
        return new BufferedReader(new InputStreamReader(System.in)).lines();
    }
}
