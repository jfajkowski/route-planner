package edu.route.planner.Models.WayNodes;

import lombok.Data;

import java.io.Serializable;

@Data
public class WayNodeId implements Serializable {
    private Long way;
    private Long node;
}