package edu.route.planner.algorithms;

import edu.route.planner.algorithms.Graph.Edge;
import edu.route.planner.algorithms.Graph.NodesGraph;
import edu.route.planner.algorithms.Graph.Vertex;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AStarAlgorithmTests {
    private NodesGraph graph;
    private Vertex v1 = new Vertex(1L, 20.0);
    private Vertex v2 = new Vertex(2L, 21.0);
    private Vertex v3 = new Vertex(3L,  17.0);
    private Vertex v4 = new Vertex(4L,  16.0);
    private Vertex v5 = new Vertex(5L,  10.0);
    private Vertex v6 = new Vertex(6L,  5.0);
    private Vertex v7 = new Vertex(7L,  7.0);
    private Vertex v8 = new Vertex(8L,  0.0);
    private Vertex v9 = new Vertex(9L, 4.0);
    private Vertex v10 = new Vertex(10L,  10.0);

    private Edge e1 = new Edge(1L,  v1.getId(), v2.getId(), 6.0, 0.0);
    private Edge e101 = new Edge(2L, v2.getId(), v1.getId(), 6.0, 0.0);
    private Edge e2 = new Edge(3L, v1.getId(), v3.getId(), 7.0, 0.0);
    private Edge e102 = new Edge(4L,  v3.getId(), v1.getId(), 7.0, 0.0);
    private Edge e3 = new Edge(5L, v2.getId(), v3.getId(), 7.0, 0.0);
    private Edge e103 = new Edge(6L, v3.getId(), v2.getId(), 7.0, 0.0);
    private Edge e4 = new Edge(7L, v2.getId(), v4.getId(), 9., 0.0);
    private Edge e5 = new Edge(8L, v4.getId(), v5.getId(), 8.0, 0.0);
    private Edge e105 = new Edge(9L, v5.getId(), v4.getId(), 8.0, 0.0);
    private Edge e6 = new Edge(10L, v3.getId(), v5.getId(), 7.0, 0.0);
    private Edge e106 = new Edge(11L, v5.getId(), v3.getId(), 7.0, 0.0);
    private Edge e7 = new Edge(12L, v5.getId(), v6.getId(), 7.0, 0.0);
    private Edge e107 = new Edge(13L, v6.getId(), v5.getId(), 7.0, 0.0);
    private Edge e8 = new Edge(14L, v3.getId(), v7.getId(), 9.0, 0.0);
    private Edge e108 = new Edge(15L, v7.getId(), v3.getId(), 9.0, 0.0);
    private Edge e9 = new Edge(16L, v6.getId(), v7.getId(), 6.0, 0.0);
    private Edge e109 = new Edge(17L, v7.getId(), v6.getId(), 6.0, 0.0);
    private Edge e10 = new Edge(18L, v7.getId(), v8.getId(), 9.0, 0.0);
    private Edge e11 = new Edge(19L, v6.getId(), v8.getId(), 7.0, 0.0);
    private Edge e111 = new Edge(20L,v8.getId() , v6.getId(), 7.0, 0.0);
    private Edge e12 = new Edge(21L, v8.getId(), v9.getId(), 6.0, 0.0);
    private Edge e112 = new Edge(22L, v9.getId(), v8.getId(), 6.0, 0.0);
    private Edge e13 = new Edge(23L, v9.getId(), v6.getId(), 5.0, 0.0);
    private Edge e113 = new Edge(24L, v6.getId(), v9.getId(), 5.0, 0.0);
    private Edge e14 = new Edge(25L, v9.getId(), v10.getId(), 8.0, 0.0);
    private Edge e114 = new Edge(26L, v10.getId(), v9.getId(), 8.0, 0.0);
    private Edge e15 = new Edge(27L, v5.getId(), v10.getId(), 6.0, 0.0);
    private Edge e115 = new Edge(28L, v10.getId(), v5.getId(), 6.0, 0.0);
    private Edge e16 = new Edge(29L, v4.getId(), v10.getId(), 9.0, 0.0);
    private Edge e116 = new Edge(30L, v10.getId(), v4.getId(), 9.0, 0.0);

    @SuppressWarnings("Duplicates")
    @Before
    public void SetUpGraph(){
        graph = new NodesGraph();

        v1.addEdge(e2);
        v1.addEdge(e1);
        v2.addEdge(e101);
        v2.addEdge(e3);
        v2.addEdge(e4);
        v3.addEdge(e8);
        v3.addEdge(e102);
        v3.addEdge(e103);
        v3.addEdge(e6);
        v4.addEdge(e5);
        v4.addEdge(e16);
        v5.addEdge(e106);
        v5.addEdge(e7);
        v5.addEdge(e105);
        v5.addEdge(e15);
        v6.addEdge(e107);
        v6.addEdge(e9);
        v6.addEdge(e11);
        v6.addEdge(e113);
        v7.addEdge(e108);
        v7.addEdge(e10);
        v7.addEdge(e109);
        v8.addEdge(e12);
        v8.addEdge(e111);
        v9.addEdge(e112);
        v9.addEdge(e13);
        v9.addEdge(e14);
        v10.addEdge(e114);
        v10.addEdge(e115);
        v10.addEdge(e116);

        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addVertex(v3);
        graph.addVertex(v4);
        graph.addVertex(v5);
        graph.addVertex(v6);
        graph.addVertex(v7);
        graph.addVertex(v8);
        graph.addVertex(v9);
        graph.addVertex(v10);
    }

    @Test
    public void getShortestPathFromV1ToV8(){
        List<Edge> expected = new ArrayList<>();
        expected.add(e2);
        expected.add(e8);
        expected.add(e10);

        testShortestPath(v1, v8, expected);
    }

    @Test
    public void getShortestPathFromV1ToV9(){
        List<Edge> expected = new ArrayList<>();
        expected.add(e2);
        expected.add(e6);
        expected.add(e7);
        expected.add(e113);

        testShortestPath(v1, v9, expected);
    }

    @Test
    public void getShortestPathFromV9ToV1(){
        List<Edge> expected = new ArrayList<>();
        expected.add(e13);
        expected.add(e107);
        expected.add(e106);
        expected.add(e102);

        testShortestPath(v9, v1, expected);
    }

    @Test
    public void getShortestPathFromV1ToV4(){
        List<Edge> expected = new ArrayList<>();
        expected.add(e1);
        expected.add(e4);

        testShortestPath(v1, v4, expected);
    }

    @Test
    public void getShortestPathFromV4ToV1(){
        List<Edge> expected = new ArrayList<>();
        expected.add(e5);
        expected.add(e106);
        expected.add(e102);

        testShortestPath(v4, v1, expected);
    }


    private void testShortestPath(Vertex start, Vertex finish, List<Edge> expectedPath){
        AStarAlgorithm algorithm = new AStarAlgorithm(start, finish, graph);

        List<Edge> path = algorithm.calculate();

        Assert.assertArrayEquals(expectedPath.toArray(), path.toArray());
    }
}
