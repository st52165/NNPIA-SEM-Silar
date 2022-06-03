package cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.repository;

import java.util.Optional;

import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.model.Role;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(RoleName roleName);
}