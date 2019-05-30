package edu.route.planner.service;

import edu.route.planner.model.WayEdge;

import java.util.List;

public interface WayEdgeService {
    void recalculateCache();

    WayEdge findDirect(Long sourceCityNodeId, Long destinationCityNodeId);

    List<WayEdge> findOptimalBruteForce(Long sourceCityNodeId, Long destinationCityNodeId,
                                        Double distanceBuffer, Double durationBuffer);

    List<WayEdge> findOptimalCustom(Long sourceCityNodeId, Long destinationCityNodeId,
                                    Double distanceBuffer, Double durationBuffer);

    Iterable<WayEdge> findAll();
}
