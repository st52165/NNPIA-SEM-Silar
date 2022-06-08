package cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.controller;

import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.dto.CarrierDto;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.message.response.ResponseMessage;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.service.interfaces.CarrierService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carriers")
public class CarrierController {

    private final CarrierService carrierService;

    public CarrierController(CarrierService carrierService) {
        this.carrierService = carrierService;
    }

    @PreAuthorize("hasAnyRole('USER_DS', 'ADMIN_DS', 'ADMIN_SZ')")
    @GetMapping
    public ResponseEntity<?> getCarriers() {
        return ResponseEntity.ok(carrierService.getCarriersList());
    }

    @PreAuthorize("hasAnyRole('ADMIN_SZ')")
    @PostMapping
    public ResponseEntity<?> insertCarrier(@RequestBody final CarrierDto carrierRequest) {
        try {
            return ResponseEntity.ok(carrierService.insert(carrierRequest));
        } catch (HttpRequestMethodNotSupportedException e) {
            return getBadRequestResponseEntity(e.getMessage());
        }
    }

    @PreAuthorize("hasAnyRole('USER_DS', 'ADMIN_DS', 'ADMIN_SZ')")
    @GetMapping(path = "/{carrierID}")
    public ResponseEntity<?> getCarrier(@PathVariable("carrierID") final Long carrierID) {
        try {
            return ResponseEntity.ok(carrierService.getCarrierFromID(carrierID));
        } catch (HttpRequestMethodNotSupportedException e) {
            return getBadRequestResponseEntity(e.getMessage());
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN_DS', 'ADMIN_SZ')")
    @PutMapping(path = "/{id}")
    public ResponseEntity<?> updateCarrier(@PathVariable("id") final Long id, @RequestBody final CarrierDto carrierRequest) {
        try {
            return ResponseEntity.ok(carrierService.update(id, carrierRequest));
        } catch (HttpRequestMethodNotSupportedException e) {
            return getBadRequestResponseEntity(e.getMessage());
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN_DS', 'ADMIN_SZ')")
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deleteCarrier(@PathVariable("id") final Long id) {
        try {
            return ResponseEntity.ok(carrierService.delete(id));
        } catch (HttpRequestMethodNotSupportedException e) {
            return getBadRequestResponseEntity(e.getMessage());
        }
    }


    private static ResponseEntity<?> getBadRequestResponseEntity(String message) {
        return ResponseEntity.badRequest().body(new ResponseMessage(message));
    }
}
