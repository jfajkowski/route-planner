package edu.route.planner.algorithms.Graph;

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

    public Double getCost(){
        double cost = 0.0;
        for(Edge e: edges){
            cost += e.getDistance();
        }
        return cost;
    }
}
