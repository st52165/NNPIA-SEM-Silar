package cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.service;

import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.dto.IncidentDto;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.dto.IncidentInfoDto;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.exception.NotFoundException;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.model.Incident;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.model.IncidentType;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.repository.IncidentRepository;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.repository.UserRepository;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.security.service.UserPrinciple;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.service.conversion.ConversionService;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.service.interfaces.IncidentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

import static cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.service.UserServiceImpl.USER_BY_USERNAME_NOT_FOUND;

@Service
@Transactional
@AllArgsConstructor
public class IncidentServiceImpl implements IncidentService {

    public static final String INCIDENT_ID_NOT_FOUND = "Incident s id: %d nebyl nalezen.";

    private final IncidentRepository incidentRepository;
    private final UserRepository userRepository;
    private final ConversionService conversionService;

    @Override
    public List<Incident> getAll() {
        return incidentRepository.findAll();
    }

    @Override
    public List<IncidentInfoDto> getAllByUser(UserPrinciple user) {
        return getAllByCarrier(userRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new NotFoundException(String.format(USER_BY_USERNAME_NOT_FOUND, user.getUsername())))
                .getCarrier().getName());
    }

    @Override
    public List<IncidentInfoDto> getAllByCarrier(String carrierName) {
        return incidentRepository.findAllByUser_Carrier_Name(carrierName)
                .stream().map(conversionService::toIncidentInfoDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<IncidentInfoDto> getAllByIncidentType(IncidentType incidentType) {
        return incidentRepository.findAllByIncidentType(incidentType).stream()
                .map(conversionService::toIncidentInfoDto)
                .collect(Collectors.toList());
    }

    @Override
    public IncidentInfoDto getIncidentFromID(Long incidentID) throws HttpServerErrorException {
        return conversionService.toIncidentInfoDto(incidentRepository.findById(incidentID).orElseThrow(()
                -> new NotFoundException(String.format(INCIDENT_ID_NOT_FOUND, incidentID))));
    }

    @Override
    public IncidentInfoDto add(UserPrinciple user, IncidentDto incidentDto) {
        Incident newIncident = new Incident();
        newIncident.setUser(userRepository.findByUsernameIgnoreCase(user.getUsername()).orElseThrow(()
                -> new NotFoundException(String.format(USER_BY_USERNAME_NOT_FOUND, user.getUsername()))));

        return conversionService.toIncidentInfoDto(incidentRepository
                .save(conversionService.toIncident(incidentDto, newIncident)));
    }

    @Override
    public IncidentInfoDto edit(UserPrinciple user, IncidentDto incidentDto, Long id) {
        Incident updatingIncident = incidentRepository.findById(id).orElseThrow(()
                -> new NotFoundException(String.format(INCIDENT_ID_NOT_FOUND, id)));

        updatingIncident.setUser(userRepository.findByUsernameIgnoreCase(user.getUsername()).orElseThrow(()
                -> new NotFoundException(String.format(USER_BY_USERNAME_NOT_FOUND, user.getUsername()))));

        return conversionService.toIncidentInfoDto(incidentRepository
                .save(conversionService.toIncident(incidentDto, updatingIncident)));
    }

    @Override
    public IncidentInfoDto delete(Long id) {
        IncidentInfoDto deleting = getIncidentFromID(id);
        incidentRepository.deleteById(id);
        return deleting;
    }
}
