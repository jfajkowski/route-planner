package edu.route.planner.algorithms.Graph;

import edu.route.planner.model.CityNode;
import edu.route.planner.model.WayEdge;
import edu.route.planner.service.WayEdgeService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

public class GraphBuilder {

    @Autowired
    private static WayEdgeService wayEdgeService;

    private static NodesGraph graph = new NodesGraph();

    public GraphBuilder(WayEdgeService wayEdgeService){
        GraphBuilder.wayEdgeService = wayEdgeService;
    }

    public NodesGraph loadEdges( Collection<WayEdge> wayEdges, Collection<CityNode> nodes, CityNode destination){
        for(CityNode cn: nodes){
            Vertex v = new Vertex(cn.getId(), wayEdgeService.findDirect(cn.getId(), destination.getId()).getDistance());
            for (WayEdge we: wayEdges){
                Edge e = new Edge(we.getId(), we.getSourceCityNodeId(), we.getDestinationCityNodeId(), we.getDistance(), we.getDuration());
                if(v.getId().equals(e.getStartId()))
                    v.addEdge(e);

                graph.addEdge(e);
            }
            graph.addVertex(v);
        }

        return graph;
    }

    public Vertex convertToVertex(CityNode cityNode, CityNode destination){
        return new Vertex(cityNode.getId(), wayEdgeService.findDirect(cityNode.getId(), destination.getId()).getDistance());
    }
}
