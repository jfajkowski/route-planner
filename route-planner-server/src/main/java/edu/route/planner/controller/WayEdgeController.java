package edu.route.planner.controller;

import edu.route.planner.dao.CityNodeRepository;
import edu.route.planner.dao.WayEdgeRepository;
import edu.route.planner.model.CityNode;
import edu.route.planner.model.WayEdge;
import edu.route.planner.utils.Osrm;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;

@RestController
public class WayEdgeController {

    private final WayEdgeRepository wayEdgeRepository;
    private final CityNodeRepository cityNodeRepository;

    public WayEdgeController(WayEdgeRepository wayEdgeRepository, CityNodeRepository cityNodeRepository) {
        this.wayEdgeRepository = wayEdgeRepository;
        this.cityNodeRepository = cityNodeRepository;
    }

    @GetMapping("wayEdges/direct")
    public WayEdge findDirectWayEdge(
            @RequestParam("source") Long sourceCityNodeId,
            @RequestParam("destination") Long destinationCityNodeId) throws IOException {

        Optional<WayEdge> result = wayEdgeRepository.findByCityNodeIds(sourceCityNodeId, destinationCityNodeId);
        if (result.isEmpty()) {
            List<Long> cityNodeIds = asList(sourceCityNodeId, destinationCityNodeId);
            Iterator<CityNode> cityNodeIterator = cityNodeRepository.findAllById(cityNodeIds).iterator();
            return Osrm.getFastestRoute(cityNodeIterator.next(), cityNodeIterator.next());
        } else {
            return result.get();
        }
    }
}
