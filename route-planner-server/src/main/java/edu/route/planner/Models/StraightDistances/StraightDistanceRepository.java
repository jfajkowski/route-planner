package edu.route.planner.Models.StraightDistances;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(propagation = Propagation.MANDATORY)
public interface StraightDistanceRepository extends CrudRepository<StraightDistance, Long> {
}
