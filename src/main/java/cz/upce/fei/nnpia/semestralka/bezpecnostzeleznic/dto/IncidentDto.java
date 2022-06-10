package cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.dto;

import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.model.IncidentType;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class IncidentDto {

    private String description;

    private IncidentType incidentType;

    private Boolean isCriminalOffense;

    private Boolean isSolvedByPolice;

    private Double gpsLat;

    private Double gpsLon;

    private ZonedDateTime validityFrom;

    private ZonedDateTime validityTo;

    private Long regionID;

    private Long wagonID;
}
