package edu.route.planner.algorithms;

import edu.route.planner.model.WayEdge;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class SimulatedAnnealingTest {

    private static final double DISTANCE = 1.0;
    private static final double DURATION = 1.0;
    private static final WayEdge DIRECT_WAY_EDGE = new WayEdge(0L, 1L, 1.0, 1.0);

    private final Collection<WayEdge> alternativeWayEdges;
    private final double distanceBuffer;
    private final double durationBuffer;
    private final long expectedPathLength;

    public SimulatedAnnealingTest(Collection<WayEdge> alternativeWayEdges, double distanceBuffer, double durationBuffer, long expectedPathLength) {
        this.alternativeWayEdges = alternativeWayEdges;
        this.distanceBuffer = distanceBuffer;
        this.durationBuffer = durationBuffer;
        this.expectedPathLength = expectedPathLength;
    }

    @Parameters
    public static Collection<Object[]> data() {
        return asList(
                new Object[][]{
                        {fullyConnected(2), 0.0, 0.0, 1},
                        {fullyConnected(3), 0.0, 0.0, 1},
                        {fullyConnected(3), 1.0, 1.0, 2},
                        {fullyConnected(5), 0.0, 0.0, 1},
                        {fullyConnected(5), 1.0, 1.0, 2},
                        {fullyConnected(5), 2.0, 2.0, 3},
                        {fullyConnected(5), 3.0, 3.0, 4},
                        {fullyConnected(10), 10.0, 10.0, 9},
                }
        );
    }

    private static Collection<WayEdge> fullyConnected(int n) {
        List<WayEdge> result = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    result.add(new WayEdge((long) i, (long) j, DISTANCE, DURATION));
                }
            }
        }
        return result;
    }

    @Test
    public void run() {
        long pathLength = new SimulatedAnnealing(DIRECT_WAY_EDGE, alternativeWayEdges, distanceBuffer, durationBuffer).run().size();
        assertThat(pathLength).isEqualTo(expectedPathLength);
    }
}
