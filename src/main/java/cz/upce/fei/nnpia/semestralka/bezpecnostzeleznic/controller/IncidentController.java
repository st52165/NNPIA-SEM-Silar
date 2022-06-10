package cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.controller;

import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.dto.IncidentDto;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.dto.IncidentInfoDto;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.model.IncidentType;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.security.CurrentUser;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.security.service.UserPrinciple;
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
    public List<IncidentInfoDto> getAll(@CurrentUser final UserPrinciple currentUser) {
        return incidentService.getAllByUser(currentUser);
    }

    @PreAuthorize("hasAnyRole('ADMIN_DS', 'USER_DS')")
    @GetMapping("/carrier/{carrierName}")
    public List<IncidentInfoDto> getAllByCarrier(@PathVariable("carrierName") String carrierName) {
        return incidentService.getAllByCarrier(carrierName);
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
    public IncidentInfoDto post(@CurrentUser final UserPrinciple currentUser,
                                @Validated @RequestBody final IncidentDto incidentDto) {
        return incidentService.add(currentUser, incidentDto);
    }

    @PreAuthorize("hasRole('ADMIN_DS')")
    @PutMapping("/{id}")
    public IncidentInfoDto put(@CurrentUser final UserPrinciple currentUser,
                               @Validated @RequestBody final IncidentDto incidentDto,
                               @PathVariable("id") final Long id) {
        return incidentService.edit(currentUser, incidentDto, id);
    }

    @PreAuthorize("hasRole('ADMIN_DS')")
    @DeleteMapping("/{id}")
    public IncidentInfoDto delete(@PathVariable("id") final Long id) {
        return incidentService.delete(id);
    }
}