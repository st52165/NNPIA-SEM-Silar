package cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.service.interfaces;

import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.dto.CarrierDto;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.model.Carrier;
import javassist.NotFoundException;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import java.util.List;

public interface CarrierService {

    List<Carrier> getCarriersList();

    Carrier getCarrierFromID(Long carrierID) throws HttpRequestMethodNotSupportedException;

    Carrier getCarrierByName(String name) throws NotFoundException;

    boolean existsByCarrierID(Long carrierID);

    Carrier insert(CarrierDto carrierRequest) throws HttpRequestMethodNotSupportedException;

    Carrier update(Long id, CarrierDto carrierRequest) throws HttpRequestMethodNotSupportedException;

    Carrier delete(Long carrierID) throws HttpRequestMethodNotSupportedException;
}
