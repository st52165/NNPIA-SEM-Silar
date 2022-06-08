package cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserDto {

    private Long id;

    private String firstname;

    private String lastname;

    private String username;

    private String email;

    private String password;

    private List<String> roles;

    private String carrier;

}
