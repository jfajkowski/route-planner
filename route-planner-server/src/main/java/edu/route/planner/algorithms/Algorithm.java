package edu.route.planner.algorithms;

import edu.route.planner.model.WayEdge;

import java.util.*;
import java.util.function.ToDoubleFunction;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

public abstract class Algorithm {

    final WayEdge directWayEdge;
    final Collection<WayEdge> alternativeWayEdges;
    final Map<Long, List<WayEdge>> sourceCityNodeToWayEdge;
    private final double maxDistance;
    private final double maxDuration;
    private final long maxEdges;

    Algorithm(WayEdge directWayEdge, Collection<WayEdge> alternativeWayEdges, double distanceBuffer, double durationBuffer) {
        this.directWayEdge = directWayEdge;
        this.alternativeWayEdges = alternativeWayEdges;
        this.sourceCityNodeToWayEdge = alternativeWayEdges.stream().collect(groupingBy(WayEdge::getSourceCityNodeId));
        this.maxEdges = calculateMaxEdges(directWayEdge, alternativeWayEdges);
        this.maxDistance = directWayEdge.getDistance() + distanceBuffer;
        this.maxDuration = directWayEdge.getDuration() + durationBuffer;
    }

    private static long calculateMaxEdges(WayEdge directWayEdge, Collection<WayEdge> alternativeWayEdges) {
        Set<Long> uniqueCityNodes = new HashSet<>();
        uniqueCityNodes.add(directWayEdge.getSourceCityNodeId());
        uniqueCityNodes.add(directWayEdge.getDestinationCityNodeId());
        alternativeWayEdges.forEach(we -> {
            uniqueCityNodes.add(we.getSourceCityNodeId());
            uniqueCityNodes.add(we.getDestinationCityNodeId());
        });
        return uniqueCityNodes.size() - 1;
    }

    static boolean isBetter(List<WayEdge> result, List<WayEdge> bestResult) {
        return result.size() > bestResult.size() || (result.size() == bestResult.size()
                && summary(result, WayEdge::getDistance) < summary(bestResult, WayEdge::getDistance)
                && summary(result, WayEdge::getDuration) < summary(bestResult, WayEdge::getDuration));
    }

    private static double summary(List<WayEdge> edges, ToDoubleFunction<WayEdge> mapping) {
        return edges.stream().mapToDouble(mapping).sum();
    }

    public static List<Long> toAllCityIds(List<WayEdge> edges) {
        if (edges.isEmpty()) {
            return emptyList();
        }
        List<Long> cityIds = new ArrayList<>(edges.size() + 1);
        cityIds.add(0, edges.get(0).getSourceCityNodeId());
        cityIds.addAll(edges.stream().map(WayEdge::getDestinationCityNodeId).collect(toList()));
        return cityIds;
    }

    private static List<Long> toOptionalCityIds(List<WayEdge> edges) {
        if (edges.size() <= 1) {
            return emptyList();
        }
        List<Long> cityIds = toAllCityIds(edges);
        return cityIds.subList(1, cityIds.size() - 2);
    }

    public static String toString(List<WayEdge> edges) {
        List<Long> cityIds = toAllCityIds(edges);
        return String.format("size %s, distance %s, duration %s, nodes %s", cityIds.size(),
                summary(edges, WayEdge::getDistance), summary(edges, WayEdge::getDuration), cityIds);
    }

    public abstract List<WayEdge> run();

    boolean isAcceptable(List<WayEdge> result) {
        return result.size() <= maxEdges
                && summary(result, WayEdge::getDistance) <= maxDistance
                && summary(result, WayEdge::getDuration) <= maxDuration
                && toOptionalCityIds(result).size() == new HashSet<>(toOptionalCityIds(result)).size()
                && isWithoutCycles(result);
    }

    private boolean isWithoutCycles(List<WayEdge> result) {
        List<Long> cityIds = toAllCityIds(result);
        return cityIds.size() == new HashSet<>(cityIds).size();
    }

    boolean isFinal(List<WayEdge> result) {
        return result.get(result.size() - 1).getDestinationCityNodeId().equals(directWayEdge.getDestinationCityNodeId());
    }
}
