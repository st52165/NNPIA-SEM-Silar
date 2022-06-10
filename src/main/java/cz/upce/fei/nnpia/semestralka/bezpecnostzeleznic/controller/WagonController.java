package cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.controller;

import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.dto.WagonDto;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.message.response.ResponseMessage;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.model.WagonType;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.security.CurrentUser;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.security.service.UserPrinciple;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.service.interfaces.WagonService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;

@RestController
@RequestMapping("/api/wagons")
public class WagonController {

    private final WagonService wagonService;

    public WagonController(WagonService wagonService) {
        this.wagonService = wagonService;
    }

    @PreAuthorize("hasAnyRole('USER_DS', 'ADMIN_DS', 'ADMIN_SZ')")
    @GetMapping
    public ResponseEntity<?> getWagons() {
        return ResponseEntity.ok(wagonService.getWagonsList());
    }

    @PreAuthorize("hasAnyRole('USER_DS', 'ADMIN_DS', 'ADMIN_SZ')")
    @GetMapping(path = "/type/{name}")
    public ResponseEntity<?> getWagonsByWagonTypeID(@PathVariable("name") final WagonType wagonTypeName) {
        return ResponseEntity.ok(wagonService.getWagonsListByWagonType(wagonTypeName));
    }

    @PreAuthorize("hasAnyRole('ADMIN_DS', 'ADMIN_SZ')")
    @PostMapping
    public ResponseEntity<?> insertWagon(@Validated @RequestBody final WagonDto wagonRequest) {
        try {
            return ResponseEntity.ok(wagonService.insert(wagonRequest));
        } catch (HttpServerErrorException e) {
            return getBadRequestResponseEntity(e);
        }
    }

    @PreAuthorize("hasAnyRole('USER_DS', 'ADMIN_DS', 'ADMIN_SZ')")
    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getWagon(@PathVariable("id") final Long id) {
        try {
            return ResponseEntity.ok(wagonService.getWagonFromID(id));
        } catch (HttpServerErrorException e) {
            return getBadRequestResponseEntity(e);
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN_DS', 'ADMIN_SZ')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateWagon(@PathVariable("id") final Long id, @Validated @RequestBody final WagonDto wagonRequest) {
        try {
            return ResponseEntity.ok(wagonService.update(id, wagonRequest));
        } catch (HttpServerErrorException e) {
            return getBadRequestResponseEntity(e);
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN_DS', 'ADMIN_SZ')")
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deleteWagon(@PathVariable("id") final Long wagonID) {
        try {
            return ResponseEntity.ok(wagonService.delete(wagonID));
        } catch (HttpServerErrorException e) {
            return getBadRequestResponseEntity(e);
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN_DS', 'ADMIN_SZ')")
    @PutMapping(path = "/{id}/incident")
    public ResponseEntity<?> addIncident(@PathVariable("id") final Long id, @RequestBody final Long incidentId) {
        try {
            return ResponseEntity.ok(wagonService.addIncident(id, incidentId));
        } catch (HttpServerErrorException e) {
            return getBadRequestResponseEntity(e);
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN_DS', 'ADMIN_SZ')")
    @DeleteMapping(path = "/{id}/incident")
    public ResponseEntity<?> deleteIncident(@PathVariable("id") final Long id, @RequestBody final Long incidentId) {
        try {
            return ResponseEntity.ok(wagonService.deleteIncident(id, incidentId));
        } catch (HttpServerErrorException e) {
            return getBadRequestResponseEntity(e);
        }
    }

    @PreAuthorize("hasAnyRole('USER_DS', 'ADMIN_DS', 'ADMIN_SZ')")
    @GetMapping(path = "/carrier")
    public ResponseEntity<?> getWagonsByCarrier() {
        return ResponseEntity.ok(wagonService.getWagonsByCarrierId());
    }

    @PreAuthorize("hasAnyRole('USER_DS', 'ADMIN_DS', 'ADMIN_SZ')")
    @GetMapping(path = "/all")
    public ResponseEntity<?> getAllWagons(@CurrentUser final UserPrinciple currentUser) {
        return ResponseEntity.ok(wagonService.getAllWagons(currentUser));
    }

    private static ResponseEntity<?> getBadRequestResponseEntity(HttpServerErrorException e) {
        return ResponseEntity.badRequest().body(new ResponseMessage(e.getStatusCode() + ": " + e.getStatusText()));
    }
}