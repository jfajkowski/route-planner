package edu.route.planner.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Table(name = "RELATION_MEMBERS")
@IdClass(RelationMemberId.class)
public class RelationMember {

    @Id
    @Column(name = "RELATION_ID")
    private Long relationId;

    @Id
    @Column(name = "MEMBER_ID")
    private Long memberId;

    @Column(name = "MEMBER_TYPE")
    private Character memberType;

    @Column(name = "MEMBER_ROLE")
    private String memberRole;

    @NotNull
    @Column(name = "SEQUENCE_ID", nullable = false)
    private Integer sequenceId;
}
