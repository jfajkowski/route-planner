package edu.route.planner.router;

import edu.route.planner.model.CityNode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.locationtech.jts.util.Assert;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
@SpringBootTest
public class SimpleRouterTests extends RouterTestsContextProvider {

    private String startCityName;
    private String destCityName;
    private Integer additionalKms;
    private Integer additionalTime;
    private List<String> citiesToVisitNames;

    @Parameters(name = "{index}: Start: {0}, Target: {1}, AddKM: {2}, AddMinutes: {3}, CitiesToVisit: {4}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {"Żory","Rybnik",10,10,Arrays.asList("Żory","Rybnik")},
                {"Żory","Rybnik",10,40,Arrays.asList("Żory","Rybnik")},
                {"Żory","Rybnik",10,null,Arrays.asList("Żory","Rybnik")},
                {"Żory","Rybnik",30,40,Arrays.asList("Żory","Rybnik", "Jastrzębie-Zdrój")},
                {"Żory","Rybnik",null,40,Arrays.asList("Żory","Rybnik", "Jastrzębie-Zdrój")},
                {"Żory","Rybnik",30,null,Arrays.asList("Żory","Rybnik", "Jastrzębie-Zdrój")},
                {"Jaworzno","Bielsko-Biała",null,null,Arrays.asList("Jaworzno","Bielsko-Biała")},
                {"Jaworzno","Bielsko-Biała",5,null,Arrays.asList("Jaworzno","Bielsko-Biała","Tychy")},
                {"Jaworzno","Bielsko-Biała",60,null,Arrays.asList("Jaworzno","Bielsko-Biała","Tychy","Żory")},
                {"Jaworzno","Bielsko-Biała",null,5,Arrays.asList("Jaworzno","Bielsko-Biała","Tychy")},
                {"Jaworzno","Bielsko-Biała",null,130,Arrays.asList("Jaworzno","Bielsko-Biała","Tychy","Żory")},
                {"Jaworzno","Bielsko-Biała",60,130,Arrays.asList("Jaworzno","Bielsko-Biała","Tychy","Żory")},
        });
    }

    public SimpleRouterTests(String startCityName, String destCityName,
                             Integer additionalKms, Integer additionalTime, List<String> citiesToVisitNames) {
        this.startCityName = startCityName;
        this.destCityName = destCityName;
        this.additionalKms = additionalKms;
        this.additionalTime = additionalTime;
        this.citiesToVisitNames = citiesToVisitNames;
    }

    @Test
    public void should_travel_to_maximum_cities_if_possible(){
        Set<CityNode> expectedCitiesToVisit = getCitiesNodesByNames(citiesToVisitNames);

        List<CityNode> algorithmCitiesToVisit = null; //TODO: ODPALENIE ALGORYTMU Z PIERWSZYMI CZTEREMA PARAMETRAMI START, DEST CITY i oba additionale
        Set<CityNode> actualCitiesToVisit = algorithmCitiesToVisit.stream().distinct().collect(Collectors.toSet());

        Assert.equals(actualCitiesToVisit.size(), expectedCitiesToVisit.size());
        Assert.equals(actualCitiesToVisit, expectedCitiesToVisit);
    }
}
