package cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.model;

import lombok.*;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @NaturalId
    @Column(length = 60, unique = true)
    private RoleName name;

}