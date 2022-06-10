package cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.controller;

import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.dto.CarrierInfoDto;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.dto.CarrierDto;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.service.interfaces.CarrierService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carriers")
public class CarrierController {

    private final CarrierService carrierService;

    public CarrierController(CarrierService carrierService) {
        this.carrierService = carrierService;
    }

    @PreAuthorize("hasAnyRole('USER_DS', 'ADMIN_DS', 'ADMIN_SZ')")
    @GetMapping
    public List<CarrierInfoDto> getCarriers() {
        return carrierService.getCarriersList();
    }

    @PreAuthorize("hasAnyRole('ADMIN_SZ')")
    @PostMapping
    public CarrierInfoDto insertCarrier(@RequestBody final CarrierDto carrierRequest) {
        return carrierService.insert(carrierRequest);
    }

    @PreAuthorize("hasAnyRole('USER_DS', 'ADMIN_DS', 'ADMIN_SZ')")
    @GetMapping(path = "/{carrierID}")
    public CarrierInfoDto getCarrier(@PathVariable("carrierID") final Long carrierID) {
        return carrierService.getCarrierFromID(carrierID);
    }

    @PreAuthorize("hasAnyRole('ADMIN_DS', 'ADMIN_SZ')")
    @PutMapping(path = "/{id}")
    public CarrierInfoDto updateCarrier(@PathVariable("id") final Long id, @RequestBody final CarrierDto carrierRequest) {
        return carrierService.update(id, carrierRequest);
    }

    @PreAuthorize("hasAnyRole('ADMIN_DS', 'ADMIN_SZ')")
    @DeleteMapping(path = "/{id}")
    public CarrierInfoDto deleteCarrier(@PathVariable("id") final Long id) {
        return carrierService.delete(id);
    }
}