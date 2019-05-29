package edu.route.planner.service;

import edu.route.planner.algorithms.Graph.GraphBuilder;
import edu.route.planner.algorithms.Graph.NodesGraph;
import edu.route.planner.algorithms.Graph.Vertex;
import edu.route.planner.algorithms.RouterAlgorithm;
import edu.route.planner.dao.CityNodeRepository;
import edu.route.planner.dao.WayEdgeRepository;
import edu.route.planner.model.CityNode;
import edu.route.planner.model.WayEdge;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class RouterServiceImpl implements RouterService{
    @Autowired
    private CityNodeRepository cityNodeRepository;

    @Autowired
    private WayEdgeRepository wayEdgeRepository;

    @Autowired
    private WayEdgeService wayEdgeService;

    public RouterServiceImpl(CityNodeRepository cityNodeRepository, WayEdgeRepository wayEdgeRepository, WayEdgeService wayEdgeService){
        this.cityNodeRepository = cityNodeRepository;
        this.wayEdgeRepository = wayEdgeRepository;
        this.wayEdgeService = wayEdgeService;
    }

    @Override
    public List<Long> findOptimalNodesPath(Long sourceId, Long destinationId, Double distance, Double duration) {
        List<CityNode> cityNodes = new ArrayList<>();
        cityNodeRepository.findAll().forEach(cityNodes::add);

        List<WayEdge> wayEdges = new ArrayList<>();
        wayEdgeRepository.findAll().forEach(wayEdges::add);

        GraphBuilder gb = new GraphBuilder(wayEdgeService);
        NodesGraph graph = gb.loadEdges(wayEdges, cityNodes, destinationId);
        GraphBuilder reversedGb = new GraphBuilder(wayEdgeService);
        NodesGraph reversedGraph = reversedGb.loadEdges(wayEdges, cityNodes, sourceId);

        Vertex source = graph.getVertex(sourceId);
        Vertex destination = graph.getVertex(destinationId);

        RouterAlgorithm ra = new RouterAlgorithm(
                source,
                destination,
                graph,
                reversedGraph,
                1000 * 1000.0, //1000km
                10 * 60 * 60.0 //10h
        );

        return ra.calculateRoute();
    }
}
