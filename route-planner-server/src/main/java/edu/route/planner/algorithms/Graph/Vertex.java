package edu.route.planner.algorithms.Graph;

import java.util.ArrayList;
import java.util.List;

public class Vertex {
    private Long Id;
    private Double heuristic;
    private List<Edge> edges;
    private Double distanceFromStart;

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public Double getHeuristic() {
        return heuristic;
    }

    public void setHeuristic(Double heuristic) {
        this.heuristic = heuristic;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public void setEdges(List<Edge> edges) {
        this.edges = edges;
    }

    public Edge getEdge(Vertex vertex){
        for (Edge edge: edges) {
            if(edge.getDestination().equals(vertex)){
                return edge;
            }
        }
        return null;
    }

    public boolean pathToExists(Vertex v){
        for(Edge edge: edges){
            if(edge.getDestination().equals(v)){
                return true;
            }
        }
        return false;
    }

    public Double getDistanceFromStart() {
        return distanceFromStart;
    }

    public void setDistanceFromStart(Double distanceFromStart) {
        this.distanceFromStart = distanceFromStart;
    }

    public Vertex(Long Id, Double heuristic) {
        this.Id = Id;
        this.heuristic = heuristic;
        edges = new ArrayList<Edge>();
    }

    public Vertex(Vertex vertex){
        this(vertex.Id, vertex.heuristic);
        this.edges.addAll(vertex.edges);
    }

    @Override
    public boolean equals(Object obj){
        if(this == obj) return true;
        if(obj == null) return false;
        if(getClass() != obj.getClass()) return false;

        Vertex v = (Vertex)obj;
        return Id.equals(v.Id);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + Id.intValue() + heuristic.intValue();
        return 31 * (result + edges.size());
    }
}
