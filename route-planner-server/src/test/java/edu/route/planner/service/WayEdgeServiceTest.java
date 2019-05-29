package edu.route.planner.service;

import edu.route.planner.dao.WayEdgeRepository;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WayEdgeServiceTest {

    @Autowired
    private WayEdgeService wayEdgeService;

    @Autowired
    private WayEdgeRepository wayEdgeRepository;

    @Test
    public void recalculateCache() {
        wayEdgeService.recalculateCache();
        wayEdgeRepository.findAll();
    }
}
