package edu.route.planner.Models.CityNodes;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface CityNodeRepository extends CrudRepository<CityNode, Long> {
    CityNode findByCityName(String cityName);
}