package edu.route.planner.service;

import edu.route.planner.dao.CityNodeRepository;
import edu.route.planner.dao.ProximityEdgeRepository;
import edu.route.planner.dao.WayEdgeRepository;
import edu.route.planner.model.CityNode;
import edu.route.planner.model.ProximityEdge;
import edu.route.planner.model.WayEdge;
import edu.route.planner.utils.Osrm;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Optional;

import static java.util.Arrays.asList;
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
            WayEdge aToBWay = findDirect(pe.getCityAId(), pe.getCityBId());
            WayEdge bToAWay = findDirect(pe.getCityBId(), pe.getCityAId());
            wayEdgeRepository.saveAll(asList(aToBWay, bToAWay));
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
            return Osrm.getFastestRoute(sourceCityNode, destinationCityNode);
        } catch (IOException | NoSuchElementException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Collection<WayEdge> findOptimal(Long sourceCityNodeId, Long destinationCityNodeId,
                                           Double distanceBuffer, Double durationBuffer) {
        WayEdge directWay = findDirect(sourceCityNodeId, destinationCityNodeId);
        Collection<CityNode> cities = cityNodeRepository.findAllWithinBuffer(directWay.getGeometry(), distanceBuffer);
        Collection<CityNode> optionalCities = cities.stream()
                .filter(cn -> !cn.getId().equals(sourceCityNodeId) && !cn.getId().equals(destinationCityNodeId))
                .collect(toSet());

        Collection<Long> cityIds = cities.stream()
                .map(CityNode::getId)
                .collect(toSet());
        Collection<WayEdge> optionalWays = new HashSet<>();
        for (ProximityEdge proximityEdge : proximityEdgeRepository.findByCityIds(cityIds)) {
            optionalWays.add(findDirect(proximityEdge.getCityAId(), proximityEdge.getCityBId()));
            optionalWays.add(findDirect(proximityEdge.getCityBId(), proximityEdge.getCityAId()));
        }

        return optionalWays;
    }

    @Override
    public Iterable<WayEdge> findAll() {
        return wayEdgeRepository.findAll();
    }
}
