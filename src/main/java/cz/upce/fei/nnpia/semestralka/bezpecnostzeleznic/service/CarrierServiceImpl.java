package cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.service;

import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.dto.CarrierInfoDto;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.service.conversion.ConversionService;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.dto.CarrierDto;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.exception.NotFoundException;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.model.Carrier;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.repository.CarrierRepository;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.service.interfaces.CarrierService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarrierServiceImpl implements CarrierService {

    public static final String CARRIER_ID_NOT_FOUND = "Železniční dopravce s id: '%d' nebyl nalezen.";
    public static final String CARRIER_NAME_NOT_FOUND = "Železniční dopravce s názvem '%s' nebyl nalezen!";

    private final CarrierRepository carrierRepository;
    private final ConversionService conversionService;

    public CarrierServiceImpl(CarrierRepository carrierRepository, ConversionService conversionService) {
        this.carrierRepository = carrierRepository;
        this.conversionService = conversionService;
    }

    @Override
    public List<CarrierInfoDto> getCarriersList() {
        return carrierRepository.findAll().stream()
                .map(conversionService::toCarrierInfoDto)
                .collect(Collectors.toList());
    }

    @Override
    public CarrierInfoDto getCarrierFromID(Long carrierID) {
        return conversionService.toCarrierInfoDto(carrierRepository.findById(carrierID).orElseThrow(()
                -> new NotFoundException(String.format(CARRIER_ID_NOT_FOUND, carrierID))));
    }

    @Override
    public CarrierInfoDto getCarrierByName(String name) {
        return conversionService.toCarrierInfoDto(carrierRepository.findByName(name).orElseThrow(()
                -> new NotFoundException(String.format(CARRIER_NAME_NOT_FOUND, name))));
    }

    @Override
    public boolean existsByCarrierID(Long carrierID) {
        return carrierRepository.existsById(carrierID);
    }

    @Override
    public CarrierInfoDto insert(CarrierDto carrierRequest) {
        return conversionService.toCarrierInfoDto(carrierRepository
                .save(conversionService.toCarrier(carrierRequest, null)));
    }

    @Override
    public CarrierInfoDto update(Long carrierID, CarrierDto carrierRequest) {
        Carrier requestCarrier = carrierRepository.findById(carrierID).orElseThrow(()
                -> new NotFoundException(String.format(CARRIER_ID_NOT_FOUND, carrierID)));

        return conversionService.toCarrierInfoDto(carrierRepository
                .save(conversionService.toCarrier(carrierRequest, requestCarrier)));
    }

    @Override
    public CarrierInfoDto delete(Long carrierID) {
        CarrierInfoDto requestCarrier = getCarrierFromID(carrierID);
        carrierRepository.deleteById(carrierID);
        return requestCarrier;
    }
}
