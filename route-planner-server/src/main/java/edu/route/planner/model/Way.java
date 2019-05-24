package edu.route.planner.model;

import com.vladmihalcea.hibernate.type.basic.PostgreSQLHStoreType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
@Entity
@Table(name = "WAYS")
@ToString(exclude = {"nodes"})
@EqualsAndHashCode(exclude = {"nodes"})
@TypeDef(name = "hstore", typeClass = PostgreSQLHStoreType.class)
public class Way{
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

    @OneToMany(mappedBy = "way", fetch = FetchType.EAGER)
    private Set<WayNode> nodes = new HashSet<>();
}
