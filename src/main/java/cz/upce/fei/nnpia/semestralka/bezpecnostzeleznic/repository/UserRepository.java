package cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.repository;

import java.util.Optional;

import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    Optional<User> findByUsernameIgnoreCase(String username);

}