package edu.route.planner.service;

import java.util.List;

public interface RouterService {

    public List<Long> findOptimalNodesPath(Long sourceId, Long destinationId, Double distance, Double duration);

}
