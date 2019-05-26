package edu.route.planner.algorithms.Graph;

public class Edge {
    private Long Id;
    private Vertex destination;
    private Double distance;

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public Vertex getDestination() {
        return destination;
    }

    public void setDestination(Vertex destination) {
        this.destination = destination;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Edge(Long Id, Vertex destination, Double distance){
        this.Id = Id;
        this.destination = destination;
        this.distance = distance;
    }

    @Override
    public boolean equals(Object obj){
        if(this == obj) return true;
        if(obj == null) return false;
        if(getClass() != obj.getClass()) return false;

        Edge edge = (Edge)obj;
        return (destination == edge.destination && distance.equals(edge.distance));
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + Id.hashCode();
        result = 31 * result + distance.hashCode();
        return 31 * result + destination.hashCode();
    }
}
