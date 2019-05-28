package edu.route.planner.algorithms;

import edu.route.planner.algorithms.Graph.Edge;
import edu.route.planner.algorithms.Graph.NodesGraph;
import edu.route.planner.algorithms.Graph.Path;
import edu.route.planner.algorithms.Graph.Vertex;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.PriorityQueue;

public class YenAlgorithm {
    private Vertex start;
    private Vertex finish;
    private NodesGraph graph;
    private List<List<Edge>> kShortestPaths = new ArrayList<>();
    private int K;

    public YenAlgorithm(int shortestPathsCount, Vertex start, Vertex finish, NodesGraph graph){
        this.start = start;
        this.finish = finish;
        this.graph = graph;
        this.K = shortestPathsCount;
    }

    public List<List<Edge>> calculate(){
        Vertex spurVertex;
        List<Edge> rootPath;
        PriorityQueue<Path> candidates = new PriorityQueue<>();
        AStarAlgorithm aStar = new AStarAlgorithm(start, finish, graph);
        NodesGraph tempGraph = new NodesGraph(graph);

        List<Edge> shortestPath = aStar.calculate();
        kShortestPaths.add(shortestPath);

        for(int k = 1; k < K; k++){
            List<Edge> previousPath = kShortestPaths.get(k - 1);

            for(int i = 0; i < previousPath.size(); ++i){

                spurVertex = tempGraph.getVertex(previousPath.get(i).getStartId());

                rootPath = getPathToVertex(previousPath, previousPath.get(0).getStartId(), spurVertex.getId());

                for(List<Edge> path: kShortestPaths){

                    List<Edge> stubPath = getPathToVertex(path, path.get(0).getStartId(), spurVertex.getId());

                    if(stubPath.equals(rootPath)){
                        Edge re = path.get(i);

                        tempGraph.removeEdge(re);
                    }
                }

                assert rootPath != null;
                for(Edge rootPathEdge: rootPath){
                    if(!rootPathEdge.getStartId().equals(spurVertex.getId()))
                        tempGraph.removeVertex(rootPathEdge.getStartId());
                }

                List<Edge> spurPath = new AStarAlgorithm(spurVertex, finish, tempGraph).calculate();

                if(spurPath != null){
                    Path totalPath = new Path(rootPath);
                    totalPath.addPath(spurPath);

                    if(!candidates.contains(totalPath))
                        candidates.add(totalPath);
                }

                tempGraph = new NodesGraph(graph);
            }

            boolean isNewPath = false;
            while (!isNewPath){
                Path sp = candidates.poll();
                if(sp != null) shortestPath = sp.edges;
                else shortestPath = null;

                isNewPath = true;
                if(shortestPath != null){
                    for(List<Edge> p : kShortestPaths){
                        if(p.equals(shortestPath)){
                            isNewPath = false;
                            break;
                        }
                    }
                }
            }

            if(shortestPath == null) break;

            kShortestPaths.add(shortestPath);
        }
        return kShortestPaths;
    }

    private List<Edge> getPathToVertex(List<Edge> path, String startId, String endId){
        List<Edge> result = new ArrayList<>();
        if(startId.equals(endId)){
            return result;
        }

        for(Edge edge: path){
            if(!edge.getDestinationId().equals(endId))
                result.add(edge);
            else {
                result.add(edge);
                return result;
            }
        }
        throw new IllegalArgumentException("Provided vertex is not in path.");
    }

}
