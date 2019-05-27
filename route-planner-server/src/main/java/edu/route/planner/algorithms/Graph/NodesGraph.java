package edu.route.planner.algorithms.Graph;

import java.util.HashMap;
import java.util.Map;

public class NodesGraph {
    private Map<String, Edge> edges;
    private Map<String, Vertex> vertices;

    /**
     *
     * @param start
     * @param destination
     * @param edge
     */
    public void addEdge(Vertex start, Vertex destination, Edge edge){
        if(start.getId().equals(edge.getDestinationId())) throw new IllegalArgumentException("Edge cannot start and end up in the same vertex.");
        if(edges.containsKey(edge.getId())) return;

        edges.put(edge.getId(),edge);
        start.addEdge(edge);
        vertices.putIfAbsent(destination.getId(), destination);
        vertices.putIfAbsent(start.getId(), start);
    }

    public void removeEdge(Edge edge){
        if(!vertices.containsKey(edge.getStartId())) throw new IllegalArgumentException("Start Vertex of ID: " + edge.getStartId() + " does not contain in this graph/");
        if(!vertices.containsKey(edge.getDestinationId())) throw new IllegalArgumentException("Destination Vertex of ID: " + edge.getDestinationId() + " does not contain in this graph/");

        vertices.get(edge.getStartId()).removeEdge(edge.getId());
        edges.remove(edge.getId());
    }

    public Edge getEdge(String Id){
        return edges.get(Id);
    }

    public Vertex getVertex(String Id){
        return vertices.get(Id);
    }

    public void removeVertex(String Id){
        if(!vertices.containsKey(Id)) return;

        removeEdgesToVertex(Id);
        vertices.remove(Id);
    }

    public Map<String, Edge> getEdges() {
        return edges;
    }

    public void setEdges(Map<String, Edge> edges) {
        this.edges = edges;
    }

    public Map<String, Vertex> getVertices() {
        return vertices;
    }

    public void setVertices(Map<String, Vertex> vertices) {
        this.vertices = vertices;
    }

    public void addVertex(Vertex v){
        this.vertices.put(v.getId(), v);
    }

    public NodesGraph() {
        edges = new HashMap<>();
        vertices = new HashMap<>();
    }

    public NodesGraph(NodesGraph graph){
        this();
        edges.putAll(graph.edges);
        vertices.putAll(graph.vertices);
    }

    private void removeEdgesToVertex(String Id){
        for(Edge edgeToRemove: edges.values()){
            for (Vertex v: vertices.values()){
                v.removeEdge(edgeToRemove.getId());
            }
        }
        edges.remove(Id);
    }
}
