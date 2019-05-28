package edu.route.planner.algorithms;

import edu.route.planner.dao.CityNodeRepository;
import edu.route.planner.model.CityNode;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SimulatedAnnealingTest {

    @Autowired
    private CityNodeRepository cityNodeRepository;

    @Ignore
    @Test
    public void run() {
        SimulatedAnnealing.Parameters parameters = new SimulatedAnnealing.Parameters();
        parameters.coolingFactor = 0.9;
        parameters.maxIterations = 1000;
        parameters.initialTemperature = 10000;

        SimulatedAnnealing.Constraints constraints = new SimulatedAnnealing.Constraints();
        constraints.durationBuffer = 60 * 60;
        constraints.distanceBuffer = 100 * 1000;

        List<CityNode> allNodes = new ArrayList<>();
        cityNodeRepository.findAll().forEach(allNodes::add);

        CityNode sourceNode = allNodes.get(0);
        CityNode destinationNode = allNodes.get(allNodes.size() - 1);
        List<CityNode> optionalNodes = allNodes.subList(0, allNodes.size() - 1);

        SimulatedAnnealing.run(parameters, constraints, sourceNode, destinationNode, optionalNodes);
    }
}
