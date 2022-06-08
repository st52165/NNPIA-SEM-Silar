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
    public static final int minNameLength = 3;
    public static final int maxNameLength = 50;
    public static final int minPasswordLength = 6;
    public static final int maxPasswordLength = 100;
    public static final int minUsernameLength = 5;
    public static final int maxUsernameLength = 50;
    public static final int maxEmailLength = 50;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "varchar(50) default 'NULL'")
    @Size(min = minNameLength, max = maxNameLength)
    private String firstname;

    @Column(nullable = false, columnDefinition = "varchar(50) default 'NULL'")
    @Size(min = minNameLength, max = maxNameLength)
    private String lastname;

    @Column(nullable = false, unique = true, columnDefinition = "varchar(50) default 'NULL'")
    @Size(min = minUsernameLength, max = maxUsernameLength)
    private String username;

    @Column(nullable = false, unique = true, columnDefinition = "varchar(50) default 'NULL'")
    @Size(max = maxEmailLength)
    @Email
    private String email;

    @Column(nullable = false, columnDefinition = "varchar(100) default 'NULL'")
    @Size(min = minPasswordLength, max = maxPasswordLength)
    @JsonIgnore
    private String password;


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @ManyToOne(fetch = FetchType.EAGER)
    private Carrier carrier;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private Set<Incident> incidents;

    public User(String firstname, String lastname, String username, String email, String password, Set<Role> roles) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.carrier = null;
    }

    public User(String firstname, String lastname, String username, String email, String password, Set<Role> roles, Carrier carrier) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.carrier = carrier;
    }
}