package cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.controller;

import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.dto.EnumNameDto;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.dto.IncidentDto;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.dto.IncidentInfoDto;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.dto.IncidentWagonDto;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.model.IncidentType;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.service.interfaces.IncidentService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/incident")
@AllArgsConstructor
public class IncidentController {

    private final IncidentService incidentService;

    @PreAuthorize("hasAnyRole('ADMIN_DS', 'USER_DS')")
    @GetMapping("/all")
    public List<IncidentInfoDto> getAll() {
        return incidentService.getAllByUser();
    }

    @PreAuthorize("hasAnyRole('ADMIN_DS', 'USER_DS')")
    @GetMapping("/carrier/{carrierName}")
    public List<IncidentInfoDto> getAllByCarrier(@PathVariable("carrierName") String carrierName) {
        return incidentService.getAllByCarrier(carrierName);
    }

    @PreAuthorize("hasAnyRole('USER_DS', 'ADMIN_DS', 'ADMIN_SZ')")
    @GetMapping(path = "/type")
    public List<EnumNameDto> getAllIncidentTypes() {
        return incidentService.getAllIncidentTypes();
    }

    @PreAuthorize("hasAnyRole('ADMIN_DS', 'USER_DS')")
    @GetMapping("/type/{incidentType}")
    public List<IncidentInfoDto> getAllByIncidentType(@PathVariable("incidentType") IncidentType incidentType) {
        return incidentService.getAllByIncidentType(incidentType);
    }

    @PreAuthorize("hasAnyRole('ADMIN_DS', 'USER_DS')")
    @GetMapping("/{id}")
    public IncidentInfoDto get(@PathVariable("id") final Long id) {
        return incidentService.getIncidentFromID(id);
    }

    @PreAuthorize("hasRole('ADMIN_DS')")
    @PostMapping
    public IncidentInfoDto post(@Validated @RequestBody final IncidentDto incidentDto) {
        return incidentService.add(incidentDto);
    }

    @PreAuthorize("hasRole('ADMIN_DS')")
    @PutMapping("/{id}")
    public IncidentInfoDto put(@Validated @RequestBody final IncidentDto incidentDto,
                               @PathVariable("id") final Long id) {
        return incidentService.edit(incidentDto, id);
    }

    @PreAuthorize("hasRole('ADMIN_DS')")
    @DeleteMapping("/{id}")
    public IncidentInfoDto delete(@PathVariable("id") final Long id) {
        return incidentService.delete(id);
    }


    @PreAuthorize("hasAnyRole('ADMIN_DS')")
    @PostMapping(path = "/{id}/addwagon")
    public IncidentInfoDto addWagon(@PathVariable("id") final Long id,
                                    @RequestBody final IncidentWagonDto incidentWagonDto) {
        return incidentService.addWagon(id, incidentWagonDto);
    }

    @PreAuthorize("hasAnyRole('ADMIN_DS')")
    @PutMapping(path = "/{id}/wagon")
    public IncidentInfoDto updateWagon(@PathVariable("id") final Long id,
                                       @RequestBody final IncidentWagonDto incidentWagonDto) {
        return incidentService.updateWagon(id, incidentWagonDto);
    }

    @PreAuthorize("hasAnyRole('ADMIN_DS')")
    @DeleteMapping(path = "/{id}/removewagon")
    public IncidentInfoDto removeWagon(@PathVariable("id") final Long id,
                                       @RequestBody final IncidentWagonDto incidentWagonDto) {
        return incidentService.removeWagon(id, incidentWagonDto);
    }
}