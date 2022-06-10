package cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.service.interfaces;

import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.dto.CarrierDto;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.dto.CarrierInfoDto;

import java.util.List;

public interface CarrierService {

    List<CarrierInfoDto> getCarriersList();

    CarrierInfoDto getCarrierFromID(Long carrierID);

    CarrierInfoDto getCarrierByName(String name);

    boolean existsByCarrierID(Long carrierID);

    CarrierInfoDto insert(CarrierDto carrierRequest);

    CarrierInfoDto update(Long id, CarrierDto carrierRequest);

    CarrierInfoDto delete(Long carrierID);
}
