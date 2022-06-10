package cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.model;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "damages")
public class Damage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private int price;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private Incident incident;
}
