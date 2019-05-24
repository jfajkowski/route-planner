package edu.route.planner.model;

import lombok.Data;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
class RelationMemberId implements Serializable {
    private Long relationId;
    private Long memberId;
}
