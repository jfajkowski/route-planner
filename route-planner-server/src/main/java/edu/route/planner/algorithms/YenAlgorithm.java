package edu.route.planner.algorithms;

import com.sun.scenario.effect.Identity;
import edu.route.planner.algorithms.Graph.Edge;
import edu.route.planner.algorithms.Graph.NodesGraph;
import edu.route.planner.algorithms.Graph.Vertex;
import org.hibernate.id.GUIDGenerator;

import java.util.ArrayList;
import java.util.List;

public class YenAlgorithm {
    private Vertex start;
    private Vertex finish;
    private NodesGraph graph;
    private List<List<Edge>> prevShortestPaths = new ArrayList<>();
    private List<List<Edge>> kShortestPaths = new ArrayList<>();
    private int k;

    public YenAlgorithm(int shortestPathsCount, Vertex start, Vertex finish, NodesGraph graph){
        this.start = start;
        this.finish = finish;
        this.graph = graph;
        this.k = shortestPathsCount;
    }

    public List<List<Edge>> calculate(){
        AStarAlgorithm aStar = new AStarAlgorithm(start, finish, graph);

        Vertex tempStart = new Vertex(start);
        prevShortestPaths.add(aStar.calculate());
        Vertex spurVertex = null;
        List<Edge> rootPath;

        while(k > 0){
            List<Edge> previousPath = prevShortestPaths.get(k - 1);

            for(int i = 0; i < previousPath.size(); ++i){
//            for(Edge edge: prevShortestPaths.get(k - 1)){
//                List<Edge> removedEdges = new ArrayList<>();

                if(spurVertex == null) spurVertex = tempStart;
                else spurVertex = previousPath.get(i).getDestination();

                rootPath = getPathToVertex(prevShortestPaths.get(k - 1), spurVertex, previousPath.get(i).getDestination());

                for(List<Edge> path: prevShortestPaths){

                    List<Edge> stubPath = getPathToVertex(path, tempStart, previousPath.get(i).getDestination());

                    assert rootPath != null;
                    if(rootPath.equals(stubPath)){
                        Edge re = path.get(i);

                        if(i == 0) graph.removeEdge(tempStart, re);
                        else graph.removeEdge(path.get(i - 1).getDestination(), re);
//                        removedEdges.add(re);
                    }
                }

                for(Edge rootPathEdge: rootPath){

                }
            }

            --k;
        }
        return null;
    }

    private List<Edge> getPathToVertex(List<Edge> path, Vertex start, Vertex end){
        if(start.equals(end)) return null;
        List<Edge> result = new ArrayList<>();

        for(Edge edge: path){
            if(!edge.getDestination().equals(end))
                result.add(edge);
            else return result;
        }
        throw new IllegalArgumentException("Provided vertex is not in path.");
    }

}
