package edu.route.planner.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "STRAIGHT_DISTANCES")
public class StraightDistance {

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "CITY_A")
    private String cityA;

    @Column(name = "CITY_B")
    private String cityB;

    @Column(name = "DISTANCE")
    private Double distance;
}
