package cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.service.interfaces;

import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.dto.WagonDto;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.dto.WagonInfoDto;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.model.WagonType;

import java.util.List;

public interface WagonService {

    List<WagonInfoDto> getWagonsList();

    List<WagonInfoDto> getWagonsListByWagonType(WagonType wagonType);

    WagonInfoDto getWagonFromID(Long wagonID);

    boolean existsByWagonID(Long wagonID);

    WagonInfoDto insert(WagonDto wagonRequest);

    WagonInfoDto update(Long id, WagonDto wagonRequest);

    WagonInfoDto delete(Long wagonID);

    WagonInfoDto addIncident(Long wagonID, Long incidentId);

    WagonInfoDto deleteIncident(Long wagonID, Long incidentId);

    List<WagonInfoDto> getWagonsByCarrierId();
}
