package edu.route.planner.dao;

import edu.route.planner.model.RelationMember;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface RelationMemberRepository extends CrudRepository<RelationMember, Long> {
}