package edu.route.planner.Models.Relations;

import com.vladmihalcea.hibernate.type.basic.PostgreSQLHStoreType;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

@Data
@Entity
@Table(name = "RELATIONS")
@TypeDef(name = "hstore", typeClass = PostgreSQLHStoreType.class)
public class Relation {
    @Id
    @Column(name = "ID")
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
    private Map<String, String> tags = new HashMap<>();
}