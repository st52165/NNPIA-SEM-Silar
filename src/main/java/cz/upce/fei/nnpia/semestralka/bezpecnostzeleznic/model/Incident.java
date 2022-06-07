package cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.model;

import com.bedatadriven.jackson.datatype.jts.serialization.GeometryDeserializer;
import com.bedatadriven.jackson.datatype.jts.serialization.GeometrySerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.vividsolutions.jts.geom.Geometry;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "incidents")
public class Incident {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "Geometry")
    @JsonSerialize(using = GeometrySerializer.class)
    @JsonDeserialize(using = GeometryDeserializer.class)
    private Geometry position;

    @Column(nullable = false)
    private ZonedDateTime validityFrom;

    @Column(nullable = false)
    private ZonedDateTime validityTo;

    @Column
    private String description;

    @Column(nullable = false)
    private boolean isCriminalOffense;

    @Column(nullable = false)
    private boolean isSolvedByPolice;

    @Column(nullable = false)
    private boolean isFireIncident;


    @ManyToOne(fetch = FetchType.LAZY)
    private Wagon wagon;

    @ManyToOne
    private District district;

    @ManyToOne
    private User user;

    @JsonIgnore
    @OneToMany(mappedBy = "incident", cascade = CascadeType.REMOVE)
    private List<Damage> damages;
}
