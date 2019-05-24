package edu.route.planner.dao;

import edu.route.planner.model.Relation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface RelationRepository extends CrudRepository<Relation, Long> {
}
