package cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.service;

import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.service.conversion.ConversionService;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.dto.CarrierDto;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.exception.NotFoundException;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.model.Carrier;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.repository.CarrierRepository;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.service.interfaces.CarrierService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarrierServiceImpl implements CarrierService {

    private final CarrierRepository carrierRepository;
    private final ConversionService conversionService;

    public CarrierServiceImpl(CarrierRepository carrierRepository, ConversionService conversionService) {
        this.carrierRepository = carrierRepository;
        this.conversionService = conversionService;
    }

    @Override
    public List<Carrier> getCarriersList() {
        return carrierRepository.findAll();
    }

    @Override
    public Carrier getCarrierFromID(Long carrierID) {
        return carrierRepository.findById(carrierID).orElseThrow(()
                -> new NotFoundException("Železniční dopravce s id: '" + carrierID + "' nebyl nalezen."));
    }

    @Override
    public Carrier getCarrierByName(String name) {
        return carrierRepository.findByName(name).orElseThrow(()
                -> new NotFoundException("Společnost s názvem '" + name + "' nebyla nalezena!"));
    }

    @Override
    public boolean existsByCarrierID(Long carrierID) {
        return carrierRepository.existsById(carrierID);
    }

    @Override
    public Carrier insert(CarrierDto carrierRequest) {
        return updateCarrierEntity(carrierRequest, null);
    }

    @Override
    public Carrier update(Long id, CarrierDto carrierRequest) {
        Carrier requestCarrier = getCarrierFromID(id);
        return updateCarrierEntity(carrierRequest, requestCarrier);
    }

    @Override
    public Carrier delete(Long carrierID) {
        Carrier requestCarrier = getCarrierFromID(carrierID);
        carrierRepository.delete(requestCarrier);
        return requestCarrier;
    }

    private Carrier updateCarrierEntity(CarrierDto carrierRequest, Carrier updatingEntity) {
        return carrierRepository.save(conversionService.toCarrier(carrierRequest, updatingEntity));
    }
}
