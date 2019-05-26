package edu.route.planner.model;

import com.vividsolutions.jts.geom.Geometry;
import com.vladmihalcea.hibernate.type.basic.PostgreSQLHStoreType;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

@Data
@Entity
@Table(name = "NODES")
@TypeDef(name = "hstore", typeClass = PostgreSQLHStoreType.class)
public class Node{

    @Id
    @NotNull
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "VERSION")
    private Integer version;

    @Column(name = "USER_ID")
    private Integer userID;

    @Column(name = "TSTAMP")
    private Timestamp timestamp;

    @Column(name = "CHANGESET_ID")
    private Long changesetId;

    @Type(type = "hstore")
    @Column(name = "TAGS", columnDefinition = "hstore")
    private Map<String, String> tags = new HashMap<String, String>();

    @Column(name = "GEOM")
    private Geometry geom;
}
