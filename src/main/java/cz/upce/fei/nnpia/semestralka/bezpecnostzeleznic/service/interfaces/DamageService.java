package cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.service.interfaces;

import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.dto.DamageDto;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.dto.DamageInfoDto;

import java.util.List;

public interface DamageService {

    List<DamageInfoDto> getAll();

    DamageInfoDto get(Long id);

    DamageInfoDto add(DamageDto damageDto);

    DamageInfoDto edit(DamageDto damageDto, Long id);

    DamageInfoDto delete(Long id);

    List<DamageInfoDto> getByIncident(Long id);
}
