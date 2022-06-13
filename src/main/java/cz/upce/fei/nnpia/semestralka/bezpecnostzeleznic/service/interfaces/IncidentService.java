package cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.service.interfaces;

import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.dto.EnumNameDto;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.dto.IncidentDto;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.dto.IncidentInfoDto;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.dto.IncidentWagonDto;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.model.IncidentType;

import java.util.List;

public interface IncidentService {

    List<IncidentInfoDto> getAll();

    List<IncidentInfoDto> getAllByUser();

    List<IncidentInfoDto> getAllByCarrier(String carrierName);

    List<IncidentInfoDto> getAllByIncidentType(IncidentType incidentType);

    IncidentInfoDto getIncidentFromID(Long incidentID);

    IncidentInfoDto add(IncidentDto incidentDto);

    IncidentInfoDto edit(IncidentDto incidentDto, Long id);

    IncidentInfoDto delete(Long id);

    IncidentInfoDto addWagon(Long id, IncidentWagonDto incidentWagonDto);

    IncidentInfoDto updateWagon(Long id, IncidentWagonDto incidentWagonDto);

    IncidentInfoDto removeWagon(Long id, IncidentWagonDto incidentWagonDto);

    List<EnumNameDto> getAllIncidentTypes();
}
