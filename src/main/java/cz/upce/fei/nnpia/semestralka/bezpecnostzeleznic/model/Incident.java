package cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.geolatte.geom.Geometry;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    @Column(nullable = false)
    private IncidentType incidentType;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "incident_wagons",
            joinColumns = @JoinColumn(name = "incident_id"),
            inverseJoinColumns = @JoinColumn(name = "wagon_id"))
    private Set<Wagon> wagons = new HashSet<>();

    @ManyToOne(optional = false)
    private Region region;

    @ManyToOne(optional = false)
    private User user;

    @JsonIgnore
    @OneToMany(mappedBy = "incident", cascade = CascadeType.REMOVE)
    private List<Damage> damages;
}
