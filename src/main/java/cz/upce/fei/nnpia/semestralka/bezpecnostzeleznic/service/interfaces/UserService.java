package cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.service.interfaces;

import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.dto.RoleDto;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.dto.UserEditDto;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.dto.UserPasswordDto;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.dto.UserUsernameDto;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.model.User;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.security.service.UserPrinciple;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {

    User getUserByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    void insertUser(User user);

    ResponseEntity<?> getUserResponseByUsername(String username);

    ResponseEntity<List<String>> getUsers();

    ResponseEntity<?> isUsernameExist(String username);

    ResponseEntity<?> getUserList(UserPrinciple currentUser);

    ResponseEntity<?> getUserListWithSameCarrier(String username);

    ResponseEntity<?> updatePassword(String username, UserPasswordDto passwordRequest);

    ResponseEntity<?> updateUsername(String username, UserUsernameDto nameRequest);

    ResponseEntity<?> updateOther(String username, UserEditDto userRequest);

    ResponseEntity<?> deleteUser(Long id);

    ResponseEntity<?> addRole(String username, RoleDto roleRequest);

    ResponseEntity<?> updateUserRole(String username, RoleDto roleRequest);
}
