package cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.service.interfaces;

import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.dto.*;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.model.User;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.security.service.UserPrinciple;

import java.util.List;

public interface UserService {

    User getUserByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    void insertUser(User user);

    UserInfoDto getUserResponseByUsername(String username);

    List<String> getUsers();

    boolean isUsernameExist(String username);

    List<UserDto> getUserList(UserPrinciple currentUser);

    List<UserDto> getUserListWithSameCarrier(String username);

    UserDto deleteUser(Long id);
}
