package edu.route.planner.service;

import edu.route.planner.contracts.GetRouteResponse;
import edu.route.planner.model.WayEdge;

public interface WayEdgeService {
    void recalculateCache();

    WayEdge findDirect(Long sourceCityNodeId, Long destinationCityNodeId);

    GetRouteResponse findOptimalBruteForce(Long sourceCityNodeId, Long destinationCityNodeId,
                                           Double distanceBuffer, Double durationBuffer);

    GetRouteResponse findOptimalCustom(Long sourceCityNodeId, Long destinationCityNodeId,
                                       Double distanceBuffer, Double durationBuffer);


    GetRouteResponse findOptimalSimulatedAnnealing(Long sourceCityNodeId, Long destinationCityNodeId,
                                                   Double distanceBuffer, Double durationBuffer);

    Iterable<WayEdge> findAll();
}
