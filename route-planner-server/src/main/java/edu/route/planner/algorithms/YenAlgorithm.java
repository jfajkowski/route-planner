package edu.route.planner.algorithms;

import edu.route.planner.algorithms.Graph.Edge;
import edu.route.planner.algorithms.Graph.NodesGraph;
import edu.route.planner.algorithms.Graph.Vertex;

import java.util.ArrayList;
import java.util.List;

public class YenAlgorithm {
    private Vertex start;
    private Vertex finish;
    private NodesGraph graph;
    private List<List<Edge>> kShortestPaths = new ArrayList<>();
    private int k;

    public YenAlgorithm(int shortestPathsCount, Vertex start, Vertex finish, NodesGraph graph){
        this.start = start;
        this.finish = finish;
        this.graph = graph;
        this.k = shortestPathsCount;
    }

    public List<List<Edge>> calculate(){
        List<List<Edge>> prevShortestPaths = new ArrayList<>();
        Vertex spurVertex = null;
        List<Edge> rootPath;
        AStarAlgorithm aStar = new AStarAlgorithm(start, finish, graph);
        Vertex tempStart = new Vertex(start);
        NodesGraph tempGraph = new NodesGraph(graph);

        prevShortestPaths.add(aStar.calculate());

        while(k > 0){
            List<Edge> previousPath = prevShortestPaths.get(k - 1);

            for(int i = 0; i < previousPath.size(); ++i){

                if(spurVertex == null) spurVertex = tempStart;
                else spurVertex = tempGraph.getVertex(previousPath.get(i).getDestinationId());

                rootPath = getPathToVertex(prevShortestPaths.get(k - 1), spurVertex, tempGraph.getVertex(previousPath.get(i).getDestinationId()));

                for(List<Edge> path: prevShortestPaths){

                    List<Edge> stubPath = getPathToVertex(path, tempStart, tempGraph.getVertex(previousPath.get(i).getDestinationId()));

                    assert rootPath != null;
                    if(rootPath.equals(stubPath)){
                        Edge re = path.get(i);

                        tempGraph.removeEdge(re);

                    }
                }

                assert rootPath != null;
                for(Edge rootPathEdge: rootPath){
                    if(rootPathEdge.getStartId().equals(spurVertex.getId()))
                        tempGraph.removeVertex(rootPathEdge.getStartId());
                }

                List<Edge> spurPath = new AStarAlgorithm(spurVertex, finish, tempGraph).calculate();

                if(spurPath != null){
                    List<Edge> totalPath = new ArrayList<>(rootPath);
                    totalPath.addAll(spurPath);

                    if(!kShortestPaths.contains(totalPath))
                        kShortestPaths.add(totalPath);
                }


                tempGraph = new NodesGraph(graph);
            }
            --k;

            boolean isNewPath = false;
            while (!isNewPath){
                previousPath = kShortestPaths.get(0);
                isNewPath = true;
                if(previousPath != null){
                    for(List<Edge> p : prevShortestPaths){
                        if(p.equals(previousPath)){
                            isNewPath = false;
                            break;
                        }
                    }
                }
            }

            if(previousPath == null) break;

            kShortestPaths.add(previousPath);
        }
        return null;
    }

    private List<Edge> getPathToVertex(List<Edge> path, Vertex start, Vertex end){
        if(start.equals(end)) return null;
        List<Edge> result = new ArrayList<>();

        for(Edge edge: path){
            if(!edge.getDestinationId().equals(end.getId()))
                result.add(edge);
            else {
                result.add(edge);
                return result;
            }
        }
        throw new IllegalArgumentException("Provided vertex is not in path.");
    }

}
