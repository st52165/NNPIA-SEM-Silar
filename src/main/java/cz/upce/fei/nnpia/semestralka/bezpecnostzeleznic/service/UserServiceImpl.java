package cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.service;

import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.service.conversion.ConversionService;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.dto.*;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.exception.NotFoundException;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.model.Carrier;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.model.Role;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.model.User;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.repository.RoleRepository;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.repository.UserRepository;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.security.service.UserPrinciple;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.service.interfaces.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    public static final String USER_BY_ID_NOT_FOUND = "Uživatel s id: %d nebyl nalezen!";
    public static final String USER_BY_USERNAME_NOT_FOUND = "Uživatel s uživatelským jménem '%s' nebyl nalezen!";
    private static final String USERNAME_NOT_LOAD_MESSAGE = "Nepodařilo se načíst uživatele!";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ConversionService conversionService;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
                           ConversionService conversionService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.conversionService = conversionService;
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
    public void insertUser(User user) {
        userRepository.save(user);
    }

    @Override
    public UserInfoDto getUserResponseByUsername(String username) {
        return conversionService.toUserInfoDto(getUserByUsername(username));
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
    public List<UserDto> getUserList(UserPrinciple currentUser) {
        Collection<User> users;
        User user = userRepository.findByUsername(currentUser.getUsername()).orElseThrow(()
                -> new NotFoundException(USERNAME_NOT_LOAD_MESSAGE));

        if (Objects.isNull(user.getCarrier())) {
            users = userRepository.findAll();
        } else {
            users = user.getCarrier().getUsers();
        }

        return users.stream().map(conversionService::toUserDto).collect(Collectors.toList());
    }

    @Override
    public List<UserDto> getUserListWithSameCarrier(String username) {

        User loggedUser = userRepository.findByUsername(username).orElseThrow(()
                -> new NotFoundException(USERNAME_NOT_LOAD_MESSAGE));

        Carrier carrier = loggedUser.getCarrier();

        List<User> users = userRepository.findAll();

        return users.stream().filter(user -> user.getCarrier() != null && user.getCarrier().equals(carrier))
                .map(conversionService::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto deleteUser(Long id) {
        User deletingUser = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(USER_BY_ID_NOT_FOUND, id)));
        userRepository.deleteById(id);
        return conversionService.toUserDto(deletingUser);
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsernameIgnoreCase(username).orElseThrow(()
                -> new NotFoundException(String.format(USER_BY_USERNAME_NOT_FOUND, username)));
    }
}
