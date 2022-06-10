package cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.service;

import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.dto.DamageDto;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.dto.DamageInfoDto;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.exception.NotFoundException;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.model.Damage;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.repository.DamageRepository;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.service.conversion.ConversionService;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.service.interfaces.AuthenticationService;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.service.interfaces.DamageService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DamageServiceImpl implements DamageService {

    public static final String DAMAGE_ID_NOT_FOUND = "Finanční škoda s id: '%d' nebyla nalezena.";

    private final DamageRepository damageRepository;
    private final ConversionService conversionService;
    private final AuthenticationService authenticationService;

    public DamageServiceImpl(DamageRepository damageRepository, ConversionService conversionService, AuthenticationService authenticationService) {
        this.damageRepository = damageRepository;
        this.conversionService = conversionService;
        this.authenticationService = authenticationService;
    }

    @Override
    public List<DamageInfoDto> getAll() {
        return damageRepository.findAll().stream()
                .map(conversionService::toDamageInfoDto)
                .collect(Collectors.toList());
    }

    @Override
    public DamageInfoDto get(Long id) {
        return conversionService.toDamageInfoDto(damageRepository.findById(id).orElseThrow(()
                -> new NotFoundException(String.format(DAMAGE_ID_NOT_FOUND, id))));
    }

    @Override
    public DamageInfoDto add(DamageDto damageDto) {
        return conversionService.toDamageInfoDto(damageRepository
                .save(conversionService.toDamage(damageDto, null)));
    }

    @Override
    public DamageInfoDto edit(DamageDto damageDto, Long id) {
        Damage updatingDamage = damageRepository.findById(id).orElseThrow(()
                -> new NotFoundException(String.format(DAMAGE_ID_NOT_FOUND, id)));

        return conversionService.toDamageInfoDto(damageRepository
                .save(conversionService.toDamage(damageDto, updatingDamage)));
    }

    @Override
    public DamageInfoDto delete(Long id) {
        Damage deletingDamage = damageRepository.findByIdAndIncident_User_Carrier(id,
                authenticationService.getLoggedUser().getCarrier()).orElseThrow(()
                -> new NotFoundException(String.format(DAMAGE_ID_NOT_FOUND, id)));

        damageRepository.delete(deletingDamage);
        return conversionService.toDamageInfoDto(deletingDamage);
    }

    @Override
    public List<DamageInfoDto> getByIncident(Long id) {
        return damageRepository.findAllByIncident_Id(id).stream()
                .map(conversionService::toDamageInfoDto)
                .collect(Collectors.toList());
    }
}
