package cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.model;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class User {

    private Long id;
    private String firstname;
    private String lastname;
    private String username;
    private String email;
    private String password;
    private Set<Role> roles = new HashSet<>();
    private Carrier carrier;

    public User() {
    }

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