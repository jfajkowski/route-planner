package edu.route.planner.Models.RelationMembers;

import lombok.Data;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
public class RelationMemberId implements Serializable {
    private Long relationId;
    private Long memberId;
}