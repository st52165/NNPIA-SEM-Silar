package cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.service;

import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.message.request.LoginForm;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.message.request.SignUpForm;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.message.response.JwtResponse;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.message.response.ResponseMessage;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.model.Carrier;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.model.Role;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.model.RoleName;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.model.User;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.security.jwt.JwtProvider;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.security.service.UserPrinciple;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.service.interfaces.AuthenticationService;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.service.interfaces.CarrierService;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.service.interfaces.RoleService;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.service.interfaces.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder encoder;
    private final JwtProvider jwtProvider;
    private final UserService userService;
    private final RoleService roleService;
    private final CarrierService carrierService;

    public AuthenticationServiceImpl(AuthenticationManager authenticationManager, PasswordEncoder encoder,
                                     JwtProvider jwtProvider, UserService userService, RoleService roleService, CarrierService carrierService) {
        this.authenticationManager = authenticationManager;
        this.encoder = encoder;
        this.jwtProvider = jwtProvider;
        this.userService = userService;
        this.roleService = roleService;
        this.carrierService = carrierService;
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

        if (userService.existsByUsername(signUpRequest.getUsername())) {
            return new ResponseMessage("Fail -> Username je již použit!");
        }

        if (userService.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseMessage("Fail -> Email je již použit!");
        }

        Role role;
        String roleRequest = signUpRequest.getRole() == null ? "" : signUpRequest.getRole().toUpperCase();
        switch (roleRequest) {
            case "ADMIN_DS":
                role = roleService.findByName(RoleName.ROLE_ADMIN_DS);
                break;
            default:
                role = roleService.findByName(RoleName.ROLE_USER_DS);
        }

        Carrier carrier = null;
        RoleName roleName = role.getName();
        if (Objects.nonNull(signUpRequest.getCarrier())) {
            carrier = carrierService.getCarrierByName(signUpRequest.getCarrier());
        } else if (roleName == RoleName.ROLE_ADMIN_DS || roleName == RoleName.ROLE_USER_DS) {
            carrier = userService.getUserByUsername(currentUser.getUsername()).getCarrier();
        }

        User user = new User(signUpRequest.getFirstname(), signUpRequest.getLastname(),
                signUpRequest.getUsername(), signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()), role, carrier);

        userService.insertUser(user);

        return new ResponseMessage("Účet " + signUpRequest.getUsername() + " byl úspěšně vytvořen!");
    }

    @Override
    public User getLoggedUser() {
        var principles = (UserPrinciple) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return userService.getUserByUsername(principles.getUsername());
    }

}
