package cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.controller;

import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.service.conversion.ConversionService;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.dto.CarrierDto;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.service.interfaces.CarrierService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/carriers")
public class CarrierController {

    private final ConversionService conversionService;
    private final CarrierService carrierService;

    public CarrierController(ConversionService conversionService, CarrierService carrierService) {
        this.conversionService = conversionService;
        this.carrierService = carrierService;
    }

    @PreAuthorize("hasAnyRole('USER_DS', 'ADMIN_DS', 'ADMIN_SZ')")
    @GetMapping
    public List<CarrierDto> getCarriers() {
        return carrierService.getCarriersList().stream()
                .map(conversionService::toCarrierDto).collect(Collectors.toList());
    }

    @PreAuthorize("hasAnyRole('ADMIN_SZ')")
    @PostMapping
    public CarrierDto insertCarrier(@RequestBody final CarrierDto carrierRequest) {
        return conversionService.toCarrierDto(carrierService.insert(carrierRequest));
    }

    @PreAuthorize("hasAnyRole('USER_DS', 'ADMIN_DS', 'ADMIN_SZ')")
    @GetMapping(path = "/{carrierID}")
    public CarrierDto getCarrier(@PathVariable("carrierID") final Long carrierID) {
        return conversionService.toCarrierDto(carrierService.getCarrierFromID(carrierID));
    }

    @PreAuthorize("hasAnyRole('ADMIN_DS', 'ADMIN_SZ')")
    @PutMapping(path = "/{id}")
    public CarrierDto updateCarrier(@PathVariable("id") final Long id, @RequestBody final CarrierDto carrierRequest) {
        return conversionService.toCarrierDto(carrierService.update(id, carrierRequest));
    }

    @PreAuthorize("hasAnyRole('ADMIN_DS', 'ADMIN_SZ')")
    @DeleteMapping(path = "/{id}")
    public CarrierDto deleteCarrier(@PathVariable("id") final Long id) {
        return conversionService.toCarrierDto(carrierService.delete(id));
    }
}