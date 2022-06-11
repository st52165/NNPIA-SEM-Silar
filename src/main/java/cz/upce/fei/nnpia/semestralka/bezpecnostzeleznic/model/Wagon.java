package cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Collections;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "wagons")
@AllArgsConstructor
public class Wagon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int length;

    @Column(nullable = false)
    private int weight;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WagonType wagonType;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private Carrier carrier;

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "wagons")
    @JsonIgnore
    private Set<Incident> incidents;

    public Wagon() {
        this.incidents = Collections.emptySet();
    }

    public Wagon(WagonType wagonType) {
        this();
        this.wagonType = wagonType;
    }

    public Wagon(int length, int weight, WagonType wagonType) {
        this();
        this.length = length;
        this.weight = weight;
        this.wagonType = wagonType;
    }
}
