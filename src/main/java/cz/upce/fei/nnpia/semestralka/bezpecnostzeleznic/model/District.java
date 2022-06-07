package cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "districts")
public class District {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 20)
    private String shortName;

    @ManyToOne
    private Region region;

    @OneToMany(mappedBy = "district", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private Set<Incident> incidents;
}
