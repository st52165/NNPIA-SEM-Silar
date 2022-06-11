package cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.controller;

import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.dto.RegionDto;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.dto.RegionInfoDto;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.service.interfaces.RegionService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/region")
public class RegionController {

    private final RegionService regionService;

    public RegionController(RegionService regionService) {
        this.regionService = regionService;
    }

    @PreAuthorize("hasAnyRole('ADMIN_DS', 'USER_DS')")
    @GetMapping("")
    public List<RegionInfoDto> getAll() {
        return regionService.getAll();
    }

    @PreAuthorize("hasRole('ADMIN_DS')")
    @GetMapping("/{id}")
    public RegionInfoDto get(@PathVariable("id") final Long id) {
        return regionService.get(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN_DS', 'USER_DS')")
    @GetMapping("/name/{name}")
    public RegionInfoDto getByName(@PathVariable("name") final String regionName) {
        return regionService.getByName(regionName);
    }

    @PreAuthorize("hasAnyRole('ADMIN_DS', 'USER_DS')")
    @PostMapping("")
    public RegionInfoDto post(@RequestBody final RegionDto regionDto) {
        return regionService.add(regionDto);
    }

    @PreAuthorize("hasRole('ADMIN_DS')")
    @PutMapping("/{id}")
    public RegionInfoDto put(@PathVariable("id") final Long id, @RequestBody final RegionDto regionDto) {
        return regionService.edit(regionDto, id);
    }

    @PreAuthorize("hasRole('ADMIN_DS')")
    @DeleteMapping("/{id}")
    public RegionInfoDto delete(@PathVariable("id") final Long id) {
        return regionService.delete(id);
    }
}
