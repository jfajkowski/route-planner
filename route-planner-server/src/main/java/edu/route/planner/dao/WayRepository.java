package edu.route.planner.dao;

import edu.route.planner.model.Way;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface WayRepository extends CrudRepository<Way, Long> {
}
