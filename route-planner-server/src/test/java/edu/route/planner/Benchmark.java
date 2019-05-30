package edu.route.planner;

import edu.route.planner.algorithms.BruteForce;
import edu.route.planner.dao.CityNodeRepository;
import edu.route.planner.model.CityNode;
import edu.route.planner.model.WayEdge;
import edu.route.planner.service.WayEdgeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestContextManager;

import java.util.Collection;
import java.util.List;

import static java.util.Arrays.asList;
import static org.apache.commons.lang3.time.DurationFormatUtils.formatDurationHMS;

@RunWith(Parameterized.class)
@SpringBootTest
public class Benchmark {

    private static final Logger logger = LoggerFactory.getLogger(Benchmark.class);
    private final String startCity;
    private final String targetCity;
    @Autowired
    private CityNodeRepository cityNodeRepository;
    @Autowired
    private WayEdgeService wayEdgeService;

    public Benchmark(String startCity, String targetCity) {
        this.startCity = startCity;
        this.targetCity = targetCity;
    }

    @Parameters(name = "{index}: startCity: {0}, targetCity: {1}")
    public static Collection<Object[]> data() {
        return asList(new Object[][]{
                {"Wisła", "Zakopane"},
                {"Warszawa", "Katowice"},
                {"Gliwice", "Opole"},
                {"Żyrardów", "Tłuszcz"},
                {"Poznań", "Konin"}
        });
    }

    @Before
    public void setUp() throws Exception {
        new TestContextManager(getClass()).prepareTestInstance(this);
    }

    @Test
    public void findAllWithinBuffer() {
        CityNode startCityNode = cityNodeRepository.findByCityName(startCity);
        CityNode targetCityNode = cityNodeRepository.findByCityName(targetCity);

        double kilometers = 10;
        double hours = 6;

        measureTime("Custom", () -> {
            List<WayEdge> optimalCustom = (List<WayEdge>) wayEdgeService.findOptimalCustom(startCityNode.getId(), targetCityNode.getId(), kilometers, hours).route;
            logger.info("Custom: {}", BruteForce.toString(optimalCustom));
        });
        measureTime("Brute force", () -> {
            List<WayEdge> optimalBruteForce = (List<WayEdge>) wayEdgeService.findOptimalBruteForce(startCityNode.getId(), targetCityNode.getId(), kilometers, hours).route;
            logger.info("Brute force: {}", BruteForce.toString(optimalBruteForce));
        });
    }

    private void measureTime(String name, Runnable runnable) {
        long startTime = System.currentTimeMillis();
        runnable.run();
        long estimatedTime = System.currentTimeMillis() - startTime;
        logger.info("{} took: {}", name, formatDurationHMS(estimatedTime));
    }
}
