package cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.service;

import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.dto.CarrierDto;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.model.Carrier;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.repository.CarrierRepository;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.service.interfaces.CarrierService;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class CarrierServiceImpl implements CarrierService {

    private final CarrierRepository carrierRepository;

    public CarrierServiceImpl(CarrierRepository carrierRepository) {
        this.carrierRepository = carrierRepository;
    }

    @Override
    public List<Carrier> getCarriersList() {
        try {
            return carrierRepository.findAll();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    @Override
    public Carrier getCarrierFromID(Long carrierID) throws HttpRequestMethodNotSupportedException {
        Optional<Carrier> requestCarrier = carrierRepository.findById(carrierID);
        if (requestCarrier.isEmpty()) {
            throw new HttpRequestMethodNotSupportedException("Železniční dopravce s id: '" + carrierID + "' nebyl nalezen.");
        }
        return requestCarrier.get();
    }

    @Override
    public Carrier getCarrierByName(String name) throws NotFoundException {
        Optional<Carrier> foundCarrier = carrierRepository.findByName(name);
        if (foundCarrier.isPresent()) {
            return foundCarrier.get();
        }

        throw new NotFoundException("Společnost s název " + name + " nebyla nalezena!");
    }

    @Override
    public boolean existsByCarrierID(Long carrierID) {
        return carrierRepository.existsById(carrierID);
    }

    @Override
    public Carrier insert(CarrierDto carrierRequest) throws HttpRequestMethodNotSupportedException {
        String errorPrompt = "Chyba při vkládání nového železničního dopravce!";
        try {
            return updateCarrierEntity(new Carrier(), carrierRequest);
        } catch (HttpRequestMethodNotSupportedException e) {
            throw new HttpRequestMethodNotSupportedException(errorPrompt + System.lineSeparator() + e.getMessage());
        }
    }

    @Override
    public Carrier update(Long id, CarrierDto carrierRequest) throws HttpRequestMethodNotSupportedException {
        String errorPrompt = "Chyba při upravování železničního dopravce!";
        try {
            Carrier requestCarrier = getCarrierFromID(id);
            return updateCarrierEntity(requestCarrier, carrierRequest);
        } catch (HttpRequestMethodNotSupportedException e) {
            throw new HttpRequestMethodNotSupportedException(errorPrompt + System.lineSeparator() + e.getMessage());
        }
    }

    @Override
    public Carrier delete(Long carrierID) throws HttpRequestMethodNotSupportedException {
        String errorPrompt = "Chyba při odstraňování železničního dopravce!";
        try {
            Carrier requestCarrier = getCarrierFromID(carrierID);
            carrierRepository.delete(requestCarrier);
            return requestCarrier;
        } catch (HttpRequestMethodNotSupportedException e) {
            throw new HttpRequestMethodNotSupportedException(errorPrompt + System.lineSeparator() + e.getMessage());
        }

    }

    private Carrier updateCarrierEntity(Carrier updatingEntity, CarrierDto carrierRequest)
            throws HttpRequestMethodNotSupportedException {
        try {
            String entityName = carrierRequest.getName();
            if (!isNullOrEmpty(entityName) && !entityName.equals(updatingEntity.getName())) {
                updatingEntity.setName(entityName);
            }
            carrierRepository.save(updatingEntity);
            return updatingEntity;
        } catch (Exception e) {
            throw new HttpRequestMethodNotSupportedException(e.getMessage());
        }
    }

    private static boolean isNullOrEmpty(String s) {
        return s == null || s.length() == 0;
    }

}
