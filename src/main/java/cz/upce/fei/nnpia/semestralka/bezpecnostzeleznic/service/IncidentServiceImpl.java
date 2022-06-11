package cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.service;

import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.dto.IncidentDto;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.dto.IncidentInfoDto;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.dto.IncidentWagonDto;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.exception.NotFoundException;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.model.Incident;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.model.IncidentType;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.repository.IncidentRepository;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.service.conversion.ConversionAction;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.service.conversion.ConversionService;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.service.interfaces.AuthenticationService;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.service.interfaces.IncidentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class IncidentServiceImpl implements IncidentService {

    public static final String INCIDENT_ID_NOT_FOUND = "Incident s id: %d nebyl nalezen.";

    public static final String INCIDENT_PERMISSION = "Je možné, že nemáte oprávnění spravovat tento incident.";

    private final IncidentRepository incidentRepository;
    private final ConversionService conversionService;

    private final AuthenticationService authenticationService;

    @Override
    public List<Incident> getAll() {
        return incidentRepository.findAll();
    }

    @Override
    public List<IncidentInfoDto> getAllByUser() {
        return getAllByCarrier(authenticationService.getLoggedUser().getCarrier().getName());
    }

    @Override
    public List<IncidentInfoDto> getAllByCarrier(String carrierName) {
        return incidentRepository.findAllByUser_Carrier_Name(carrierName).stream().map(conversionService::toIncidentInfoDto).collect(Collectors.toList());
    }

    @Override
    public List<IncidentInfoDto> getAllByIncidentType(IncidentType incidentType) {
        return incidentRepository.findAllByIncidentType(incidentType).stream().map(conversionService::toIncidentInfoDto).collect(Collectors.toList());
    }

    @Override
    public IncidentInfoDto getIncidentFromID(Long incidentID) throws HttpServerErrorException {
        return conversionService.toIncidentInfoDto(incidentRepository.findById(incidentID).orElseThrow(()
                -> new NotFoundException(String.format(INCIDENT_ID_NOT_FOUND, incidentID))));
    }

    @Override
    public IncidentInfoDto add(IncidentDto incidentDto) {
        return conversionService.toIncidentInfoDto(incidentRepository
                .save(conversionService.toIncident(incidentDto, null)));
    }

    @Override
    public IncidentInfoDto edit(IncidentDto incidentDto, Long id) {
        Incident updatingIncident = getIncidentFromIDAndLoggedUserCarrier(id);

        updatingIncident.setUser(authenticationService.getLoggedUser());

        return conversionService.toIncidentInfoDto(incidentRepository
                .save(conversionService.toIncident(incidentDto, updatingIncident)));
    }

    @Override
    public IncidentInfoDto delete(Long id) {
        Incident deletingIncident = getIncidentFromIDAndLoggedUserCarrier(id);

        incidentRepository.delete(deletingIncident);
        return conversionService.toIncidentInfoDto(deletingIncident);
    }

    @Override
    public IncidentInfoDto addWagon(Long id, IncidentWagonDto incidentWagonDto) {
        return conversionService.toIncidentInfoDto(incidentRepository.save(conversionService.toIncident(
                incidentWagonDto, getIncidentFromIDAndLoggedUserCarrier(id), ConversionAction.ADD)));
    }

    @Override
    public IncidentInfoDto updateWagon(Long id, IncidentWagonDto incidentWagonDto) {
        return conversionService.toIncidentInfoDto(incidentRepository.save(conversionService.toIncident(
                incidentWagonDto, getIncidentFromIDAndLoggedUserCarrier(id), ConversionAction.UPDATE)));
    }

    @Override
    public IncidentInfoDto removeWagon(Long id, IncidentWagonDto incidentWagonDto) {
        return conversionService.toIncidentInfoDto(incidentRepository.save(conversionService.toIncident(
                incidentWagonDto, getIncidentFromIDAndLoggedUserCarrier(id), ConversionAction.REMOVE)));
    }


    private Incident getIncidentFromIDAndLoggedUserCarrier(Long id) {
        return incidentRepository.findByIdAndUser_Carrier(id,
                authenticationService.getLoggedUser().getCarrier()).orElseThrow(()
                -> new NotFoundException(String.format(INCIDENT_ID_NOT_FOUND + " " + INCIDENT_PERMISSION, id)));
    }
}
