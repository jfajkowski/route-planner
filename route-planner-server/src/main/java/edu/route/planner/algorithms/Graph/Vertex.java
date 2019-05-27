package edu.route.planner.algorithms.Graph;

import java.util.*;

public class Vertex {
    private String Id;
    private Double heuristic;
    private Map<String, Edge> edges;
    private Double distanceFromStart;

    public String getId() {
        return Id;
    }

    public void setId(UUID id) {
        Id = id.toString();
    }

    public Double getHeuristic() {
        return heuristic;
    }

    public void setHeuristic(Double heuristic) {
        this.heuristic = heuristic;
    }

    public Map<String, Edge> getEdges() {
        return edges;
    }

    public void addEdge(Edge edge) {
        this.edges.put(edge.getId(), edge);
    }

    public void addEdge(String startId, String destinationId, Double distance) {
        UUID id = UUID.randomUUID();
        this.edges.put(id.toString(), new Edge(id, startId, destinationId, distance));
    }

    public Edge getEdge(Vertex vertex){
        for (Edge edge: edges.values()) {
            if(edge.getDestinationId().equals(vertex.Id)){
                return edge;
            }
        }
        return null;
    }

    public void removeEdge(String edgeId){
        edges.remove(edgeId);
    }

    public Double getDistanceFromStart() {
        return distanceFromStart;
    }

    public void setDistanceFromStart(Double distanceFromStart) {
        this.distanceFromStart = distanceFromStart;
    }

    public Vertex(String Id, Double heuristic) {
        this.Id = Id;
        this.heuristic = heuristic;
        edges = new HashMap<>();
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
