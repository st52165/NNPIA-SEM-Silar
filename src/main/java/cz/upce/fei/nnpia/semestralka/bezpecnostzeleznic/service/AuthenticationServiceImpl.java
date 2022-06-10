package cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.service;

import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.exception.NotFoundException;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.message.request.LoginForm;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.message.request.SignUpForm;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.message.response.JwtResponse;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.message.response.ResponseMessage;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.model.Carrier;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.model.Role;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.model.RoleName;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.model.User;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.repository.CarrierRepository;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.repository.UserRepository;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.security.jwt.JwtProvider;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.security.service.UserPrinciple;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.service.interfaces.AuthenticationService;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.service.interfaces.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

import static cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.service.CarrierServiceImpl.CARRIER_NAME_NOT_FOUND;
import static cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.service.UserServiceImpl.USER_BY_USERNAME_NOT_FOUND;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder encoder;
    private final JwtProvider jwtProvider;
    private final RoleService roleService;
    private final CarrierRepository carrierRepository;
    private final UserRepository userRepository;

    public AuthenticationServiceImpl(AuthenticationManager authenticationManager, PasswordEncoder encoder,
                                     JwtProvider jwtProvider, RoleService roleService,
                                     CarrierRepository carrierRepository, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.encoder = encoder;
        this.jwtProvider = jwtProvider;
        this.roleService = roleService;
        this.carrierRepository = carrierRepository;
        this.userRepository = userRepository;
    }

    @Override
    public JwtResponse authenticateUser(LoginForm loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateJwtToken(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        return new JwtResponse(jwt, userDetails.getUsername(), userDetails.getAuthorities());
    }

    @Override
    public ResponseMessage registerUser(UserPrinciple currentUser, SignUpForm signUpRequest) {

        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, String.format("Fail -> Username '%s' již je použit!",
                    signUpRequest.getUsername()));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, String.format("Fail -> Email '%s' již je použit!",
                    signUpRequest.getEmail()));
        }

        Role role;
        String roleRequest = signUpRequest.getRole() == null ? "" : signUpRequest.getRole().toUpperCase();
        role = roleService.findByName("ADMIN_DS".equals(roleRequest) ?
                RoleName.ROLE_ADMIN_DS : RoleName.ROLE_USER_DS);

        Carrier carrier = null;
        RoleName roleName = role.getName();
        if (Objects.nonNull(signUpRequest.getCarrier())) {
            carrier = carrierRepository.findByName(signUpRequest.getCarrier()).orElseThrow(()
                    -> new NotFoundException(String.format(CARRIER_NAME_NOT_FOUND, signUpRequest.getCarrier())));
        } else if (roleName == RoleName.ROLE_ADMIN_DS || roleName == RoleName.ROLE_USER_DS) {
            carrier = getUserByUserName(currentUser.getUsername()).getCarrier();
        }

        User user = new User(signUpRequest.getFirstname(), signUpRequest.getLastname(),
                signUpRequest.getUsername(), signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()), role, carrier);

        userRepository.save(user);

        return new ResponseMessage("Účet " + signUpRequest.getUsername() + " byl úspěšně vytvořen!");
    }

    @Override
    public User getLoggedUser() {
        UserPrinciple principles = (UserPrinciple) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return getUserByUserName(principles.getUsername());
    }

    private User getUserByUserName(String userName) {
        return userRepository.findByUsernameIgnoreCase(userName).orElseThrow(()
                -> new NotFoundException(String.format(USER_BY_USERNAME_NOT_FOUND, userName)));
    }
}
