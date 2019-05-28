package edu.route.planner.router;

import edu.route.planner.dao.CityNodeRepository;
import edu.route.planner.model.CityNode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
@SpringBootTest
public class PointsBehindTargetTests extends RouterTestsContextProvider{

    @Autowired
    CityNodeRepository cityNodeRepository;

    private String startCityName;
    private String destCityName;
    private Integer additionalKms;
    private Integer additionalTime;
    private List<String> citiesToVisitNames;

    @Parameters(name = "{index}: Start: {0}, Target: {1}, AddKM: {2}, AddMinutes: {3}, CitiesToVisit: {4}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {"Tychy","Żory",10,null,Arrays.asList("Tychy","Żory")},
                {"Tychy","Żory",56,null,Arrays.asList("Tychy","Żory","Rybnik","Jastrzębie-Zdrój")},
                {"Tychy","Żory",null,50,Arrays.asList("Tychy","Żory","Jastrzębie-Zdrój")},
                {"Tychy","Żory",56,50,Arrays.asList("Tychy","Żory","Jastrzębie-Zdrój")},
                {"Tychy","Żory",56,93,Arrays.asList("Tychy","Żory","Rybnik","Jastrzębie-Zdrój")},
                {"Tychy","Żory",31,50,Arrays.asList("Tychy","Żory","Jastrzębie-Zdrój")},
                {"Tychy","Żory",10,93,Arrays.asList("Tychy","Żory")}
        });
    }

    public PointsBehindTargetTests(String startCityName, String destCityName,
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

        org.locationtech.jts.util.Assert.equals(actualCitiesToVisit.size(), expectedCitiesToVisit.size());
        org.locationtech.jts.util.Assert.equals(actualCitiesToVisit, expectedCitiesToVisit);
    }
}