package edu.route.planner.Models.Relations;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface RelationRepository extends CrudRepository<Relation, Long> {
}