package cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.controller;

import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.dto.DamageDto;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.dto.DamageInfoDto;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.service.interfaces.DamageService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/damage")
public class DamageController {

    private final DamageService damageService;

    public DamageController(DamageService damageService) {
        this.damageService = damageService;
    }

    @PreAuthorize("hasAnyRole('ADMIN_SZ', 'ADMIN_DS', 'USER_DS')")
    @GetMapping("")
    public List<DamageInfoDto> getAll() {
        return damageService.getAll();
    }

    @PreAuthorize("hasRole('ADMIN_DS')")
    @GetMapping("/{id}")
    public DamageInfoDto get(@PathVariable("id") final Long id) {
        return damageService.get(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN_SZ', 'ADMIN_DS', 'USER_DS')")
    @GetMapping("/incident/{id}")
    public List<DamageInfoDto> getByIncident(@PathVariable("id") final Long id) {
        return damageService.getByIncident(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN_SZ', 'ADMIN_DS', 'USER_DS')")
    @PostMapping("")
    public DamageInfoDto post(@Validated @RequestBody final DamageDto damageDto) {
        return damageService.add(damageDto);
    }

    @PreAuthorize("hasRole('ADMIN_DS')")
    @PutMapping("/{id}")
    public DamageInfoDto put(@PathVariable("id") final Long id, @Validated @RequestBody final DamageDto damageDto) {
        return damageService.edit(damageDto, id);
    }

    @PreAuthorize("hasRole('ADMIN_DS')")
    @DeleteMapping("/{id}")
    public DamageInfoDto delete(@PathVariable("id") final Long id) {
        return damageService.delete(id);
    }
}
