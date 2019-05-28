package edu.route.planner.dao;

import edu.route.planner.model.ProximityEdge;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface ProximityEdgeRepository extends CrudRepository<ProximityEdge, Long> {

    @Query("from ProximityEdge where cityAId in (:cityIds) or cityBId in (:cityIds)")
    Collection<ProximityEdge> findByCityIds(@Param("cityIds") Collection<Long> cityIds);
}
