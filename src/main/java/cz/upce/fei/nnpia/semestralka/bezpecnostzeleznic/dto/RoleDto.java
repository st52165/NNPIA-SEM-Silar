package cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.dto;

import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.model.RoleName;
import lombok.Data;

@Data
public class RoleDto {

    private Long id;

    private RoleName roleName;
}
