package edu.route.planner.service;

import edu.route.planner.algorithms.BruteForce;
import edu.route.planner.algorithms.Graph.GraphBuilder;
import edu.route.planner.algorithms.Graph.NodesGraph;
import edu.route.planner.algorithms.Graph.Vertex;
import edu.route.planner.algorithms.RouterAlgorithm;
import edu.route.planner.dao.CityNodeRepository;
import edu.route.planner.dao.ProximityEdgeRepository;
import edu.route.planner.dao.WayEdgeRepository;
import edu.route.planner.model.CityNode;
import edu.route.planner.model.ProximityEdge;
import edu.route.planner.model.WayEdge;
import edu.route.planner.utils.Osrm;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

import static java.util.stream.Collectors.toSet;

@Service
public class WayEdgeServiceImpl implements WayEdgeService {

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
    public List<WayEdge> findOptimalBruteForce(Long sourceCityNodeId, Long destinationCityNodeId,
                                               Double distanceBuffer, Double durationBuffer) {
        WayEdge directWay = findDirect(sourceCityNodeId, destinationCityNodeId);

        Collection<CityNode> cities = cityNodeRepository.findAllWithinBuffer(directWay.getGeometry(), distanceBuffer);
        Collection<Long> cityIds = cities.stream()
                .map(CityNode::getId)
                .collect(toSet());
        Collection<WayEdge> optionalWays = new HashSet<>();
        if (!cityIds.isEmpty()) {
            for (ProximityEdge proximityEdge : proximityEdgeRepository.findByCityIds(cityIds)) {
                optionalWays.add(findDirect(proximityEdge.getCityAId(), proximityEdge.getCityBId()));
            }
        }

        return BruteForce.run(directWay, optionalWays, distanceBuffer, durationBuffer);
    }

    @Override
    public List<WayEdge> findOptimalCustom(Long sourceCityNodeId, Long destinationCityNodeId, Double distanceBuffer, Double durationBuffer) {
        List<CityNode> cityNodes = new ArrayList<>();
        cityNodeRepository.findAll().forEach(cityNodes::add);

        List<WayEdge> wayEdges = new ArrayList<>();
        wayEdgeRepository.findAll().forEach(wayEdges::add);

        GraphBuilder gb = new GraphBuilder(this);
        NodesGraph graph = gb.loadEdges(wayEdges, cityNodes, destinationCityNodeId);
        GraphBuilder reversedGb = new GraphBuilder(this);
        NodesGraph reversedGraph = reversedGb.loadEdges(wayEdges, cityNodes, sourceCityNodeId);

        Vertex source = graph.getVertex(sourceCityNodeId);
        Vertex destination = graph.getVertex(destinationCityNodeId);

        RouterAlgorithm ra = new RouterAlgorithm(
                source,
                destination,
                graph,
                reversedGraph,
                distanceBuffer * 1000,
                durationBuffer * 60 * 60.0
        );

        List<WayEdge> result = new ArrayList<>();
//        ra.calculateRoute().forEach(e -> result.add(findDirect(e.getStartId(), e.getDestinationId())));
        ra.calculateRoute().forEach(e -> wayEdgeRepository.findById(e.getId()).ifPresent(result::add));
        return result;
    }

    @Override
    public Iterable<WayEdge> findAll() {
        return wayEdgeRepository.findAll();
    }
}
