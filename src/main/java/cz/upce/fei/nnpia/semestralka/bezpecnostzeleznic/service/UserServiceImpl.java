package cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.service;

import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.dto.UserInfoDto;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.exception.NotFoundException;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.model.RoleName;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.model.User;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.repository.UserRepository;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.service.conversion.ConversionService;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.service.interfaces.AuthenticationService;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.service.interfaces.UserService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    public static final String USER_BY_ID_NOT_FOUND = "Uživatel s id: %d nebyl nalezen!";
    public static final String USER_BY_USERNAME_NOT_FOUND = "Uživatel s uživatelským jménem '%s' nebyl nalezen!";

    private final UserRepository userRepository;
    private final ConversionService conversionService;
    private final AuthenticationService authenticationService;

    public UserServiceImpl(UserRepository userRepository, ConversionService conversionService, AuthenticationService authenticationService) {
        this.userRepository = userRepository;
        this.conversionService = conversionService;
        this.authenticationService = authenticationService;
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public UserInfoDto getUserByUsername(String username) {
        return conversionService.toUserInfoDto(userRepository.findByUsernameIgnoreCase(username).orElseThrow(()
                -> new NotFoundException(String.format(USER_BY_USERNAME_NOT_FOUND, username))));
    }

    @Override
    public List<String> getUsers() {
        Collection<User> users = userRepository.findAll();
        return users.stream().map(User::getUsername).collect(Collectors.toList());
    }

    @Override
    public boolean isUsernameExist(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public List<UserInfoDto> getUserList() {
        Collection<User> users;
        User loggedUser = authenticationService.getLoggedUser();

        if (loggedUser.getRole().getName().equals(RoleName.ROLE_ADMIN_DS)) {
            users = userRepository.findAll();
        } else {
            users = loggedUser.getCarrier().getUsers();
        }

        return users.stream().map(conversionService::toUserInfoDto).collect(Collectors.toList());
    }

    @Override
    public List<UserInfoDto> getUserListWithSameCarrier() {
        return userRepository.findAllByCarrier(authenticationService.getLoggedUser().getCarrier())
                .stream().map(conversionService::toUserInfoDto).collect(Collectors.toList());
    }

    @Override
    public UserInfoDto deleteUser(Long id) {
        User deletingUser = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(USER_BY_ID_NOT_FOUND, id)));
        userRepository.deleteById(id);
        return conversionService.toUserInfoDto(deletingUser);
    }
}
