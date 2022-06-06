package cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.service.interfaces;

import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.model.User;

public interface UserService {

    User getUserByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    void insertUser(User user);
}
