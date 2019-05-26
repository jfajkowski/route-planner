package edu.route.planner.utils;

import com.vividsolutions.jts.geom.Coordinate;
import edu.route.planner.model.CityNode;
import edu.route.planner.model.GraphEdge;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Answers.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * These tests require OSRM backend running
 */
@RunWith(MockitoJUnitRunner.class)
public class OsrmTest {

    private static final String QUERY = "20.7810167,52.2326063;21.163768,52.3400262";

    @Mock(answer = RETURNS_DEEP_STUBS)
    private CityNode cityNodeA;

    @Mock(answer = RETURNS_DEEP_STUBS)
    private CityNode cityNodeB;

    @Before
    public void runBefore() {
        initMocks(OsrmTest.class);
    }

    @Ignore
    @Test
    public void benchmarkFastestRoute() {
        long startTime = System.nanoTime();
        for (int i = 0; i < 1000; ++i) {
            boolean received = false;
            while (!received) {
                try {
                    Osrm.getFastestRoute(QUERY);
                    received = true;
                } catch (URISyntaxException | IOException e) {
                    e.printStackTrace();
                }
            }
        }
        long endTime = System.nanoTime();

        long duration = (endTime - startTime);
        System.out.println(duration);
    }

    @Ignore
    @Test
    public void getFastestRoute() throws IOException, URISyntaxException {
        when(cityNodeA.getId()).thenReturn(1L);
        when(cityNodeA.getGeom().getCoordinate()).thenReturn(new Coordinate(52.2326063, 20.7810167));
        when(cityNodeB.getId()).thenReturn(2L);
        when(cityNodeB.getGeom().getCoordinate()).thenReturn(new Coordinate(52.3400262, 21.163768));
        GraphEdge graphEdge = Osrm.getFastestRoute(cityNodeA, cityNodeB);
        assertThat(graphEdge.getSourceCityNodeId()).isEqualTo(1L);
        assertThat(graphEdge.getDestinationCityNodeId()).isEqualTo(2L);
    }
}
