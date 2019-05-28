package edu.route.planner.algorithms;

import edu.route.planner.model.CityNode;
import edu.route.planner.model.WayEdge;
import edu.route.planner.utils.Osrm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.ToDoubleFunction;

import static java.lang.Math.exp;

public abstract class SimulatedAnnealing {

    private static final Random rand = new Random();

    public static Iterable<WayEdge> run(Parameters parameters, Constraints constraints,
                                        CityNode sourceNode, CityNode destinationNode, List<CityNode> optionalNodes) {

        double temperature = parameters.initialTemperature;

        List<CityNode> additionalNodes = new ArrayList<>();
        List<WayEdge> initial = generateWayEdges(sourceNode, destinationNode, additionalNodes);
        List<WayEdge> previousSolution = new ArrayList<>(initial);
        double previousCost = cost(previousSolution);
        double i = 0;
        while (i < parameters.maxIterations) {

            List<WayEdge> currentSolution = modify(constraints, initial, sourceNode, destinationNode, additionalNodes, optionalNodes);

            double currentCost = cost(previousSolution);
            double costDifference = currentCost - previousCost;
            if (costDifference < 0 || rand.nextDouble() < exp(-costDifference / temperature)) {
                previousSolution = currentSolution;
                previousCost = currentCost;
            }

            temperature *= parameters.coolingFactor;
            ++i;
        }

        return previousSolution;
    }

    private static List<WayEdge> modify(Constraints constraints, List<WayEdge> initial,
                                        CityNode sourceNode, CityNode destinationNode,
                                        List<CityNode> additionalNodes, List<CityNode> availableNodes) {

        List<WayEdge> proposed = null;
        boolean accepted = false;
        while (!accepted) {
            if (availableNodes.size() > 0) {
                CityNode nodeToAdd = randomNode(availableNodes);
                additionalNodes.add(nodeToAdd);
                availableNodes.remove(nodeToAdd);
            }
            if (rand.nextDouble() > 0.5) {
                CityNode nodeToRemove = randomNode(additionalNodes);
                additionalNodes.remove(nodeToRemove);
                availableNodes.add(nodeToRemove);
            }
            proposed = generateWayEdges(sourceNode, destinationNode, additionalNodes);
            accepted = respectsConstraints(constraints, initial, proposed);
        }
        return proposed;
    }

    private static CityNode randomNode(List<CityNode> nodes) {
        return nodes.get(rand.nextInt(nodes.size()));
    }

    private static List<WayEdge> generateWayEdges(CityNode sourceNode, CityNode destinationNode,
                                                  List<CityNode> additionalNodes) {
        List<CityNode> nodes = new ArrayList<>();
        nodes.add(sourceNode);
        nodes.addAll(additionalNodes);
        nodes.add(destinationNode);

        List<WayEdge> edges = new ArrayList<>();
        for (int i = 0; i < nodes.size() - 1; i++) {
            try {
                edges.add(Osrm.getFastestRoute(nodes.get(i), nodes.get(i + 1)));
            } catch (IOException ignored) {
                throw new RuntimeException();
            }
        }
        return edges;
    }

    private static double summary(List<WayEdge> edges, ToDoubleFunction<WayEdge> mapping) {
        return edges.stream().mapToDouble(mapping).sum();
    }

    private static boolean respectsConstraints(Constraints constraints, List<WayEdge> initial, List<WayEdge> proposed) {
        double distanceDifference = summary(proposed, WayEdge::getDistance) - summary(initial, WayEdge::getDistance);
        double durationDifference = summary(proposed, WayEdge::getDuration) - summary(initial, WayEdge::getDuration);
        return distanceDifference <= constraints.distanceBuffer
                && durationDifference <= constraints.durationBuffer;
    }

    private static double cost(List<WayEdge> edges) {
        return 1.0 / edges.size();
    }

    static class Parameters {
        double initialTemperature;
        double coolingFactor;
        double maxIterations;
    }

    static class Constraints {
        double distanceBuffer;
        double durationBuffer;
    }
}
