package edu.route.planner.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import edu.route.planner.utils.GeometryDeserializer;
import edu.route.planner.utils.GeometrySerializer;
import lombok.Data;
import org.locationtech.jts.geom.Geometry;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity
@Table(name = "WAY_EDGES")
public class WayEdge {

    @Id
    @GeneratedValue(strategy = IDENTITY)
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

    public WayEdge() {

    }

    public WayEdge(Long sourceCityNodeId, Long destinationCityNodeId, Double distance, Double duration) {
        this.sourceCityNodeId = sourceCityNodeId;
        this.destinationCityNodeId = destinationCityNodeId;
        this.distance = distance;
        this.duration = duration;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Double getDuration() {
        return duration;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }

    public Long getId() {
        return id;
    }

    public Long getSourceCityNodeId() {
        return sourceCityNodeId;
    }

    public Long getDestinationCityNodeId() {
        return destinationCityNodeId;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setSourceCityNodeId(Long sourceCityNodeId) {
        this.sourceCityNodeId = sourceCityNodeId;
    }

    public void setDestinationCityNodeId(Long destinationCityNodeId) {
        this.destinationCityNodeId = destinationCityNodeId;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }
}
