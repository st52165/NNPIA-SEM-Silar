package cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "username"
        }),
        @UniqueConstraint(columnNames = {
                "email"
        })
})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class User {
    public static final int MIN_NAME_LENGTH = 3;
    public static final int MAX_NAME_LENGTH = 50;
    public static final int MIN_PASSWORD_LENGTH = 6;
    public static final int MAX_PASSWORD_LENGTH = 100;
    public static final int MIN_USERNAME_LENGTH = 5;
    public static final int MAX_USERNAME_LENGTH = 50;
    public static final int MAX_EMAIL_LENGTH = 50;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "varchar(50) default 'NULL'")
    @Size(min = MIN_NAME_LENGTH, max = MAX_NAME_LENGTH)
    private String firstname;

    @Column(nullable = false, columnDefinition = "varchar(50) default 'NULL'")
    @Size(min = MIN_NAME_LENGTH, max = MAX_NAME_LENGTH)
    private String lastname;

    @Column(nullable = false, unique = true, columnDefinition = "varchar(50) default 'NULL'")
    @Size(min = MIN_USERNAME_LENGTH, max = MAX_USERNAME_LENGTH)
    private String username;

    @Column(nullable = false, unique = true, columnDefinition = "varchar(50) default 'NULL'")
    @Size(max = MAX_EMAIL_LENGTH)
    @Email
    private String email;

    @Column(nullable = false, columnDefinition = "varchar(100) default 'NULL'")
    @Size(min = MIN_PASSWORD_LENGTH, max = MAX_PASSWORD_LENGTH)
    @JsonIgnore
    private String password;


    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private Role role;
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private Carrier carrier;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private Set<Incident> incidents;

    public User(String firstname, String lastname, String username, String email, String password, Role role) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.carrier = null;
    }

    public User(String firstname, String lastname, String username, String email, String password, Role role, Carrier carrier) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.carrier = carrier;
    }
}