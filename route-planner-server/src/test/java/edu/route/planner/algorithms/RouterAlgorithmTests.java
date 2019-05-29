package edu.route.planner.algorithms;

import edu.route.planner.algorithms.Graph.GraphBuilder;
import edu.route.planner.algorithms.Graph.NodesGraph;
import edu.route.planner.algorithms.Graph.Vertex;
import edu.route.planner.dao.CityNodeRepository;
import edu.route.planner.dao.WayEdgeRepository;
import edu.route.planner.model.CityNode;
import edu.route.planner.model.WayEdge;
import edu.route.planner.service.WayEdgeService;
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

        CityNode source = cityNodes.get(0);
        CityNode destination = cityNodes.get(cityNodes.size()/2);
        GraphBuilder gb = new GraphBuilder(wayEdgeService);
        NodesGraph graph = gb.loadEdges(wayEdges, cityNodes, destination);

        RouterAlgorithm ra = new RouterAlgorithm(
                gb.convertToVertex(source, destination),
                gb.convertToVertex(destination, destination),
                graph,
                100 * 1000.0,
                60 * 60.0
        );
        List<Long> path = ra.calculateRoute();
    }
}
