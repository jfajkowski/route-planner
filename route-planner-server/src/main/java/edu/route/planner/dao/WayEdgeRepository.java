package edu.route.planner.dao;

import edu.route.planner.model.WayEdge;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WayEdgeRepository extends CrudRepository<WayEdge, Long> {

    @Query("from WayEdge we where we.sourceCityNodeId=:a and we.destinationCityNodeId=:b")
    Optional<WayEdge> findByCityNodeIds(@Param("a") Long sourceCityNodeId, @Param("b") Long destinationCityNodeId);

    @Query("from WayEdge we where we.id=:id")
    Optional<WayEdge> findById(@Param("id") Long wayId);
}
