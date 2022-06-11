package cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.service.interfaces;

import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.dto.RegionDto;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.dto.RegionInfoDto;

import java.util.List;

public interface RegionService {

    List<RegionInfoDto> getAll();

    RegionInfoDto get(Long id);

    RegionInfoDto getByName(String regionName);

    RegionInfoDto add(RegionDto regionDto);

    RegionInfoDto edit(RegionDto regionDto, Long id);

    RegionInfoDto delete(Long id);
}
