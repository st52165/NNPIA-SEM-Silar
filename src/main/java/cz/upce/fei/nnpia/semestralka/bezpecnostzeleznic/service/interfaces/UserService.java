package cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.service.interfaces;

import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.dto.*;

import java.util.List;

public interface UserService {

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    UserInfoDto getUserByUsername(String username);

    List<String> getUsers();

    boolean isUsernameExist(String username);

    List<UserInfoDto> getUserList();

    List<UserInfoDto> getUserListWithSameCarrier();

    UserInfoDto deleteUser(Long id);
}
