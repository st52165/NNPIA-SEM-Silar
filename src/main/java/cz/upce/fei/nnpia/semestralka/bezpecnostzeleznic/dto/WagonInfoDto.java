package cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.dto;

import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.model.WagonType;
import lombok.Data;

@Data
public class WagonInfoDto {
    private Long id;

    private WagonType wagonType;

    private Integer length;

    private Integer weight;

    CarrierInfoDto carrierDto;
}
