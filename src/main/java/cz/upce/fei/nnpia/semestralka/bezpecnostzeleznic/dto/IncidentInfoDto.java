package cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.dto;

import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.model.IncidentType;
import lombok.Data;
import org.geolatte.geom.Position;

import java.time.ZonedDateTime;

@Data
public class IncidentInfoDto {

    private Long id;

    Position position;

    private ZonedDateTime validityFrom;

    private ZonedDateTime validityTo;

    private String description;

    private boolean isCriminalOffense;

    private boolean isSolvedByPolice;

    private IncidentType incidentType;

    private WagonDto wagonDto;

    UserInfoDto userInfoDto;

    RegionDto regionDto;
}
