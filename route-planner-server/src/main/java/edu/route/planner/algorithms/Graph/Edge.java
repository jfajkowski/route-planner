package edu.route.planner.algorithms.Graph;

import java.util.UUID;

public class Edge {
    private String Id;
    private String startId;
    private String destinationId;
    private Double distance;

    public String getId() {
        return Id;
    }

    public void setId(UUID id) {
        Id = id.toString();
    }

    public String getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(String destinationId) {
        this.destinationId = destinationId;
    }

    public String getStartId() {
        return startId;
    }

    public void setStartId(String startId) {
        this.startId = startId;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Edge(UUID Id, String startId, String destinationId, Double distance){
        this.Id = Id.toString();
        this.startId = startId;
        this.destinationId = destinationId;
        this.distance = distance;
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
