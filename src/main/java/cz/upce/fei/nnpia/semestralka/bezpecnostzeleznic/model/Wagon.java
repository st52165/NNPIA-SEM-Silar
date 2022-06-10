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

    @Column
    private int length;

    @Column
    private int weight;

    @Enumerated(EnumType.STRING)
    private WagonType wagonType;

    @ManyToOne(fetch = FetchType.EAGER)
    private Carrier carrier;

    @OneToMany(mappedBy = "wagon", cascade = CascadeType.REMOVE)
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
