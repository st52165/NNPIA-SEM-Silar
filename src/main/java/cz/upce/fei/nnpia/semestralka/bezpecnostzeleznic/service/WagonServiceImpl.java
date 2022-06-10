package cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.service;

import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.dto.WagonDto;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.exception.NotFoundException;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.model.*;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.repository.CarrierRepository;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.repository.IncidentRepository;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.repository.WagonRepository;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.security.service.UserPrinciple;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.service.interfaces.AuthenticationService;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.service.interfaces.IncidentService;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.service.interfaces.WagonService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpServerErrorException;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.service.IncidentServiceImpl.INCIDENT_ID_NOT_FOUND;

@Service
@Transactional
public class WagonServiceImpl implements WagonService {

    private final AuthenticationService authenticationService;
    private final WagonRepository wagonRepository;
    private final IncidentRepository incidentRepository;
    private final IncidentService incidentService;
    private final CarrierRepository carrierRepository;

    public WagonServiceImpl(AuthenticationService authenticationService, WagonRepository wagonRepository,
                            IncidentRepository incidentRepository, IncidentService incidentService, CarrierRepository carrierRepository) {
        this.authenticationService = authenticationService;
        this.wagonRepository = wagonRepository;
        this.incidentRepository = incidentRepository;
        this.incidentService = incidentService;
        this.carrierRepository = carrierRepository;
    }

    @Override
    public List<Wagon> getWagonsList() {
        return getWagonsListResponseEntity(wagonRepository.findAll());
    }

    @Override
    public List<Wagon> getWagonsListByWagonType(WagonType wagonType) {
        return getWagonsListResponseEntity(wagonRepository.findAllByWagonType(wagonType));
    }

    @Override
    public Wagon getWagonFromID(Long wagonID) throws HttpServerErrorException {
        Optional<Wagon> requestWagon = wagonRepository.findById(wagonID);
        if (requestWagon.isEmpty()) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST,
                    "Vagon s id: '" + wagonID + "' nebyl nalezen.");
        }
        return requestWagon.get();
    }

    @Override
    public boolean existsByWagonID(Long wagonID) {
        return wagonRepository.existsById(wagonID);
    }

    @Override
    public Wagon insert(WagonDto wagonRequest) throws HttpServerErrorException {
        String errorPrompt = "Chyba při vkládání nového vagonu!";
        try {
            return updateWagonEntity(new Wagon(), wagonRequest);
        } catch (HttpServerErrorException e) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST,
                    errorPrompt + System.lineSeparator() + e.getStatusText());
        }
    }

    @Override
    public Wagon update(Long id, WagonDto wagonRequest) throws HttpServerErrorException {
        String errorPrompt = "Chyba při upravování vagonu!";
        try {
            Wagon requestWagon = getWagonFromID(id);

            return updateWagonEntity(requestWagon, wagonRequest);
        } catch (HttpServerErrorException e) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST,
                    errorPrompt + System.lineSeparator() + e.getStatusText());
        }
    }

    @Override
    public Wagon delete(Long wagonID) throws HttpServerErrorException {
        String errorPrompt = "Chyba při odstraňování vagonu!";
        try {
            Wagon requestWagon = getWagonFromID(wagonID);
            wagonRepository.delete(requestWagon);
            return requestWagon;
        } catch (HttpServerErrorException e) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST,
                    errorPrompt + System.lineSeparator() + e.getStatusText());
        }

    }

    @Override
    public Wagon addIncident(Long wagonID, Long incidentId) throws HttpServerErrorException {
        String errorPrompt = "Chyba při přiřazování incidentu k vagonu!";
        return updateIncident(wagonID, incidentId, errorPrompt, true);
    }

    @Override
    public Wagon deleteIncident(Long wagonID, Long incidentId) throws HttpServerErrorException {
        String errorPrompt = "Chyba při odebírání incidentu z vagonu!";
        return updateIncident(wagonID, incidentId, errorPrompt, false);
    }

    @Override
    public List<Wagon> getWagonsByCarrierId() {
        return wagonRepository.findAllByCarrier(authenticationService.getLoggedUser().getCarrier());
    }

    @Override
    public List<Wagon> getAllWagons(UserPrinciple currentUser) {
        List<Wagon> wagons;
        if (currentUser.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN_SZ"))) {
            wagons = wagonRepository.findAll();
        } else {
            wagons = getWagonsByCarrierId();
        }

        return wagons;
    }

    private Wagon updateIncident(Long wagonID, Long incidentId, String errorPrompt, boolean addingIncident)
            throws HttpServerErrorException {
        try {
            Wagon updatingWagon = getWagonFromID(wagonID);
            Incident updatingIncident = incidentRepository.findById(incidentId).orElseThrow(()
                    -> new NotFoundException(String.format(INCIDENT_ID_NOT_FOUND, incidentId)));

            if (addingIncident) {
                updatingIncident.setWagon(updatingWagon);
            } else if (updatingIncident.getWagon().equals(updatingWagon)) {
                updatingIncident.setWagon(null);
            }

            incidentRepository.save(updatingIncident);
            return updatingWagon;
        } catch (HttpServerErrorException e) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST,
                    errorPrompt + System.lineSeparator() + e.getStatusText());
        }
    }

    private Wagon updateWagonEntity(Wagon updatingEntity, WagonDto wagonRequest)
            throws HttpServerErrorException {
        try {
            if (wagonRequest.getWagonType() != null) {
                updatingEntity.setWagonType(wagonRequest.getWagonType());
            }
            if (isNotNullOrEmpty(wagonRequest.getLength())) {
                updatingEntity.setLength(wagonRequest.getLength());
            }
            if (isNotNullOrEmpty(wagonRequest.getWeight())) {
                updatingEntity.setWeight(wagonRequest.getWeight());
            }
            if (isNotNullOrEmpty(wagonRequest.getCarrierID())) {
                updatingEntity.setCarrier(carrierRepository.findById(wagonRequest.getCarrierID()).orElseThrow());
            } else if (wagonRequest.getCarrierName() != null && !StringUtils.containsWhitespace(wagonRequest.getCarrierName())) {
                updatingEntity.setCarrier(carrierRepository.findByName(wagonRequest.getCarrierName()).orElseThrow());
            }

            wagonRepository.save(updatingEntity);
            return updatingEntity;
        } catch (Exception e) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    private List<Wagon> getWagonsListResponseEntity(List<Wagon> wagons) {
        try {
            return wagons;
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    private static boolean isNotNullOrEmpty(Long l) {
        return l != null && l != -1;
    }

    private static boolean isNotNullOrEmpty(Integer i) {
        return i != null && i != -1;
    }
}
