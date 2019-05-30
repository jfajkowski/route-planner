package edu.route.planner.algorithms;

import edu.route.planner.algorithms.Graph.Edge;
import edu.route.planner.algorithms.Graph.NodesGraph;
import edu.route.planner.algorithms.Graph.Path;
import edu.route.planner.algorithms.Graph.Vertex;

import java.util.ArrayList;
import java.util.List;

public class RouterAlgorithm {
    private Vertex source;
    private Vertex destination;
    private NodesGraph graph;
    private NodesGraph reversedGraph;
    private Double maxDuration;
    private Double maxDistance;

    public RouterAlgorithm(Vertex source, Vertex destination, NodesGraph graph, NodesGraph reversedGraph, Double maxDistance, Double maxDuration){
        this.source = source;
        this.destination = destination;
        this.graph = graph;
        this.reversedGraph = reversedGraph;
        this.maxDistance = maxDistance;
        this.maxDuration = maxDuration;
    }

    public List<Edge> calculateRoute() {
        Vertex newSource = new Vertex(reversedGraph.getVertex(destination.getId()));
        Vertex newDestination = new Vertex(reversedGraph.getVertex(source.getId()));

        YenAlgorithm yen = new YenAlgorithm(maxDistance, maxDuration, new Vertex(source), new Vertex(destination), graph);
        List<List<Edge>> paths = yen.calculate();
        YenAlgorithm reversedYen = new YenAlgorithm(maxDistance, maxDuration, newSource, newDestination, reversedGraph);
        paths.addAll(reversedYen.calculate());

        List<Edge> longest = new ArrayList<>();
        int longestLength = 0;
        for (List<Edge> p: paths){
            int l = p.size();
            if(l > longestLength) {
                longest = p;
            }
        }

        return longest;
    }
}
