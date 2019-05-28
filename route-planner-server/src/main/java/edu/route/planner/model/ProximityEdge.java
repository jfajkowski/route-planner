package edu.route.planner.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "VORONOI_NNS")
public class ProximityEdge {

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "CITY_A_ID")
    private Long cityAId;

    @Column(name = "CITY_B_ID")
    private Long cityBId;
}
