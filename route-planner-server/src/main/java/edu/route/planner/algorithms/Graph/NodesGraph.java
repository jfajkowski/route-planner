package edu.route.planner.algorithms.Graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NodesGraph {
    private Map<Long, Edge> edges;
    private Map<Long, Vertex> vertices;

    public void addEdge(Edge edge){
        if(!edges.containsKey(edge.getId()))
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

    public Vertex getVertex(Long Id){
        return vertices.get(Id);
    }

    public void removeVertex(Long vertexId){
        if(!vertices.containsKey(vertexId)) return;

        removeConnectionsWithVertex(vertexId);
        vertices.remove(vertexId);
    }

    public Map<Long, Vertex> getVertices() {
        return vertices;
    }

    public void addVertex(Vertex v){
        if(!vertices.containsKey(v.getId()))
            this.vertices.put(v.getId(), v);
    }

    public NodesGraph() {
        edges = new HashMap<Long, Edge>();
        vertices = new HashMap<Long, Vertex>();
    }

    public NodesGraph(NodesGraph graph){
        this();
        graph.edges.values().forEach(e -> edges.put(e.getId(), new Edge(e)));
        graph.vertices.values().forEach(v -> vertices.put(v.getId(), new Vertex(v)));
    }

    private void removeConnectionsWithVertex(Long vertexId){
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
