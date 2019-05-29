package edu.route.planner.algorithms.Graph;

import java.util.List;
import java.util.UUID;

public class Edge {
    private Long Id;
    private Long startId;
    private Long destinationId;
    private Double distance;
    private Double duration;

    public Long getId() {
        return Id;
    }

    public Long getDestinationId() {
        return destinationId;
    }

    public Long getStartId() {
        return startId;
    }

    public Double getDistance() {
        return distance;
    }

    public Double getDuration() { return duration; }

    public Edge(Long Id, Long startId, Long destinationId, Double distance, Double duration){
        this.Id = Id;
        this.startId = startId;
        this.destinationId = destinationId;
        this.distance = distance;
        this.duration = duration;
    }

    public Edge(Edge edge){
        this(edge.Id, edge.startId, edge.destinationId, edge.distance, edge.duration);
    }

    @Override
    public boolean equals(Object obj){
        if(this == obj) return true;
        if(obj == null) return false;
        if(getClass() != obj.getClass()) return false;

        Edge edge = (Edge)obj;
        return (destinationId.equals(edge.destinationId) && distance.equals(edge.distance));
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + Id.hashCode() + distance.hashCode() + destinationId.hashCode() + startId.hashCode();
        return 31 * result;
    }
}
