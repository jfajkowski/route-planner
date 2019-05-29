package edu.route.planner.algorithms;

import edu.route.planner.algorithms.Graph.Edge;
import edu.route.planner.algorithms.Graph.NodesGraph;
import edu.route.planner.algorithms.Graph.Path;
import edu.route.planner.algorithms.Graph.Vertex;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RouterAlgorithm {
    private Vertex source;
    private Vertex destination;
    private NodesGraph graph;
    private Double maxDuration;
    private Double maxDistance;

    public RouterAlgorithm(Vertex source, Vertex destination, NodesGraph graph, Double maxDistance, Double maxDuration){
        this.source = source;
        this.destination = destination;
        this.graph = graph;
        this.maxDistance = maxDistance;
        this.maxDuration = maxDuration;
    }

    public List<Long> calculateRoute(){
        YenAlgorithm yen = new YenAlgorithm(maxDistance, maxDuration, source, destination, graph);
        List<List<Edge>> paths = yen.calculate();

        Vertex newSource = new Vertex(destination);
        Vertex newDestination = new Vertex(source);
        YenAlgorithm reversedYen = new YenAlgorithm(maxDistance, maxDuration, newSource, newDestination, graph);
        paths.addAll(reversedYen.calculate());

        List<Edge> longest = new ArrayList<>();
        Double longestPathDistance = 0.0;
        Double longestPathDuration = 0.0;
        for (List<Edge> p: paths){
            Double dist = Path.calculatePathDistance(p);
            if(dist > longestPathDistance){
                longest = p;
                longestPathDistance = dist;
                longestPathDuration = Path.calculatePathDuration(p);
            }
        }

        List<Edge> lasting = new ArrayList<>();
        Double lastingPathDuration = 0.0;
        Double lastingPathDistance = 0.0;
        for (List<Edge> p: paths){
            Double duration = Path.calculatePathDuration(p);
            if(duration > lastingPathDuration){
                lasting = p;
                lastingPathDuration = duration;
                lastingPathDistance = Path.calculatePathDistance(p);
            }
        }


        if (longestPathDistance > lastingPathDistance && longestPathDuration > lastingPathDuration) return Path.getVertices(longest);
        if (lastingPathDistance > longestPathDistance && lastingPathDuration > longestPathDuration) return Path.getVertices(lasting);

        //if it cannot be simply settled which route is better, the longest one is returned.
        return Path.getVertices(longest);
    }
}
