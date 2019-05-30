package edu.route.planner.algorithms;

import edu.route.planner.model.WayEdge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.Collections.singletonList;

public class BruteForce extends Algorithm {

    private static final Logger logger = LoggerFactory.getLogger(BruteForce.class);

    public BruteForce(WayEdge directWayEdge, Collection<WayEdge> alternativeWayEdges,
                      double distanceBuffer, double durationBuffer) {
        super(directWayEdge, alternativeWayEdges, distanceBuffer, durationBuffer);
    }

    @Override
    public List<WayEdge> run() {

        logger.debug("Considering {} way edges", alternativeWayEdges.size());
        List<WayEdge> result = singletonList(directWayEdge);

        long sourceNode = directWayEdge.getSourceCityNodeId();
        if (sourceCityNodeToWayEdge.containsKey(sourceNode)) {
            result = depthFirstSearch(sourceNode, new ArrayList<>(), result);
        }
        return result;
    }

    private List<WayEdge> depthFirstSearch(long currentNode, List<WayEdge> currentResult, List<WayEdge> bestResult) {

        for (WayEdge nextEdge : sourceCityNodeToWayEdge.get(currentNode)) {
            currentResult.add(nextEdge);
            if (isAcceptable(currentResult)) {
                if (isFinal(currentResult)) {
                    if (isBetter(currentResult, bestResult)) {
                        bestResult = new ArrayList<>(currentResult);
                        logger.debug("New best result: {}", toString(bestResult));
                    }
                } else {
                    long nextNode = nextEdge.getDestinationCityNodeId();
                    bestResult = depthFirstSearch(nextNode, currentResult, bestResult);
                }
            }
            currentResult.remove(currentResult.size() - 1);
        }
        return bestResult;
    }
}
