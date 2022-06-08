package cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.dto;

import lombok.Data;

@Data
public class WagonDto {

    private Long wagonTypeID;

    private String wagonTypeName;

    private Integer length;

    private Integer weight;

    private Long carrierID;

    private String carrierName;

}
