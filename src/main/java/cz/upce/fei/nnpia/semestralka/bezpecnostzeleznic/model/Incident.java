package cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.geolatte.geom.Geometry;

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


    @Enumerated(EnumType.STRING)
    private IncidentType incidentType;
    @ManyToOne(fetch = FetchType.LAZY)
    private Wagon wagon;

    @ManyToOne
    private Region region;

    @ManyToOne
    private User user;

    @JsonIgnore
    @OneToMany(mappedBy = "incident", cascade = CascadeType.REMOVE)
    private List<Damage> damages;
}
