package cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.service.interfaces;

import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.message.request.LoginForm;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.message.request.SignUpForm;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.model.User;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.security.service.UserPrinciple;
import org.springframework.http.ResponseEntity;

public interface AuthenticationService {

    ResponseEntity<?> authenticateUser(LoginForm loginRequest);

    ResponseEntity<?> registerUser(UserPrinciple currentUser, SignUpForm signUpRequest);

    User getLoggedUser();

}
