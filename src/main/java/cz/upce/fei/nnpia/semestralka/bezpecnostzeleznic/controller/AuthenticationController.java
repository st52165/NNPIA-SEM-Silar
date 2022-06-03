package cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.controller;

import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.message.request.LoginForm;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.message.request.SignUpForm;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.security.CurrentUser;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.security.service.UserPrinciple;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.service.interfaces.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/authentication")
public class AuthenticationController {

    private final AuthenticationService authService;

    public AuthenticationController(AuthenticationService authService) {
        this.authService = authService;
    }

    @PostMapping(path = "/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody final LoginForm loginRequest) {
        return authService.authenticateUser(loginRequest);
    }

    @PostMapping(path = "/signup")
    public ResponseEntity<?> registerUser(@CurrentUser final UserPrinciple currentUser, @Valid @RequestBody final SignUpForm signUpRequest) {
        return authService.registerUser(currentUser, signUpRequest);
    }
}
