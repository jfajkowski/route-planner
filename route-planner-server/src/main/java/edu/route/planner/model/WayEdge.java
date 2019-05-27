package edu.route.planner.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import edu.route.planner.utils.GeometryDeserializer;
import edu.route.planner.utils.GeometrySerializer;
import lombok.Data;
import org.locationtech.jts.geom.Geometry;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "WAY_EDGES")
public class WayEdge {

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "SRC_CITY_NODE_ID")
    private Long sourceCityNodeId;

    @Column(name = "DST_CITY_NODE_ID")
    private Long destinationCityNodeId;

    @JsonSerialize(using = GeometrySerializer.class)
    @JsonDeserialize(using = GeometryDeserializer.class)
    @Column(name = "GEOMETRY")
    private Geometry geometry;

    @Column(name = "DISTANCE")
    private Double distance;

    @Column(name = "DURATION")
    private Double duration;
}
