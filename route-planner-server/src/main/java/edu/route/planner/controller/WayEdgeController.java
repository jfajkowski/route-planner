package edu.route.planner.controller;

import edu.route.planner.contracts.GetRouteResponse;
import edu.route.planner.model.WayEdge;
import edu.route.planner.service.WayEdgeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WayEdgeController {

    private final WayEdgeService wayEdgeService;

    public WayEdgeController(WayEdgeService wayEdgeService) {
        this.wayEdgeService = wayEdgeService;
    }

    @GetMapping("wayEdges/all")
    public Iterable<WayEdge> findAll() {
        return wayEdgeService.findAll();
    }

    @GetMapping("wayEdges/direct")
    public WayEdge findDirect(
            @RequestParam("source") Long sourceCityNodeId,
            @RequestParam("destination") Long destinationCityNodeId) {
        return wayEdgeService.findDirect(sourceCityNodeId, destinationCityNodeId);
    }

    @GetMapping("wayEdges/optimal/bruteforce")
    public GetRouteResponse findOptimalBruteForce(
            @RequestParam("source") Long sourceCityNodeId,
            @RequestParam("destination") Long destinationCityNodeId,
            @RequestParam("distanceBuffer") Double distanceBuffer,
            @RequestParam("durationBuffer") Double durationBuffer) {
        return wayEdgeService.findOptimalBruteForce(sourceCityNodeId, destinationCityNodeId, distanceBuffer, durationBuffer);
    }

    @GetMapping("wayEdges/optimal/router")
    public GetRouteResponse findOptimalRouter(
            @RequestParam("source") Long sourceCityNodeId,
            @RequestParam("destination") Long destinationCityNodeId,
            @RequestParam("distanceBuffer") Double distanceBuffer,
            @RequestParam("durationBuffer") Double durationBuffer) {
        return wayEdgeService.findOptimalCustom(sourceCityNodeId, destinationCityNodeId, distanceBuffer, durationBuffer);
    }

    @GetMapping("wayEdges/optimal/annealing")
    public GetRouteResponse findOptimalSimulatedAnnealing(
            @RequestParam("source") Long sourceCityNodeId,
            @RequestParam("destination") Long destinationCityNodeId,
            @RequestParam("distanceBuffer") Double distanceBuffer,
            @RequestParam("durationBuffer") Double durationBuffer) {
        return wayEdgeService.findOptimalSimulatedAnnealing(sourceCityNodeId, destinationCityNodeId, distanceBuffer, durationBuffer);
    }
}
