package cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.service;

import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.dto.WagonDto;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.dto.WagonInfoDto;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.exception.NotFoundException;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.model.*;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.repository.WagonRepository;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.service.conversion.ConversionService;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.service.interfaces.AuthenticationService;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.service.interfaces.WagonService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class WagonServiceImpl implements WagonService {
    public static final String WAGON_ID_NOT_FOUND = "Železniční vagon s id: '%d' nebyl nalezen.";
    public static final String WAGON_PERMISSION = "Je možné, že nemáte oprávnění spravovat tento vagon.";

    private final AuthenticationService authenticationService;
    private final WagonRepository wagonRepository;
    private final ConversionService conversionService;

    public WagonServiceImpl(AuthenticationService authenticationService, WagonRepository wagonRepository,
                            ConversionService conversionService) {
        this.authenticationService = authenticationService;
        this.wagonRepository = wagonRepository;
        this.conversionService = conversionService;
    }

    @Override
    public List<WagonInfoDto> getWagonsList() {
        return wagonRepository.findAll().stream()
                .map(conversionService::toWagonInfoDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<WagonInfoDto> getWagonsListByWagonType(WagonType wagonType) {
        return wagonRepository.findAllByWagonType(wagonType).stream()
                .map(conversionService::toWagonInfoDto)
                .collect(Collectors.toList());
    }

    @Override
    public WagonInfoDto getWagonFromID(Long wagonID) {
        return conversionService.toWagonInfoDto(wagonRepository.findById(wagonID).orElseThrow(
                () -> new NotFoundException(String.format(WAGON_ID_NOT_FOUND, wagonID))));
    }

    @Override
    public boolean existsByWagonID(Long wagonID) {
        return wagonRepository.existsById(wagonID);
    }

    @Override
    public WagonInfoDto insert(WagonDto wagonRequest) {
        Wagon newWagon = new Wagon();
        newWagon.setCarrier(authenticationService.getLoggedUser().getCarrier());

        return conversionService.toWagonInfoDto(wagonRepository
                .save(conversionService.toWagon(wagonRequest, newWagon)));
    }

    @Override
    public WagonInfoDto update(Long id, WagonDto wagonRequest) {
        Wagon updatingWagon = wagonRepository.findByIdAndCarrier(id,
                authenticationService.getLoggedUser().getCarrier()).orElseThrow(()
                -> new NotFoundException(String.format(WAGON_ID_NOT_FOUND + " " + WAGON_PERMISSION, id)));

        return conversionService.toWagonInfoDto(wagonRepository
                .save(conversionService.toWagon(wagonRequest, updatingWagon)));
    }

    @Override
    public WagonInfoDto delete(Long wagonID) {
        Wagon deletingWagon = wagonRepository.findByIdAndCarrier(wagonID,
                authenticationService.getLoggedUser().getCarrier()).orElseThrow(()
                -> new NotFoundException(String.format(WAGON_ID_NOT_FOUND + " " + WAGON_PERMISSION, wagonID)));

        wagonRepository.delete(deletingWagon);
        return conversionService.toWagonInfoDto(deletingWagon);
    }

    @Override
    public WagonInfoDto addIncident(Long wagonID, Long incidentId) {
        String errorPrompt = "Chyba při přiřazování incidentu k vagonu!";
        return updateIncident(wagonID, incidentId, errorPrompt, true);
    }

    @Override
    public WagonInfoDto deleteIncident(Long wagonID, Long incidentId) {
        String errorPrompt = "Chyba při odebírání incidentu z vagonu!";
        return updateIncident(wagonID, incidentId, errorPrompt, false);
    }

    @Override
    public List<WagonInfoDto> getWagonsByCarrierName(String carrierName) {
        return wagonRepository.findAllByCarrier_Name(carrierName).stream()
                .map(conversionService::toWagonInfoDto).collect(Collectors.toList());
    }

    private WagonInfoDto updateIncident(Long wagonID, Long incidentId, String errorPrompt, boolean addingIncident) {
//        try {
//            Wagon updatingWagon = getWagonFromID(wagonID);
//            Incident updatingIncident = incidentRepository.findById(incidentId).orElseThrow(()
//                    -> new NotFoundException(String.format(INCIDENT_ID_NOT_FOUND, incidentId)));
//
//            if (addingIncident) {
//                updatingIncident.setWagon(updatingWagon);
//            } else if (updatingIncident.getWagon().equals(updatingWagon)) {
//                updatingIncident.setWagon(null);
//            }
//
//            incidentRepository.save(updatingIncident);
//            return updatingWagon;
//        } catch (HttpServerErrorException e) {
//            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST,
//                    errorPrompt + System.lineSeparator() + e.getStatusText());
//        }
        return null;
    }
}
