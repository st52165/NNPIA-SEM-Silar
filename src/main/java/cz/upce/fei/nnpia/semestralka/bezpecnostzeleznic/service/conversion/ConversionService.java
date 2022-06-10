package cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.service.conversion;

import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.dto.*;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.exception.NotFoundException;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.model.*;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.repository.IncidentRepository;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.repository.RegionRepository;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.repository.WagonRepository;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.service.interfaces.AuthenticationService;
import org.geolatte.geom.crs.CoordinateSystemAxis;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.Objects;

import static cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.service.IncidentServiceImpl.INCIDENT_ID_NOT_FOUND;
import static cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.service.IncidentServiceImpl.INCIDENT_PERMISSION;
import static cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.service.WagonServiceImpl.WAGON_ID_NOT_FOUND;
import static cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.service.WagonServiceImpl.WAGON_PERMISSION;
import static org.geolatte.geom.builder.DSL.g;
import static org.geolatte.geom.builder.DSL.point;
import static org.geolatte.geom.crs.CoordinateReferenceSystems.WGS84;

@Service
public class ConversionService {
    private final ModelMapper modelMapper;
    private final WagonRepository wagonRepository;
    private final RegionRepository regionRepository;
    private final IncidentRepository incidentRepository;
    private final AuthenticationService authenticationService;

    public ConversionService(ModelMapper modelMapper, WagonRepository wagonRepository, RegionRepository regionRepository,
                             IncidentRepository incidentRepository, AuthenticationService authenticationService) {
        this.modelMapper = modelMapper;
        this.wagonRepository = wagonRepository;
        this.regionRepository = regionRepository;
        this.incidentRepository = incidentRepository;
        this.authenticationService = authenticationService;
    }


    public CarrierDto toCarrierDto(Carrier carrier) {
        return modelMapper.map(carrier, CarrierDto.class);
    }

    public CarrierInfoDto toCarrierInfoDto(Carrier carrier) {
        return modelMapper.map(carrier, CarrierInfoDto.class);
    }

    public Carrier toCarrier(CarrierDto carrierDto, Carrier carrier) {
        if (!isNullOrEmpty(carrierDto.getName())) {
            carrier = carrier == null ? new Carrier() : carrier;
            carrier.setName(carrierDto.getName());
        }
        return carrier;
    }

    public UserInfoDto toUserInfoDto(User user) {
        UserInfoDto infoDTO = modelMapper.map(user, UserInfoDto.class);
        infoDTO.setRole(user.getRole().getName().name());
        return infoDTO;
    }

    public UserDto toUserDto(User user) {
        UserDto userDTO = modelMapper.map(user, UserDto.class);
        userDTO.setRole(user.getRole().getName().name());
        userDTO.setCarrier(Objects.isNull(user.getCarrier()) ? null : user.getCarrier().getName());
        return userDTO;
    }

    public IncidentInfoDto toIncidentInfoDto(Incident incident) {
        IncidentInfoDto incidentDto = modelMapper.map(incident, IncidentInfoDto.class);
        incidentDto.setPosition(incident.getPosition().getPositionN(0));
        incidentDto.setWagonDto(toWagonInfoDto(incident.getWagon()));
        incidentDto.setUserInfoDto(toUserInfoDto(incident.getUser()));
        incidentDto.setRegionDto(toRegionDto(incident.getRegion()));
        return incidentDto;
    }

