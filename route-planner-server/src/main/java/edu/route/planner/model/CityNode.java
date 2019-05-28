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
@Table(name = "CITY_NODES")
public class CityNode {

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME")
    private String cityName;

    @JsonSerialize(using = GeometrySerializer.class)
    @JsonDeserialize(using = GeometryDeserializer.class)
    @Column(name = "GEOM")
    private Geometry geom;
}
