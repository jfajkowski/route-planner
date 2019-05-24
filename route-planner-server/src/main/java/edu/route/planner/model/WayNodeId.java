package edu.route.planner.model;

import lombok.Data;

import java.io.Serializable;

@Data
class WayNodeId implements Serializable {
    private Long way;
    private Long node;
}