    public Incident toIncident(IncidentDto incidentDto, Incident incident) {
        incident = incident == null ? new Incident() : incident;
        if (incident.getUser() == null) {
            incident.setUser(authenticationService.getLoggedUser());
        }
        if (!isNullOrEmpty(incidentDto.getDescription())) {
            incident.setDescription(incident.getDescription());
        }
        if (incidentDto.getIncidentType() != null) {
            incident.setIncidentType(incidentDto.getIncidentType());
        }
        if (incidentDto.getIsCriminalOffense() != null) {
            incident.setCriminalOffense(incidentDto.getIsCriminalOffense());
        }
        if (incidentDto.getIsSolvedByPolice() != null) {
            incident.setSolvedByPolice(incidentDto.getIsSolvedByPolice());
        }
        if (!isNullOrEmpty(incidentDto.getGpsLat()) && !isNullOrEmpty(incidentDto.getGpsLon())) {
            incident.setPosition(point(WGS84, g(incidentDto.getGpsLon(), incidentDto.getGpsLat())));
        } else if (!isNullOrEmpty(incidentDto.getGpsLat())) {
            double lon = incident.getPosition().getPositionN(0).getCoordinate(CoordinateSystemAxis.mkLonAxis());
            incident.setPosition(point(WGS84, g(lon, incidentDto.getGpsLat())));
        } else if (!isNullOrEmpty(incidentDto.getGpsLon())) {
            double lat = incident.getPosition().getPositionN(0).getCoordinate(CoordinateSystemAxis.mkLatAxis());
            incident.setPosition(point(WGS84, g(incidentDto.getGpsLon(), lat)));
        }
        if (incidentDto.getValidityFrom() != null) {
            incident.setValidityFrom(incidentDto.getValidityFrom().toLocalDateTime().atZone(ZoneId.systemDefault()));
        }
        if (incidentDto.getValidityTo() != null) {
            incident.setValidityTo(incidentDto.getValidityTo().toLocalDateTime().atZone(ZoneId.systemDefault()));
        }
        if (!isNullOrEmpty(incidentDto.getRegionID())) {
            incident.setRegion(regionRepository.findById(incidentDto.getRegionID()).orElseThrow(()
                    -> new NotFoundException("Region s id: '" + incidentDto.getRegionID() + "' nebyl nalezen.")));
        }
        if (!isNullOrEmpty(incidentDto.getWagonID())) {
            incident.setWagon(wagonRepository.findByIdAndCarrier(incidentDto.getWagonID(), incident.getUser().getCarrier())
                    .orElseThrow(() -> new NotFoundException(String.format(WAGON_ID_NOT_FOUND
                            + " " + WAGON_PERMISSION, incidentDto.getWagonID()))));
        }
        return incident;
    }

    public WagonInfoDto toWagonInfoDto(Wagon wagon) {
        WagonInfoDto wagonDto = modelMapper.map(wagon, WagonInfoDto.class);
        wagonDto.setCarrierDto(toCarrierInfoDto(wagon.getCarrier()));
        return wagonDto;
    }

    public Wagon toWagon(WagonDto wagonDto, Wagon wagon) {
        wagon = wagon == null ? new Wagon() : wagon;
        if (wagonDto.getWagonType() != null) {
            wagon.setWagonType(wagonDto.getWagonType());
        }
        if (!isNullOrEmpty(wagonDto.getLength())) {
            wagon.setLength(wagonDto.getLength());
        }
        if (!isNullOrEmpty(wagonDto.getWeight())) {
            wagon.setWeight(wagonDto.getWeight());
        }
        return wagon;
    }

    public DamageInfoDto toDamageInfoDto(Damage damage) {
        DamageInfoDto damageInfoDto = modelMapper.map(damage, DamageInfoDto.class);
        damageInfoDto.setIncidentDto(toIncidentInfoDto(damage.getIncident()));
        return damageInfoDto;
    }

    public Damage toDamage(DamageDto damageDto, Damage damage) {
        damage = damage == null ? new Damage() : damage;

        if (!isNullOrEmpty(damageDto.getName())) {
            damage.setName(damageDto.getName());
        }
        if (!isNullOrEmpty(damageDto.getPrice())) {
            damage.setPrice(damageDto.getPrice());
        }
        if (!isNullOrEmpty(damageDto.getIncidentID())) {
            Incident updating = incidentRepository.findByIdAndUser_Carrier(damageDto.getIncidentID(),
                    authenticationService.getLoggedUser().getCarrier()).orElseThrow(()
                    -> new NotFoundException(String.format(INCIDENT_ID_NOT_FOUND, damageDto.getIncidentID())
                    + " " + INCIDENT_PERMISSION));
            damage.setIncident(updating);
        }
        return damage;
    }

    public RegionDto toRegionDto(Region region) {
        return modelMapper.map(region, RegionDto.class);
    }


    private static boolean isNullOrEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }

    private static boolean isNullOrEmpty(Long l) {
        return l == null || l == -1;
    }

    private static boolean isNullOrEmpty(Integer i) {
        return i == null || i == -1;
    }

    private static boolean isNullOrEmpty(Double d) {
        return d == null || d.isNaN();
    }
}
