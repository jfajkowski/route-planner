package edu.route.planner.service;

import edu.route.planner.algorithms.BruteForce;
import edu.route.planner.algorithms.Graph.*;
import edu.route.planner.algorithms.RouterAlgorithm;
import edu.route.planner.algorithms.SimulatedAnnealing;
import edu.route.planner.contracts.GetRouteResponse;
import edu.route.planner.dao.CityNodeRepository;
import edu.route.planner.dao.ProximityEdgeRepository;
import edu.route.planner.dao.WayEdgeRepository;
import edu.route.planner.model.CityNode;
import edu.route.planner.model.ProximityEdge;
import edu.route.planner.model.WayEdge;
import edu.route.planner.utils.Osrm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

import static edu.route.planner.algorithms.BruteForce.toAllCityIds;
import static java.util.stream.Collectors.toSet;

@Service
public class WayEdgeServiceImpl implements WayEdgeService {

    private static final Logger logger = LoggerFactory.getLogger(WayEdgeService.class);

    private final CityNodeRepository cityNodeRepository;
    private final ProximityEdgeRepository proximityEdgeRepository;
    private final WayEdgeRepository wayEdgeRepository;

    public WayEdgeServiceImpl(CityNodeRepository cityNodeRepository, ProximityEdgeRepository proximityEdgeRepository, WayEdgeRepository wayEdgeRepository) {
        this.cityNodeRepository = cityNodeRepository;
        this.proximityEdgeRepository = proximityEdgeRepository;
        this.wayEdgeRepository = wayEdgeRepository;
    }

    @Override
    public void recalculateCache() {
        wayEdgeRepository.deleteAll();
        for (ProximityEdge pe : proximityEdgeRepository.findAll()) {
            wayEdgeRepository.save(findDirect(pe.getCityAId(), pe.getCityBId()));
        }
    }

    @Override
    public WayEdge findDirect(Long sourceCityNodeId, Long destinationCityNodeId) {
        Optional<WayEdge> result = wayEdgeRepository.findByCityNodeIds(sourceCityNodeId, destinationCityNodeId);

        if (result.isPresent()) {
            return result.get();
        }

        try {
            CityNode sourceCityNode = cityNodeRepository.findById(sourceCityNodeId).orElseThrow();
            CityNode destinationCityNode = cityNodeRepository.findById(destinationCityNodeId).orElseThrow();
            WayEdge fastestRoute = Osrm.getFastestRoute(sourceCityNode, destinationCityNode);
            wayEdgeRepository.save(fastestRoute);
            return fastestRoute;
        } catch (IOException | NoSuchElementException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Iterable<WayEdge> findAll() {
        return wayEdgeRepository.findAll();
    }

    @Override
    public GetRouteResponse findOptimalBruteForce(Long sourceCityNodeId, Long destinationCityNodeId,
                                               Double distanceBuffer, Double durationBuffer) {
        double distanceInKmBuffer = distanceBuffer * 1000;
        double durationInHBuffer = durationBuffer * 60 * 60;
        WayEdge directWay = findDirect(sourceCityNodeId, destinationCityNodeId);

        Collection<WayEdge> optionalWays = findOptionalProximityGraphWayEdges(directWay, distanceInKmBuffer);

        List<WayEdge> result = new BruteForce(directWay, optionalWays, distanceInKmBuffer, durationInHBuffer).run();
        return new GetRouteResponse(Path.calculatePathDistance(result), Path.calculatePathDuration(result), result);
    }

    @Override
    public GetRouteResponse findOptimalCustom(Long sourceCityNodeId, Long destinationCityNodeId, Double distanceBuffer, Double durationBuffer) {
        Double distanceInKmBuffer = distanceBuffer * 1000;
        Double durationInHBuffer = durationBuffer * 60 * 60;

        WayEdge directWay = findDirect(sourceCityNodeId, destinationCityNodeId);
        List<WayEdge> wayEdges = findOptionalProximityGraphWayEdges(directWay, distanceInKmBuffer);
        Set<Long> cityNodeIds = new HashSet<>(toAllCityIds(wayEdges));
        cityNodeIds.add(directWay.getSourceCityNodeId());
        cityNodeIds.add(directWay.getDestinationCityNodeId());

        GraphBuilder gb = new GraphBuilder(this);
        NodesGraph graph = gb.loadEdges(wayEdges, cityNodeIds, destinationCityNodeId);
        GraphBuilder reversedGb = new GraphBuilder(this);
        NodesGraph reversedGraph = reversedGb.loadEdges(wayEdges, cityNodeIds, sourceCityNodeId);

        Vertex source = graph.getVertex(sourceCityNodeId);
        Vertex destination = graph.getVertex(destinationCityNodeId);

        RouterAlgorithm ra = new RouterAlgorithm(
                source,
                destination,
                graph,
                reversedGraph,
                distanceInKmBuffer,
                durationInHBuffer
        );

        List<Edge> calculatedPath = ra.calculateRoute();
        List<WayEdge> result = new ArrayList<>();
        calculatedPath.forEach(e -> wayEdgeRepository.findById(e.getId()).ifPresent(result::add));

        return new GetRouteResponse(Path.calculatePathDistance(calculatedPath), Path.calculatePathDuration(calculatedPath), result);
    }

    @Override
    public GetRouteResponse findOptimalSimulatedAnnealing(Long sourceCityNodeId, Long destinationCityNodeId,
                                                          Double distanceBuffer, Double durationBuffer) {
        double distanceInKmBuffer = distanceBuffer * 1000;
        double durationInHBuffer = durationBuffer * 60 * 60;
        WayEdge directWay = findDirect(sourceCityNodeId, destinationCityNodeId);

        List<WayEdge> optionalWays = findOptionalProximityGraphWayEdges(directWay, distanceInKmBuffer);

        List<WayEdge> result = new SimulatedAnnealing(directWay, optionalWays, distanceInKmBuffer, durationInHBuffer).run();
        return new GetRouteResponse(Path.calculatePathDistance(result), Path.calculatePathDuration(result), result);
    }

    private List<WayEdge> findOptionalProximityGraphWayEdges(WayEdge directWay, Double distanceBuffer) {
        Collection<CityNode> cities = cityNodeRepository.findAllWithinBuffer(directWay.getGeometry(), distanceBuffer / 2);
        Collection<Long> cityIds = cities.stream()
                .map(CityNode::getId)
                .collect(toSet());
        List<WayEdge> optionalWays = new ArrayList<>();
        if (!cityIds.isEmpty()) {
            Collection<ProximityEdge> pes = proximityEdgeRepository.findByCityIds(cityIds);
            for (ProximityEdge proximityEdge : pes) {
                optionalWays.add(findDirect(proximityEdge.getCityAId(), proximityEdge.getCityBId()));
            }
        }
        logger.info("Found {} way edges", optionalWays.size());
        return optionalWays;
    }
}
