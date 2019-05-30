package edu.route.planner.algorithms;

import edu.route.planner.model.WayEdge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import static java.lang.Math.exp;
import static java.util.Collections.shuffle;
import static java.util.Collections.singletonList;

public class SimulatedAnnealing extends Algorithm {

    private static final Logger logger = LoggerFactory.getLogger(SimulatedAnnealing.class);

    private static final Random random = new Random(42);

    private static final double ITERATIONS = 100;
    private static final double INITIAL_TEMPERATURE = 100.0;
    private static final double COOLING_FACTOR = 0.95;

    public SimulatedAnnealing(WayEdge directWayEdge, Collection<WayEdge> alternativeWayEdges,
                              double distanceBuffer, double durationBuffer) {
        super(directWayEdge, alternativeWayEdges, distanceBuffer, durationBuffer);
    }

    private static double calculateCost(List<WayEdge> edges) {
        return 1.0 / edges.size();
    }

    @Override
    public List<WayEdge> run() {

        logger.debug("Considering {} way edges", alternativeWayEdges.size());
        List<WayEdge> result = singletonList(directWayEdge);
        double cost = calculateCost(result);
        List<WayEdge> bestResult = result;

        double temperature = INITIAL_TEMPERATURE;
        double i = 0;
        while (i < ITERATIONS) {
            List<WayEdge> modifiedResult = modify(result);
            double modifiedCost = calculateCost(modifiedResult);
            double costDifference = modifiedCost - cost;
            if (costDifference < 0 || random.nextDouble() < exp(-costDifference / temperature)) {
                result = modifiedResult;
                cost = modifiedCost;
                if (isBetter(modifiedResult, bestResult)) {
                    logger.debug("New best result: {}", toString(bestResult));
                    bestResult = modifiedResult;
                }
            }
            temperature *= COOLING_FACTOR;
            ++i;
        }

        return bestResult;
    }

    private List<WayEdge> modify(List<WayEdge> previousResult) {
        if (previousResult.size() > 1) {
            int lastEdgeIndex = calculateLastEdgeIndex(previousResult);
            if (lastEdgeIndex != 0) {
                List<WayEdge> result = new ArrayList<>(previousResult.subList(0, lastEdgeIndex));
                return depthFirstSearch(result.get(result.size() - 1).getDestinationCityNodeId(), result);
            }
        }
        return depthFirstSearch(directWayEdge.getSourceCityNodeId(), new ArrayList<>());
    }

    private int calculateLastEdgeIndex(List<WayEdge> edges) {
        int total = (1 + edges.size()) / 2 * edges.size();
        int pick = random.nextInt(total);
        int current = 0;
        for (int i = 0; i < edges.size(); i++) {
            current += i + 1;
            if (current > pick) {
                return i;
            }
        }
        return 0;
    }

    private List<WayEdge> depthFirstSearch(long currentNode, List<WayEdge> result) {
        if (!result.isEmpty() && isFinal(result)) {
            return result;
        }

        List<WayEdge> outEdges = sourceCityNodeToWayEdge.get(currentNode);
        shuffle(outEdges, random);
        for (WayEdge nextEdge : outEdges) {
            result.add(nextEdge);
            if (isAcceptable(result)) {
                long nextNode = nextEdge.getDestinationCityNodeId();
                result = depthFirstSearch(nextNode, result);
                if (isFinal(result)) {
                    return result;
                }
            }
            result.remove(result.size() - 1);
        }
        return result;
    }
}
