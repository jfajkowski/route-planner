package edu.route.planner.router;

import edu.route.planner.dao.CityNodeRepository;
import edu.route.planner.model.CityNode;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestContextManager;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RunWith(Parameterized.class)
@SpringBootTest
@ActiveProfiles("tests")
public class RouterTestsContextProvider {

    @Autowired
    CityNodeRepository cityNodeRepository;

    private TestContextManager testContextManager;

    @Before
    public void setUp() throws Exception {
        this.testContextManager = new TestContextManager(getClass());
        this.testContextManager.prepareTestInstance(this);
    }

    Set<CityNode> getCitiesNodesByNames(List<String> cityNames){
        return cityNames.stream()
                .map(cityName -> cityNodeRepository.findByCityName(cityName))
                .collect(Collectors.toSet());
    }
}
