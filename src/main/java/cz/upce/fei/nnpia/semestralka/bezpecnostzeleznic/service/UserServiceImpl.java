package cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.service;

import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.dto.*;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.model.Carrier;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.model.Role;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.model.RoleName;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.model.User;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.repository.RoleRepository;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.repository.UserRepository;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.security.service.UserPrinciple;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.service.interfaces.UserService;
import org.apache.commons.validator.routines.EmailValidator;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
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
    public ResponseEntity<?> getUserResponseByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);

        if (user.isPresent()) {
            UserInfoDto infoDTO = modelMapper.map(user.get(), UserInfoDto.class);
            infoDTO.setRoles(user.get().getRoles().stream().map(role -> role.getName().name()).collect(Collectors.toList()));
            return ResponseEntity.ok(infoDTO);
        }

        return ResponseEntity.badRequest().body("Uživatel s uživatelským jménem " + username + " nebyl nalezen!");
    }

    @Override
    public ResponseEntity<List<String>> getUsers() {
        try {
            Collection<User> users = userRepository.findAll();
            List<String> result = users.stream().map(User::getUsername).collect(Collectors.toList());
            return ResponseEntity.ok(result);
        } catch (Exception ex) {
            return ResponseEntity.ok(Collections.emptyList());
        }
    }

    @Override
    public ResponseEntity<?> isUsernameExist(String username) {
        try {
            boolean result = userRepository.existsByUsername(username);
            return ResponseEntity.ok(result);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body("Neplatný dotaz!");
        }
    }

    @Override
    public ResponseEntity<?> getUserList(UserPrinciple currentUser) {
        try {
            Collection<User> users;
            User user = userRepository.findByUsername(currentUser.getUsername()).orElseThrow();

            if (Objects.isNull(user.getCarrier())) {
                users = userRepository.findAll();
            } else {
                users = user.getCarrier().getUsers();
            }

            List<UserDto> result = users.stream().map(usr -> {
                        UserDto userDTO = modelMapper.map(usr, UserDto.class);
                        userDTO.setRoles(usr.getRoles().stream().map(role -> role.getName().name()).collect(Collectors.toList()));
                        userDTO.setCarrier(Objects.isNull(usr.getCarrier()) ? null : usr.getCarrier().getName());
                        return userDTO;
                    }
            ).collect(Collectors.toList());

            return ResponseEntity.ok(result);
        } catch (
                Exception ex) {
            return ResponseEntity.badRequest().body("Nepodařilo se načíst uživatele!");
        }
    }

    @Override
    public ResponseEntity<?> getUserListWithSameCarrier(String username) {
        try {
            User loggedUser = userRepository.findByUsername(username).orElseThrow();

            Carrier carrier = loggedUser.getCarrier();
            boolean isAdminSZ = false;
            Set<Role> userRoles = loggedUser.getRoles();

            for (Role role : userRoles) {
                if (role.getName().equals(RoleName.ROLE_ADMIN_SZ)) {
                    isAdminSZ = true;
                    break;
                }
            }

            List<User> users = userRepository.findAll();

            if (isAdminSZ) {
                return ResponseEntity.ok(users.stream().map(User::getUsername).collect(Collectors.toList()));

            } else {
                List<String> usersWithSameCarrier = users.stream().
                        filter(user -> user.getCarrier() != null && user.getCarrier().equals(carrier)).
                        map(User::getUsername).
                        collect(Collectors.toList());
                return ResponseEntity.ok(usersWithSameCarrier);
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Neplatný uživatel!");

        }
    }

    @Override
    public ResponseEntity<?> updatePassword(String username, UserPasswordDto passwordRequest) {
        if (passwordRequest.getPassword() != null &&
                passwordRequest.getPassword().length() >= User.minPasswordLength && passwordRequest.getPassword().length() <= User.maxPasswordLength) {

            Optional<User> user = userRepository.findByUsernameIgnoreCase(username);

            if (user.isPresent()) {
                User usr = user.get();
                usr.setPassword(passwordEncoder.encode(passwordRequest.getPassword()));
                userRepository.save(usr);
                return ResponseEntity.ok("Heslo bylo aktualizováno.");
            }
            return ResponseEntity.badRequest().body("Dané uživatelské jméno neexistuje!");
        }
        return ResponseEntity.badRequest().body("Nové heslo nesplňuje platné podmínky!");
    }

    @Override
    public ResponseEntity<?> updateUsername(String username, UserUsernameDto nameRequest) {
        if (nameRequest.getUsername() != null &&
                nameRequest.getUsername().length() >= User.minUsernameLength && nameRequest.getUsername().length() <= User.maxUsernameLength) {
            Optional<User> foundUser = userRepository.findByUsernameIgnoreCase(username);

            if (foundUser.isPresent()) {
                User updatingUser = foundUser.get();

                foundUser = userRepository.findByUsernameIgnoreCase(nameRequest.getUsername());
                if (foundUser.isPresent()) {
                    return ResponseEntity.badRequest().body("Toto uživatelské jméno již existuje!");
                }

                updatingUser.setUsername(nameRequest.getUsername());
                userRepository.save(updatingUser);
                return ResponseEntity.ok("Uživatelské jméno bylo aktualizováno.");
            }
            return ResponseEntity.badRequest().body("Dané uživatelské jméno neexistuje!");
        } else {
            return ResponseEntity.badRequest().body("Nepodařilo se aktualizovat uživatelské jméno!");
        }
    }

    @Override
    public ResponseEntity<?> updateOther(String username, UserEditDto userRequest) {
        if (userRequest.getFirstname() != null && userRequest.getLastname() != null && userRequest.getEmail() != null &&
                userRequest.getFirstname().length() >= User.minNameLength && userRequest.getLastname().length() >= User.minNameLength
                && userRequest.getFirstname().length() <= User.maxNameLength && userRequest.getLastname().length() <= User.maxNameLength
                && EmailValidator.getInstance().isValid(userRequest.getEmail()) && userRequest.getEmail().length() <= User.maxEmailLength) {

            Optional<User> user = userRepository.findByUsernameIgnoreCase(username);

            if (user.isPresent()) {
                User usr = user.get();
                usr.setFirstname(userRequest.getFirstname());
                usr.setLastname(userRequest.getLastname());
                usr.setEmail(userRequest.getEmail());
                userRepository.save(usr);
                return ResponseEntity.ok("Uživatelský učet byl aktualizován.");
            }
            return ResponseEntity.badRequest().body("Dané uživatelské jméno neexistuje!");
        } else {
            return ResponseEntity.badRequest().body("Nepodařilo se aktualizovat uživatelský účet!");
        }
    }

    @Override
    public ResponseEntity<?> deleteUser(Long id) {
        try {
            userRepository.deleteById(id);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("User s id: " + id + " nebyl nalezen!");
        }
        return ResponseEntity.ok("Uživatel byl odstraněn.");
    }

    @Override
    public ResponseEntity<?> addRole(String username, RoleDto roleRequest) {
        User user = userRepository.findByUsername(username).orElseThrow();
        Set<Role> usersRoles = user.getRoles();
        usersRoles.add(createRoleFromRoleDto(roleRequest));
        user.setRoles(usersRoles);
        userRepository.save(user);

        return ResponseEntity.ok("Uživateli byla přidána role: " + roleRequest.getRoleName().name());
    }

    @Override
    public ResponseEntity<?> updateUserRole(final UserPrinciple currentUser, String username, RoleDto roleRequest) {
        Optional<Role> foundRole = roleRepository.findByName(roleRequest.getRoleName());
        if (foundRole.isPresent()) {
            User loggedUser = userRepository.findByUsername(currentUser.getUsername()).orElseThrow();
            User updatingUser = userRepository.findByUsername(username).orElseThrow();

            boolean isAdminSZ = loggedUser.getRoles().contains(roleRepository.findByName(RoleName.ROLE_ADMIN_SZ).orElseThrow());
            if (isAdminSZ || !roleRequest.getRoleName().equals(RoleName.ROLE_ADMIN_SZ)) {
                Set<Role> roles = updatingUser.getRoles();
                if ((long) roles.size() == 1) {
                    roles.clear();
                }
                if (roles.contains(foundRole.get())) {
                    roles.remove(foundRole.get());
                } else {
                    roles.add(foundRole.get());
                }
                updatingUser.setRoles(roles);

                userRepository.save(updatingUser);
                return ResponseEntity.ok("Uživateli byla aktualizována role.");
            }
            return ResponseEntity.badRequest().body("Nepodařilo se aktualizovat roli. Uživatel nemá dostatečná oprávnění!");
        }
        return ResponseEntity.badRequest().body("Nepodařilo se aktualizovat roli. Role nebyla nalezena!");
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsernameIgnoreCase(username).orElseThrow();
    }


    private Role createRoleFromRoleDto(RoleDto roleDto) {
        return roleRepository.findByName(roleDto.getRoleName()).orElseThrow();
    }
}
