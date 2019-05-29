package edu.route.planner.algorithms;

import edu.route.planner.algorithms.Graph.Edge;
import edu.route.planner.algorithms.Graph.NodesGraph;
import edu.route.planner.algorithms.Graph.Path;
import edu.route.planner.algorithms.Graph.Vertex;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

class YenAlgorithm {
    private Vertex start;
    private Vertex finish;
    private NodesGraph graph;
    private List<List<Edge>> kShortestPaths = new ArrayList<>();
    private Double maxDistance;
    private Double maxDuration;
    private final int K = 40;

    YenAlgorithm(Double maxDistance, Double maxDuration, Vertex start, Vertex finish, NodesGraph graph){
        this.start = start;
        this.finish = finish;
        this.graph = graph;
        this.maxDistance = maxDistance;
        this.maxDuration = maxDuration;
    }

    List<List<Edge>> calculate(){
        Vertex spurVertex;
        List<Edge> rootPath;
        PriorityQueue<Path> candidates = new PriorityQueue<>();
        AStarAlgorithm aStar = new AStarAlgorithm(start, finish, graph);
        NodesGraph tempGraph = new NodesGraph(graph);

        List<Edge> shortestPath = aStar.calculate();
//        if(shortestPath == null) throw new IllegalArgumentException("Cannot find path in provided graph.");
        if(shortestPath == null) return kShortestPaths;
        kShortestPaths.add(shortestPath);

        for(int k = 1; k < K; k++){
            List<Edge> previousPath = kShortestPaths.get(k - 1);

            for(int i = 0; i < previousPath.size(); ++i){

                spurVertex = tempGraph.getVertex(previousPath.get(i).getStartId());

                rootPath = getPathToVertex(previousPath, previousPath.get(0).getStartId(), spurVertex.getId());

                for(List<Edge> path: kShortestPaths){

                    List<Edge> stubPath;
                    if(path.size() <= i) stubPath = path;
                    else stubPath = getPathToVertex(path, path.get(0).getStartId(), path.get(i).getStartId());

                    if(stubPath.equals(rootPath)){
                        Edge re;
                        if(path.size() <= i) continue;
                        else re = path.get(i);

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

            Double pathDist = Path.calculatePathDistance(shortestPath);
            Double pathDur = Path.calculatePathDuration(shortestPath);
            if( pathDist > maxDistance || pathDur > maxDuration)
                break;

            kShortestPaths.add(shortestPath);
        }
        return kShortestPaths;
    }

    private List<Edge> getPathToVertex(List<Edge> path, Long startId, Long endId){
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
