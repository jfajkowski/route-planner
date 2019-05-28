package edu.route.planner.algorithms.Graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NodesGraph {
    private Map<String, Edge> edges;
    private Map<String, Vertex> vertices;

    public void addEdge(Vertex start, Vertex destination, Edge edge){
        if(start.getId().equals(edge.getDestinationId())) throw new IllegalArgumentException("Edge cannot start and end up in the same vertex.");
        if(edges.containsKey(edge.getId())) return;

        edges.put(edge.getId(),edge);
        start.addEdge(edge);
        vertices.putIfAbsent(destination.getId(), destination);
        vertices.putIfAbsent(start.getId(), start);
    }

    public void addEdge(Edge edge){
        if(edge.getStartId().equals("") || edge.getDestinationId().equals("")) throw new IllegalArgumentException("Edges must have start and end vertex ID.");

        edges.put(edge.getId(), edge);
    }

    public void removeEdge(Edge edge){
        if(!vertices.containsKey(edge.getStartId())) throw new IllegalArgumentException("Start Vertex of ID: " + edge.getStartId() + " does not contain in this graph/");
        if(!vertices.containsKey(edge.getDestinationId())) throw new IllegalArgumentException("Destination Vertex of ID: " + edge.getDestinationId() + " does not contain in this graph/");

        edges.remove(edge.getId());
        for(Vertex v: vertices.values()){
            if(v.getEdges().containsKey(edge.getId()))
                v.removeEdge(edge.getId());
        }
    }

    public Edge getEdge(String Id){
        return edges.get(Id);
    }

    public Vertex getVertex(String Id){
        return vertices.get(Id);
    }

    public void removeVertex(String vertexId){
        if(!vertices.containsKey(vertexId)) return;

        removeConnectionsWithVertex(vertexId);
        vertices.remove(vertexId);
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

    private void removeConnectionsWithVertex(String vertexId){
        List<Edge> edgesToRemove = new ArrayList<>();
        for(Edge edge: edges.values()){
            if(edge.getStartId().equals(vertexId) || edge.getDestinationId().equals(vertexId)) {
                edgesToRemove.add(edge);
                for (Vertex v : vertices.values()) {
                    v.removeEdge(edge.getId());
                }
            }
        }
        for(Edge e: edgesToRemove)
            edges.remove(e.getId());
    }
}
