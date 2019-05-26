package edu.route.planner.model;

import lombok.Data;
import org.locationtech.jts.geom.Geometry;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "GRAPH_EDGES")
public class GraphEdge {

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "SRC_CITY_NODE_ID")
    private Long sourceCityNodeId;

    @Column(name = "DST_CITY_NODE_ID")
    private Long destinationCityNodeId;

    @Column(name = "GEOMETRY")
    private Geometry geometry;

    @Column(name = "DISTANCE")
    private Integer distance;

    @Column(name = "DURATION")
    private Double duration;
}
