package edu.route.planner.dao;

import edu.route.planner.model.Node;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(propagation = Propagation.MANDATORY)
public interface NodeRepository extends CrudRepository<Node, Long> {
}