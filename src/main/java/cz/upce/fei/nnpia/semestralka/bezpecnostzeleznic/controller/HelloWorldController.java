package cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.controller;

import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.service.interfaces.AuthenticationService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {
    private final AuthenticationService authenticationService;

    public HelloWorldController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @GetMapping({"/hello"})
    public String hello() {
        return "Hello World!";
    }


    @PreAuthorize("hasAnyRole('ADMIN_SZ')")
    @GetMapping({"/users"})
    public String user() {
        return "Hello user '" + authenticationService.getLoggedUser().getFirstname() + "'!";
    }


}
