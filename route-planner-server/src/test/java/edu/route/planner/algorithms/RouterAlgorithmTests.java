package edu.route.planner.algorithms;

import edu.route.planner.algorithms.Graph.Edge;
import edu.route.planner.algorithms.Graph.GraphBuilder;
import edu.route.planner.algorithms.Graph.NodesGraph;
import edu.route.planner.algorithms.Graph.Vertex;
import edu.route.planner.dao.CityNodeRepository;
import edu.route.planner.dao.WayEdgeRepository;
import edu.route.planner.model.CityNode;
import edu.route.planner.model.WayEdge;
import edu.route.planner.service.WayEdgeService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RouterAlgorithmTests {

    @Autowired
    private CityNodeRepository cityNodeRepository;

    @Autowired
    private WayEdgeRepository wayEdgeRepository;

    @Autowired
    private WayEdgeService wayEdgeService;

    @Test
    public void generalTest(){
        List<CityNode> cityNodes = new ArrayList<>();
        cityNodeRepository.findAll().forEach(cityNodes::add);

        List<WayEdge> wayEdges = new ArrayList<>();
        wayEdgeRepository.findAll().forEach(wayEdges::add);

        GraphBuilder gb = new GraphBuilder(wayEdgeService);
        Long sourceId = cityNodes.get(0).getId();
        Long destinationId = cityNodes.get(cityNodes.size()/2).getId();
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
        List<Edge> path = ra.calculateRoute();

        Assert.assertTrue(path.size() > 3);
    }
}
