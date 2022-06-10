package cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.dto;

import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.model.WagonType;
import lombok.Data;

@Data
public class WagonDto {
    private WagonType wagonType;

    private Integer length;

    private Integer weight;

    private Long carrierID;

    private String carrierName;

}
