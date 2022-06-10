package cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.controller;

import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.dto.WagonDto;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.dto.WagonInfoDto;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.model.WagonType;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.service.interfaces.WagonService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wagons")
public class WagonController {

    private final WagonService wagonService;

    public WagonController(WagonService wagonService) {
        this.wagonService = wagonService;
    }

    @PreAuthorize("hasAnyRole('USER_DS', 'ADMIN_DS', 'ADMIN_SZ')")
    @GetMapping
    public List<WagonInfoDto> getWagons() {
        return wagonService.getWagonsList();
    }

    @PreAuthorize("hasAnyRole('USER_DS', 'ADMIN_DS', 'ADMIN_SZ')")
    @GetMapping(path = "/type/{name}")
    public List<WagonInfoDto> getWagonsByWagonTypeID(@PathVariable("name") final WagonType wagonTypeName) {
        return wagonService.getWagonsListByWagonType(wagonTypeName);
    }

    @PreAuthorize("hasAnyRole('USER_DS', 'ADMIN_DS', 'ADMIN_SZ')")
    @GetMapping(path = "/carrier")
    public List<WagonInfoDto> getWagonsByCarrier() {
        return wagonService.getWagonsByCarrierId();
    }

    @PreAuthorize("hasAnyRole('ADMIN_DS', 'ADMIN_SZ')")
    @PostMapping
    public WagonInfoDto insertWagon(@Validated @RequestBody final WagonDto wagonRequest) {
        return wagonService.insert(wagonRequest);
    }

    @PreAuthorize("hasAnyRole('USER_DS', 'ADMIN_DS', 'ADMIN_SZ')")
    @GetMapping(path = "/{id}")
    public WagonInfoDto getWagon(@PathVariable("id") final Long id) {
        return wagonService.getWagonFromID(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN_DS', 'ADMIN_SZ')")
    @PutMapping("/{id}")
    public WagonInfoDto updateWagon(@PathVariable("id") final Long id, @Validated @RequestBody final WagonDto wagonRequest) {
        return wagonService.update(id, wagonRequest);
    }

    @PreAuthorize("hasAnyRole('ADMIN_DS', 'ADMIN_SZ')")
    @DeleteMapping(path = "/{id}")
    public WagonInfoDto deleteWagon(@PathVariable("id") final Long wagonID) {
        return wagonService.delete(wagonID);
    }

    @PreAuthorize("hasAnyRole('ADMIN_DS', 'ADMIN_SZ')")
    @PutMapping(path = "/{id}/incident")
    public WagonInfoDto addIncident(@PathVariable("id") final Long id, @RequestBody final Long incidentId) {
        return wagonService.addIncident(id, incidentId);
    }

    @PreAuthorize("hasAnyRole('ADMIN_DS', 'ADMIN_SZ')")
    @DeleteMapping(path = "/{id}/incident")
    public WagonInfoDto deleteIncident(@PathVariable("id") final Long id, @RequestBody final Long incidentId) {
        return wagonService.deleteIncident(id, incidentId);
    }

//    @PreAuthorize("hasAnyRole('USER_DS', 'ADMIN_DS', 'ADMIN_SZ')")
//    @GetMapping(path = "/all")
//    public ResponseEntity<?> getAllWagons(@CurrentUser final UserPrinciple currentUser) {
//        return ResponseEntity.ok(wagonService.getAllWagons(currentUser));
//    }
}