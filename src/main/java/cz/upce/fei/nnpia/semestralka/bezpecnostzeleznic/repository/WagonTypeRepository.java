package cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.repository;

import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.model.WagonType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WagonTypeRepository extends JpaRepository<WagonType, Long> {

    Optional<WagonType> findByName(String name);

    Boolean existsByName(String name);

}
