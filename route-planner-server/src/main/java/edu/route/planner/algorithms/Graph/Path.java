package edu.route.planner.algorithms.Graph;

import edu.route.planner.model.WayEdge;

import java.util.ArrayList;
import java.util.List;

public class Path implements Comparable<Path> {
    public List<Edge> edges;

    public Path(List<Edge> edges){
        this.edges = edges;
    }

    public void addPath(List<Edge> edges){
        this.edges.addAll(edges);
    }

    @Override
    public int compareTo(Path path) {
        double pathCost = path.getCost();
        if(this.getCost().equals(pathCost))
            return 0;
        if(this.getCost() > pathCost)
            return 1;
        return -1;
    }

    private Double getCost(){
        double cost = 0.0;
        for(Edge e: edges){
            cost += e.getDistance();
        }
        return cost;
    }

    public static Double calculatePathDistance(List<Edge> edges){
        Double result = 0.0;
        for (Edge e: edges)
            result += e.getDistance();

        return result;
    }

    public static Double calculatePathDuration(List<Edge> edges){
        Double result = 0.0;
        for (Edge e: edges)
            result += e.getDuration();

        return result;
    }

    public static Double calculatePathDistance(Iterable<WayEdge> edges){
        Double result = 0.0;
        for(WayEdge we: edges)
            result += we.getDistance();

        return result;
    }

    public static Double calculatePathDuration(Iterable<WayEdge> edges){
        Double result = 0.0;
        for (WayEdge we: edges)
            result += we.getDuration();

        return result;
    }

    public static List<Long> getVertices(List<Edge> edges){
        List<Long> result = new ArrayList<>();
        for(Edge e: edges){
            result.add(e.getStartId());
        }
        result.add(edges.get(edges.size() - 1).getDestinationId());

        return result;
    }
}
