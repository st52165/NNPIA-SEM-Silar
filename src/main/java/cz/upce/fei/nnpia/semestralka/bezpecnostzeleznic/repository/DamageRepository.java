package cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.repository;

import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.model.Carrier;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.model.Damage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DamageRepository extends JpaRepository<Damage, Long> {
    List<Damage> findAllByIncident_Id(long id);

    Optional<Damage> findByIdAndIncident_User_Carrier(long id, Carrier carrier);
}
