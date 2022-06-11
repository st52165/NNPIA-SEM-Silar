package cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.service;

import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.dto.RegionDto;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.dto.RegionInfoDto;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.exception.NotFoundException;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.model.Region;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.repository.RegionRepository;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.service.conversion.ConversionService;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.service.interfaces.RegionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RegionServiceImpl implements RegionService {

    public static final String REGION_ID_NOT_FOUND = "Region s id: '%d' nebyl nalezen.";
    public static final String REGION_NAME_NOT_FOUND = "Region s názvem: '%s' nebyl nalezen.";

    private final RegionRepository regionRepository;
    private final ConversionService conversionService;

    public RegionServiceImpl(RegionRepository regionRepository,
                             ConversionService conversionService) {
        this.regionRepository = regionRepository;
        this.conversionService = conversionService;
    }

    @Override
    public List<RegionInfoDto> getAll() {
        return regionRepository.findAll().stream()
                .map(conversionService::toRegionInfoDto)
                .collect(Collectors.toList());
    }

    @Override
    public RegionInfoDto get(Long id) {
        return conversionService.toRegionInfoDto(getRegionFromID(id));
    }

    @Override
    public RegionInfoDto getByName(String regionName) {
        return conversionService.toRegionInfoDto(regionRepository.findByNameIgnoreCase(regionName)
                .orElseThrow(() -> new NotFoundException(String.format(REGION_NAME_NOT_FOUND, regionName))));
    }

    @Override
    public RegionInfoDto add(RegionDto regionDto) {
        return conversionService.toRegionInfoDto(regionRepository
                .save(conversionService.toRegion(regionDto, null)));
    }

    @Override
    public RegionInfoDto edit(RegionDto regionDto, Long id) {
        return conversionService.toRegionInfoDto(regionRepository
                .save(conversionService.toRegion(regionDto, getRegionFromID(id))));
    }

    @Override
    public RegionInfoDto delete(Long id) {
        Region deletingRegion = getRegionFromID(id);

        regionRepository.delete(deletingRegion);
        return conversionService.toRegionInfoDto(deletingRegion);
    }


    private Region getRegionFromID(Long id) {
        return regionRepository.findById(id).orElseThrow(()
                -> new NotFoundException(String.format(REGION_ID_NOT_FOUND, id)));
    }
}
