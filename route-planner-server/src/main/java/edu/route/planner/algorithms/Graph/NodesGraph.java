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
        vertices.putIfAbsent(start.getId(), start);
    }

    public void removeEdge(Vertex start, Edge edge){
        if(!vertices.containsKey(start.getId())) throw new IllegalArgumentException("Vertex of ID: " + start.getId() + " does not contain in this graph/");
        vertices.get(start.getId()).removeEdge(edge);
        edges.remove(edge);
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

    public void addVertex(Vertex v){
        this.vertices.put(v.getId(), v);
    }


    public NodesGraph() {
        edges = new HashMap<>();
        vertices = new HashMap<>();
    }
}
