package edu.route.planner.service;

import edu.route.planner.model.WayEdge;

import java.util.List;

public interface WayEdgeService {
    void recalculateCache();

    WayEdge findDirect(Long sourceCityNodeId, Long destinationCityNodeId, boolean forceReload);

    List<WayEdge> findOptimal(Long sourceCityNodeId, Long destinationCityNodeId,
                              Double distanceBuffer, Double durationBuffer);

    Iterable<WayEdge> findAll();
}
