package edu.route.planner.algorithms;

import edu.route.planner.algorithms.Graph.Edge;
import edu.route.planner.algorithms.Graph.NodesGraph;
import edu.route.planner.algorithms.Graph.Vertex;

import java.util.*;

class AStarAlgorithm {
    private Vertex start;
    private Vertex destination;
    private NodesGraph graph;

    private List<Edge> resultPath = new ArrayList<Edge>();
    private Set<Vertex> closedVertices = new HashSet<Vertex>();
    private List<Vertex> openVertices = new ArrayList<Vertex>();
    Map<Vertex, Vertex> cameFrom = new HashMap<Vertex, Vertex>();
    Map<Vertex, Double> distanceFromStart = new HashMap<Vertex, Double>();
    Map<Vertex, Double> distanceThroughY = new HashMap<Vertex, Double>();

    private final Comparator<Vertex> comparator = new Comparator<Vertex>() {
        @Override
        public int compare(Vertex o1, Vertex o2) {
            if(distanceThroughY.get(o1) < distanceThroughY.get(o2))
                return -1;
            if(distanceThroughY.get(o1) > distanceThroughY.get(o2))
                return 1;
            return 0;
        }
    };

    public AStarAlgorithm(Vertex start, Vertex destination, NodesGraph nodesGraph){
        this.start = start;
        this.destination = destination;
        this.graph = nodesGraph;
    }

    public List<Edge> calculate(){

        for(Vertex v: graph.getVertices().values()){
            distanceThroughY.put(v, Double.MAX_VALUE);
        }
        distanceThroughY.remove(start);
        distanceThroughY.put(start, start.getHeuristic());
        start.setDistanceFromStart(0.0);
        openVertices.add(start);
        distanceFromStart.put(start, 0.0);

        while(!openVertices.isEmpty()){
//            Vertex current = getWithLowestCost();
            Vertex current = openVertices.get(0);
            if(current.equals(destination))
                return reconstructPath(cameFrom, destination);

            openVertices.remove(0);
            closedVertices.add(current);
            for(Edge edge: current.getEdges()){
                final Vertex neighbour = edge.getDestination();
                if(closedVertices.contains(neighbour))
                    continue;

                final double tentativeDistanceFromStart = distanceFromStart.get(current) + distanceBetween(current, neighbour);
                if(!openVertices.contains(neighbour))
                    openVertices.add(neighbour);
                else if(tentativeDistanceFromStart >= distanceFromStart.get(neighbour))
                    continue;

                cameFrom.put(neighbour, current);
                distanceFromStart.put(neighbour, tentativeDistanceFromStart);
                final Double estimatedDistanceThroughY = distanceFromStart.get(neighbour) + neighbour.getHeuristic();
                distanceThroughY.put(neighbour, estimatedDistanceThroughY);

                Collections.sort(openVertices, comparator);
            }
        }

        return null;
    }

    private Double distanceBetween(Vertex A, Vertex B){
        for(Edge e: A.getEdges()){
            if(e.getDestination().equals(B)){
                return e.getDistance();
            }
        }
        return Double.MAX_VALUE;
    }

    private List<Edge> reconstructPath(Map<Vertex, Vertex> cameFrom, Vertex current){
        final List<Edge> path = new ArrayList<Edge>();

        while (current != null){
            final Vertex previous = current;
            current = cameFrom.get(current);
            if(current != null){
                final Edge edge = current.getEdge(previous);
                path.add(edge);
            }
        }

        Collections.reverse(path);
        return path;
    }
}
