package edu.route.planner.Models.RelationMembers;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface RelationMemberRepository extends CrudRepository<RelationMember, Long> {
}