package edu.route.planner.controller;

import edu.route.planner.model.WayEdge;
import edu.route.planner.service.RouterService;
import edu.route.planner.service.WayEdgeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WayEdgeController {

    private final WayEdgeService wayEdgeService;

    private final RouterService routerService;

    public WayEdgeController(WayEdgeService wayEdgeService, RouterService routerService) {
        this.wayEdgeService = wayEdgeService;
        this.routerService = routerService;
    }

    @GetMapping("wayEdges/all")
    public Iterable<WayEdge> findAll() {
        return wayEdgeService.findAll();
    }

    @GetMapping("wayEdges/direct")
    public WayEdge findDirect(
            @RequestParam("source") Long sourceCityNodeId,
            @RequestParam("destination") Long destinationCityNodeId) {
        return wayEdgeService.findDirect(sourceCityNodeId, destinationCityNodeId, false);
    }

    @GetMapping("wayEdges/optimal")
    public Iterable<WayEdge> findOptimal(
            @RequestParam("source") Long sourceCityNodeId,
            @RequestParam("destination") Long destinationCityNodeId,
            @RequestParam("distanceBuffer") Double distanceBuffer,
            @RequestParam("durationBuffer") Double durationBuffer) {
        return wayEdgeService.findOptimal(sourceCityNodeId, destinationCityNodeId, distanceBuffer, durationBuffer);
    }

    @GetMapping("wayEdges/optimal/nodes")
    public Iterable<Long> findOptimalNodesPath(
            @RequestParam("source") Long sourceCityNodeId,
            @RequestParam("destination") Long destinationCityNodeId,
            @RequestParam("distanceBuffer") Double distanceBuffer,
            @RequestParam("durationBuffer") Double durationBuffer) {
        return routerService.findOptimalNodesPath(sourceCityNodeId, destinationCityNodeId, distanceBuffer, durationBuffer);
    }
}
