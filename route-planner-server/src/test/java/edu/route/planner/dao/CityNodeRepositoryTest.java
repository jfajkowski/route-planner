package edu.route.planner.dao;

import edu.route.planner.model.CityNode;
import edu.route.planner.model.WayEdge;
import edu.route.planner.service.WayEdgeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestContextManager;

import java.util.Collection;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
@SpringBootTest
public class CityNodeRepositoryTest {

    private final String startCity;
    private final String targetCity;
    private final Double buffer;
    private final List<String> expectedCitiesWithinBuffer;
    @Autowired
    private CityNodeRepository cityNodeRepository;
    @Autowired
    private WayEdgeService wayEdgeService;

    public CityNodeRepositoryTest(String startCity, String targetCity, Double buffer, List<String> expectedCitiesWithinBuffer) {
        this.startCity = startCity;
        this.targetCity = targetCity;
        this.buffer = buffer;
        this.expectedCitiesWithinBuffer = expectedCitiesWithinBuffer;
    }

    @Parameters(name = "{index}: startCity: {0}, targetCity: {1}, buffer: {2}, expectedCitiesWithinBuffer: {3}")
    public static Collection<Object[]> data() {
        return asList(new Object[][]{
                {"Warszawa", "Kobyłka", 0.0, emptyList()},
                {"Warszawa", "Kobyłka", 1.0, emptyList()},
                {"Warszawa", "Kobyłka", 10.0, emptyList()},
                {"Warszawa", "Kobyłka", 100.0, asList("Warszawa", "Kobyłka")},
                {"Warszawa", "Kobyłka", 1000.0, asList("Warszawa", "Kobyłka", "Zielonka")},
                {"Warszawa", "Kobyłka", 10000.0, asList("Kobyłka", "Warszawa", "Marki", "Łomianki",
                        "Wołomin", "Ząbki", "Zielonka", "Radzymin")}
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

        WayEdge directWayEdge = wayEdgeService.findDirect(startCityNode.getId(), targetCityNode.getId());

        Collection<String> actualCitiesWithinBuffer = cityNodeRepository
                .findAllWithinBuffer(directWayEdge.getGeometry(), buffer).stream()
                .map(CityNode::getCityName)
                .collect(toSet());

        if (expectedCitiesWithinBuffer.isEmpty()) {
            assertThat(actualCitiesWithinBuffer).isEmpty();
        } else {
            assertThat(actualCitiesWithinBuffer).containsAll(expectedCitiesWithinBuffer);
        }
    }
}
