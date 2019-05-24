package edu.route.planner.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Table(name = "WAY_NODES")
@IdClass(WayNodeId.class)
public class WayNode {

    @Id
    @NotNull
    @ManyToOne
    @JoinColumn(name = "WAY_ID", nullable = false)
    private Way way;

    @Id
    @NotNull
    @ManyToOne
    @JoinColumn(name = "NODE_ID", nullable = false)
    private Node node;

    @NotNull
    @Column(name = "SEQUENCE_ID", nullable = false)
    private Integer sequenceId;
}
