package cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.dto;

import lombok.Data;

@Data
public class DamageInfoDto {

    private Long id;

    private String name;

    private int price;

    private IncidentInfoDto incidentDto;
}
