package edu.route.planner.algorithms.Graph;

import java.util.HashMap;
import java.util.Map;

public class NodesGraph {
    private Map<Long, Edge> edges;
    private Map<Long, Vertex> vertices;

    public void addEdge(Vertex start, Edge edge){
        if(start.equals(edge.getDestination())) throw new IllegalArgumentException("Edge cannot start and end up in the same vertex.");
        if(edges.get(edge.getId()) == null) return;
        edges.put(edge.getId(),edge);
        if(vertices.get(edge.getId()) == null) vertices.put(edge.getDestination().getId(), edge.getDestination());
        if(vertices.get(start.getId()) == null) vertices.put(start.getId(), start);
    }

    public Edge getEdge(Long Id){
        return edges.get(Id);
    }

    public Vertex getVertex(Long Id){
        return vertices.get(Id);
    }

    public Map<Long, Edge> getEdges() {
        return edges;
    }

    public void setEdges(Map<Long, Edge> edges) {
        this.edges = edges;
    }

    public Map<Long, Vertex> getVertices() {
        return vertices;
    }

    public void setVertices(Map<Long, Vertex> vertices) {
        this.vertices = vertices;
    }

    public NodesGraph() {
        edges = new HashMap<Long, Edge>();
        vertices = new HashMap<Long, Vertex>();
    }
}
