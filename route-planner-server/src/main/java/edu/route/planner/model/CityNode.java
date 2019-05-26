package edu.route.planner.model;

import lombok.Data;
import org.locationtech.jts.geom.Geometry;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "CITY_NODES")
public class CityNode {

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME")
    private String cityName;

    @Column(name = "GEOM")
    private Geometry geom;

    @Column(name = "BOUNDARIES")
    private Geometry boundaries;

}
