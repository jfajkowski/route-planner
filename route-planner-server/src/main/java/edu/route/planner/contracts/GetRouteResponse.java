package edu.route.planner.contracts;

import edu.route.planner.model.WayEdge;
import java.util.List;

public class GetRouteResponse {
    public double distance;
    public double duration;
    public Iterable<WayEdge> route;

    public GetRouteResponse(double distance, double duration, List<WayEdge> route){
        this.distance = distance;
        this.duration = duration;
        this.route = route;
    }
}
