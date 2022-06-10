package cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.repository;

import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.model.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {

    Optional<Region> findByName(String regionName);

    Optional<Region> findByNameIgnoreCase(String regionName);

    Boolean existsByName(String regionName);

}
