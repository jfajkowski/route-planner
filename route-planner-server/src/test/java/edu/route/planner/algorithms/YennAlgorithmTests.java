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

import java.util.List;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
public class YennAlgorithmTests {
    private NodesGraph graph;
    private Vertex v1 = new Vertex(UUID.randomUUID().toString(), 20.0);
    private Vertex v2 = new Vertex(UUID.randomUUID().toString(), 21.0);
    private Vertex v3 = new Vertex(UUID.randomUUID().toString(),  17.0);
    private Vertex v4 = new Vertex(UUID.randomUUID().toString(),  16.0);
    private Vertex v5 = new Vertex(UUID.randomUUID().toString(),  10.0);
    private Vertex v6 = new Vertex(UUID.randomUUID().toString(),  5.0);
    private Vertex v7 = new Vertex(UUID.randomUUID().toString(),  7.0);
    private Vertex v8 = new Vertex(UUID.randomUUID().toString(),  0.0);
    private Vertex v9 = new Vertex(UUID.randomUUID().toString(), 4.0);
    private Vertex v10 = new Vertex(UUID.randomUUID().toString(),  10.0);

    private Edge e1 = new Edge(UUID.randomUUID(),  v1.getId(), v2.getId(), 6.0);
    private Edge e101 = new Edge(UUID.randomUUID(), v2.getId(), v1.getId(), 6.0);
    private Edge e2 = new Edge(UUID.randomUUID(), v1.getId(), v3.getId(), 7.0);
    private Edge e102 = new Edge(UUID.randomUUID(),  v3.getId(), v1.getId(), 7.0);
    private Edge e3 = new Edge(UUID.randomUUID(), v2.getId(), v3.getId(), 7.0);
    private Edge e103 = new Edge(UUID.randomUUID(), v3.getId(), v2.getId(), 7.0);
    private Edge e4 = new Edge(UUID.randomUUID(), v2.getId(), v4.getId(), 9.0);
    private Edge e5 = new Edge(UUID.randomUUID(), v4.getId(), v5.getId(), 8.0);
    private Edge e105 = new Edge(UUID.randomUUID(), v5.getId(), v4.getId(), 8.0);
    private Edge e6 = new Edge(UUID.randomUUID(), v3.getId(), v5.getId(), 7.0);
    private Edge e106 = new Edge(UUID.randomUUID(), v5.getId(), v3.getId(), 7.0);
    private Edge e7 = new Edge(UUID.randomUUID(), v5.getId(), v6.getId(), 7.0);
    private Edge e107 = new Edge(UUID.randomUUID(), v6.getId(), v5.getId(), 7.0);
    private Edge e8 = new Edge(UUID.randomUUID(), v3.getId(), v7.getId(), 9.0);
    private Edge e108 = new Edge(UUID.randomUUID(), v7.getId(), v3.getId(), 9.0);
    private Edge e9 = new Edge(UUID.randomUUID(), v6.getId(), v7.getId(), 6.0);
    private Edge e109 = new Edge(UUID.randomUUID(), v7.getId(), v6.getId(), 6.0);
    private Edge e10 = new Edge(UUID.randomUUID(), v7.getId(), v8.getId(), 9.0);
    private Edge e11 = new Edge(UUID.randomUUID(), v6.getId(), v8.getId(), 7.0);
    private Edge e111 = new Edge(UUID.randomUUID(),v8.getId() , v6.getId(), 7.0);
    private Edge e12 = new Edge(UUID.randomUUID(), v8.getId(), v9.getId(), 6.0);
    private Edge e112 = new Edge(UUID.randomUUID(), v9.getId(), v8.getId(), 6.0);
    private Edge e13 = new Edge(UUID.randomUUID(), v9.getId(), v6.getId(), 5.0);
    private Edge e113 = new Edge(UUID.randomUUID(), v6.getId(), v9.getId(), 5.0);
    private Edge e14 = new Edge(UUID.randomUUID(), v9.getId(), v10.getId(), 8.0);
    private Edge e114 = new Edge(UUID.randomUUID(), v10.getId(), v9.getId(), 8.0);
    private Edge e15 = new Edge(UUID.randomUUID(), v5.getId(), v10.getId(), 6.0);
    private Edge e115 = new Edge(UUID.randomUUID(), v10.getId(), v5.getId(), 6.0);
    private Edge e16 = new Edge(UUID.randomUUID(), v4.getId(), v10.getId(), 9.0);
    private Edge e116 = new Edge(UUID.randomUUID(), v10.getId(), v4.getId(), 9.0);

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
    public void getShortestPathWhenKIsOne(){
        List<List<Edge>> kYenPath = new YenAlgorithm(1, v1, v10, graph).calculate();
        List<Edge> shortestPath = new AStarAlgorithm(v1, v10, graph).calculate();

        Assert.assertEquals(1, kYenPath.size());
        Assert.assertArrayEquals(shortestPath.toArray(), kYenPath.get(0).toArray());
    }

}
