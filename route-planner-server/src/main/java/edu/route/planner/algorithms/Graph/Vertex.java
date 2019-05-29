package edu.route.planner.algorithms.Graph;

import java.util.*;

public class Vertex {
    private Long Id;
    private Double heuristic;
    private Map<Long, Edge> edges;

    public Long getId() {
        return Id;
    }

    public Double getHeuristic() {
        return heuristic;
    }

    public Map<Long, Edge> getEdges() {
        return edges;
    }

    public void addEdge(Edge edge) {
        this.edges.put(edge.getId(), edge);
    }

    public Edge getEdge(Vertex vertex){
        for (Edge edge: edges.values()) {
            if(edge.getDestinationId().equals(vertex.Id)){
                return edge;
            }
        }
        return null;
    }

    public void removeEdge(Long edgeId){
        edges.remove(edgeId);
    }

    public Vertex(Long Id, Double heuristic) {
        this.Id = Id;
        this.heuristic = heuristic;
        edges = new HashMap<Long, Edge>();
    }

    public Vertex(Vertex vertex){
        this(vertex.Id, vertex.heuristic);
        this.edges.putAll(vertex.edges);
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
        result = 31 * result + Id.hashCode() + heuristic.intValue();
        return 31 * (result + edges.size());
    }
}
