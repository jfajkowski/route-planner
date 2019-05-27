package edu.route.planner.algorithms.Graph;

public class Edge {
    private Long Id;
    private Long startId;
    private Long destinationId;
    private Double distance;

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public Long getDestination() {
        return destinationId;
    }

    public void setDestination(Long destinationId) {
        this.destinationId = destinationId;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Edge(Long Id, Long startId, Long destinationId, Double distance){
        this.Id = Id;
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
