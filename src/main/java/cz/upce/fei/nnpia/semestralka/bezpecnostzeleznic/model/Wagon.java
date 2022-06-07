package cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "wagons")
public class Wagon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long externalId;

    @Column
    private int length;

    @Column
    private int weight;

    @Column
    private ZonedDateTime validFrom;

    @Column
    private ZonedDateTime validTo;

    @ManyToOne(fetch = FetchType.EAGER)
    private WagonType wagonType;

    @ManyToOne(fetch = FetchType.EAGER)
    private Carrier carrier;

    @OneToMany(mappedBy = "wagon", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private Set<Incident> incidents;

    public Wagon() {
        this.incidents = Collections.EMPTY_SET;
    }

    public Wagon(Long externalId, WagonType wagonType) {
        this();
        this.externalId = externalId;
        this.wagonType = wagonType;
    }

    public Wagon(Long externalId, int length, int weight, ZonedDateTime validFrom, ZonedDateTime validTo, WagonType wagonType) {
        this();
        this.externalId = externalId;
        this.length = length;
        this.weight = weight;
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.wagonType = wagonType;
    }

    public Wagon(Long id, Long externalId, int length, int weight, ZonedDateTime validFrom, ZonedDateTime validTo,
                 WagonType wagonType, Carrier carrier,
                 Set<Incident> incidents) {
        this.id = id;
        this.externalId = externalId;
        this.length = length;
        this.weight = weight;
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.wagonType = wagonType;
        this.carrier = carrier;
        this.incidents = incidents;
    }
}
