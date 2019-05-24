package edu.route.planner.controller;

import edu.route.planner.dao.CityNodeRepository;
import edu.route.planner.model.CityNode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CityNodeController {

    private final CityNodeRepository repository;

    public CityNodeController(CityNodeRepository repository) {
        this.repository = repository;
    }

    @GetMapping("cityNodes/all")
    public Iterable<CityNode> allNodes() {
        return repository.findAll();
    }
}
