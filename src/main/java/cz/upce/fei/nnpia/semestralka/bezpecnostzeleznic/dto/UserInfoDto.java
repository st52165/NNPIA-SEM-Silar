package cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.dto;

import lombok.Data;

@Data
public class UserInfoDto {

    private Long id;

    private String firstname;

    private String lastname;

    private String username;

    private String email;

    private String role;

}
