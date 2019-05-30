package edu.route.planner.algorithms;

import edu.route.planner.model.WayEdge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.ToDoubleFunction;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

public abstract class BruteForce {

    private static final Logger logger = LoggerFactory.getLogger(BruteForce.class);

    public static List<WayEdge> run(WayEdge directWayEdge, Collection<WayEdge> alternativeWayEdges,
                                 double distanceBuffer, double durationBuffer) {

        logger.info("Considering {} way edges", alternativeWayEdges.size());
        List<WayEdge> result = singletonList(directWayEdge);
        long maxEdges = calculateMaxEdges(directWayEdge, alternativeWayEdges);
        double maxDistance = directWayEdge.getDistance() + distanceBuffer;
        double maxDuration = directWayEdge.getDuration() + durationBuffer;

        Map<Long, List<WayEdge>> sourceCityToWayEdge = alternativeWayEdges.stream()
                .collect(groupingBy(WayEdge::getSourceCityNodeId));

        long sourceNode = directWayEdge.getSourceCityNodeId();
        if (sourceCityToWayEdge.containsKey(sourceNode)) {
            long destinationNode = directWayEdge.getDestinationCityNodeId();
            result = depthFirstSearch(sourceCityToWayEdge, sourceNode, destinationNode,
                    maxEdges, maxDistance, maxDuration,
                    new ArrayList<>(), result);
        }
        return result;
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

    private static List<WayEdge> depthFirstSearch(Map<Long, List<WayEdge>> sourceNodeToOutEdges,
                                                  long currentNode, long destinationNode,
                                                  long maxEdges, double maxDistance, double maxDuration,
                                                  List<WayEdge> currentResult, List<WayEdge> bestResult) {

        for (WayEdge nextEdge : sourceNodeToOutEdges.get(currentNode)) {
            currentResult.add(nextEdge);
            if (isAcceptable(currentResult, maxEdges, maxDistance, maxDuration)) {
                if (isFinal(currentResult, destinationNode)) {
                    if (isBetter(currentResult, bestResult)) {
                        bestResult = new ArrayList<>(currentResult);
                        logger.info("New best result: {}", toString(bestResult));
                    }
                } else {
                    long nextNode = nextEdge.getDestinationCityNodeId();
                    bestResult = depthFirstSearch(sourceNodeToOutEdges, nextNode, destinationNode,
                            maxEdges, maxDistance, maxDuration,
                            currentResult, bestResult);
                }
            }
            currentResult.remove(currentResult.size() - 1);
        }
        return bestResult;
    }

    private static boolean isAcceptable(List<WayEdge> currentResult, long maxEdges, double maxDistance, double maxDuration) {
        return currentResult.size() <= maxEdges
                && summary(currentResult, WayEdge::getDistance) <= maxDistance
                && summary(currentResult, WayEdge::getDuration) <= maxDuration
                && toOptionalCityIds(currentResult).size() == new HashSet<>(toOptionalCityIds(currentResult)).size();
    }

    private static boolean isFinal(List<WayEdge> currentResult, long destinationNode) {
        return currentResult.get(currentResult.size() - 1).getDestinationCityNodeId().equals(destinationNode);
    }

    private static boolean isBetter(List<WayEdge> currentResult, List<WayEdge> bestResult) {
        return currentResult.size() > bestResult.size() || (currentResult.size() == bestResult.size()
                && summary(currentResult, WayEdge::getDistance) < summary(bestResult, WayEdge::getDistance)
                && summary(currentResult, WayEdge::getDuration) < summary(bestResult, WayEdge::getDuration));
    }

    private static double summary(List<WayEdge> edges, ToDoubleFunction<WayEdge> mapping) {
        return edges.stream().mapToDouble(mapping).sum();
    }

    public static List<Long> toAllCityIds(List<WayEdge> edges) {
        if (edges.size() <= 1) {
            return emptyList();
        }
        List<Long> cityIds = new ArrayList<>(edges.size() + 1);
        cityIds.add(0, edges.get(0).getSourceCityNodeId());
        cityIds.addAll(edges.stream().map(WayEdge::getDestinationCityNodeId).collect(toList()));
        return cityIds;
    }

    public static List<Long> toOptionalCityIds(List<WayEdge> edges) {
        if (edges.size() <= 1) {
            return emptyList();
        }
        List<Long> cityIds = toAllCityIds(edges);
        return cityIds.subList(1, cityIds.size() - 2);
    }

    public static String toString(List<WayEdge> edges) {
        return String.format("size %s, distance %s, duration %s, nodes %s", edges.size(),
                summary(edges, WayEdge::getDistance), summary(edges, WayEdge::getDuration), toAllCityIds(edges));
    }
}
