package cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.dto;

import lombok.Data;

@Data
public class UserEditDto {

    private Long id;

    private String firstname;

    private String lastname;

    private String email;
}
