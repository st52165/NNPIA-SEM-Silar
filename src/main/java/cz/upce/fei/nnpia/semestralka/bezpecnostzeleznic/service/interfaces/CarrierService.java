package cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.service.interfaces;

import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.dto.CarrierDto;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.model.Carrier;

import java.util.List;

public interface CarrierService {

    List<Carrier> getCarriersList();

    Carrier getCarrierFromID(Long carrierID);

    Carrier getCarrierByName(String name);

    boolean existsByCarrierID(Long carrierID);

    Carrier insert(CarrierDto carrierRequest);

    Carrier update(Long id, CarrierDto carrierRequest);

    Carrier delete(Long carrierID);
}
