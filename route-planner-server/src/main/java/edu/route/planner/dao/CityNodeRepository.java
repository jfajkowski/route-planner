package edu.route.planner.dao;

import edu.route.planner.model.CityNode;
import org.locationtech.jts.geom.Geometry;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface CityNodeRepository extends CrudRepository<CityNode, Long> {

    CityNode findByCityName(String cityName);

    @Query(nativeQuery = true,
            value = "select * from city_nodes cn where st_contains(st_buffer(:w, :b), cn.geom)")
    Collection<CityNode> findAllWithinBuffer(@Param("w") Geometry way, @Param("b") Double buffer);
}
